package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.ActivityLogService;
import com.example.demo.DTO.ChangePasswordRequest;
import com.example.demo.DTO.UserLoginRequest;
import com.example.demo.DTO.UserSignUpRequest;
import com.example.demo.Model.ActivityLog;
import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.BusinessLogic.UserBudgetService;
import com.example.demo.BusinessLogic.UserService;
import com.example.demo.Config.SecurityHelper;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BudgetService budgetService;
    private final UserBudgetService userBudgetService;
    private final SecurityHelper securityHelper;
    private final ActivityLogService activityLogService;

    public UserController(UserService userService, BudgetService budgetService, UserBudgetService userBudgetService, SecurityHelper securityHelper, ActivityLogService activityLogService) {
        this.userService = userService;
        this.budgetService = budgetService;
        this.userBudgetService = userBudgetService;
        this.securityHelper = securityHelper;
        this.activityLogService = activityLogService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserSignUpRequest request) {
        if (userService.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username already exists."));
        }
        String hashedPassword = securityHelper.hashPassword(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(hashedPassword);
        user.setSalary(request.getIncome());
        User savedUser = userService.save(user);

        String generatedCardNumber = "CARD-" + UUID.randomUUID().toString().substring(0, 8);
        Budget defaultBudget = new Budget(request.getIncome(), generatedCardNumber);
        Budget savedBudget = budgetService.save(defaultBudget);
        userBudgetService.linkUserToBudget(savedUser, savedBudget);

        savedUser.setPasswordHash(null);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequest loginRequest) {
        User user = userService.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found."));
        }
        String hashedInput = securityHelper.hashPassword(loginRequest.getPassword());
        if (!user.getPasswordHash().equals(hashedInput)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid password."));
        }

        try {
            activityLogService.saveActivity(user.getUsername(), "logged in");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("⚠️ Failed to save login activity: " + e.getMessage());
        }

        user.setPasswordHash(null); // ✅ moved AFTER password check and logging

        return ResponseEntity.ok(Map.of("success", true, "user", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username is required for logout."));
        }

        try {
            activityLogService.saveActivity(username, "logged out");
            return ResponseEntity.ok(Map.of("success", true, "message", "Logout activity recorded."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Failed to record logout activity."));
        }
    }

    @GetMapping("/activity-logs")
    public ResponseEntity<List<ActivityLog>> getActivityLog() {
        List<ActivityLog> logs = activityLogService.getAllActivities();
        return ResponseEntity.ok(logs);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found."));
        }
        String newHashed = securityHelper.hashPassword(request.getNewPassword());
        user.setPasswordHash(newHashed);
        userService.save(user);
        return ResponseEntity.ok(Map.of("success", true, "message", "Password updated successfully."));
    }

    @GetMapping("/{username}/budgets")
    public ResponseEntity<?> getBudgetsByUsername(@PathVariable String username) {
        List<Budget> budgets = userService.getBudgetsByUsername(username);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        users.forEach(user -> user.setPasswordHash(null));
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{username}/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found."));
        }

        String uploadDir = "uploads/";
        File directory = new File(System.getProperty("user.dir") + File.separator + uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = username + "_" + file.getOriginalFilename();
        try {
            File destinationFile = new File(directory, filename);
            file.transferTo(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Failed to save file."));
        }

        user.setProfilePicture(filename);
        userService.save(user);

        return ResponseEntity.ok(Map.of("success", true, "filename", filename));
    }

    @GetMapping("/profile-picture/{filename:.+}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        try {
            Resource file = new UrlResource(new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + filename).toURI());
            if (file.exists() || file.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(file);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody Map<String, Object> request) {
        try {
            String fromCard = (String) request.get("fromCard");
            String toCard = (String) request.get("toCard");
            Double amount = Double.parseDouble(request.get("amount").toString());

            if (fromCard.equals(toCard)) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Cannot transfer to the same card."));
            }

            Budget fromBudget = budgetService.findByCardNumber(fromCard);
            Budget toBudget = budgetService.findByCardNumber(toCard);

            if (fromBudget == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "Sender card not found."));
            }

            if (toBudget == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "Recipient card not found."));
            }

            if (fromBudget.getAmount() < amount) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Insufficient balance."));
            }

            fromBudget.setAmount(fromBudget.getAmount() - amount);
            toBudget.setAmount(toBudget.getAmount() + amount);

            budgetService.save(fromBudget);
            budgetService.save(toBudget);

            return ResponseEntity.ok(Map.of("success", true, "message", "Transfer successful!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Transfer failed."));
        }
    }
}
