package com.example.demo.BusinessLogic;

import com.example.demo.Config.SecurityHelper;
import com.example.demo.DTO.*;
import com.example.demo.Model.ActivityLog;
import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.UserRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BudgetService budgetService;
    private final UserBudgetService userBudgetService;
    private final SecurityHelper securityHelper;
    private final ActivityLogService activityLogService;

    public UserService(UserRepository userRepository,
                       BudgetService budgetService,
                       UserBudgetService userBudgetService,
                       SecurityHelper securityHelper,
                       ActivityLogService activityLogService) {
        this.userRepository = userRepository;
        this.budgetService = budgetService;
        this.userBudgetService = userBudgetService;
        this.securityHelper = securityHelper;
        this.activityLogService = activityLogService;
    }

    public ResponseEntity<?> registerNewUser(UserSignUpRequest request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username already exists."));
        }

        String hashedPassword = securityHelper.hashPassword(request.getPassword());
        User user = new User(request.getUsername(), hashedPassword, request.getIncome());
        User savedUser = userRepository.save(user);

        Budget defaultBudget = new Budget(request.getIncome(), "CARD-" + UUID.randomUUID().toString().substring(0, 8));
        Budget savedBudget = budgetService.save(defaultBudget);
        userBudgetService.linkUserToBudget(savedUser, savedBudget);

        Map<String, Object> responseUser = new HashMap<>();
        responseUser.put("id", savedUser.getId());
        responseUser.put("username", savedUser.getUsername());
        responseUser.put("salary", savedUser.getSalary());
        responseUser.put("profilePicture", savedUser.getProfilePicture());

        return ResponseEntity.ok(Map.of("success", true, "user", responseUser));
    }

    public ResponseEntity<?> login(UserLoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found."));
        }

        String hashedInput = securityHelper.hashPassword(loginRequest.getPassword());
        if (!user.getPasswordHash().equals(hashedInput)) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Invalid password."));
        }

        activityLogService.saveActivity(user.getUsername(), "logged in");

        Map<String, Object> responseUser = new HashMap<>();
        responseUser.put("id", user.getId());
        responseUser.put("username", user.getUsername());
        responseUser.put("salary", user.getSalary());
        responseUser.put("profilePicture", user.getProfilePicture());

        return ResponseEntity.ok(Map.of("success", true, "user", responseUser));
    }

    public ResponseEntity<?> logout(String username) {
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

    public List<ActivityLog> getAllActivities() {
        return activityLogService.getAllActivities();
    }

    public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found."));
        }
        user.setPasswordHash(securityHelper.hashPassword(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true, "message", "Password updated successfully."));
    }

    public ResponseEntity<List<Budget>> getBudgetsByUsernameResponse(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || user.getUserBudgets() == null) {
            return ResponseEntity.ok(new ArrayList<>());
        }
        List<Budget> budgets = user.getUserBudgets().stream()
                .map(UserBudget::getBudget)
                .collect(Collectors.toList());
        return ResponseEntity.ok(budgets);
    }

    public List<UserSummaryDTO> getAllUsersWithoutPasswords() {
        return userRepository.findAll().stream()
                .map(user -> new UserSummaryDTO(
                        user.getUsername(),
                        user.getSalary(),
                        user.getProfilePicture()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> saveProfilePicture(String username, MultipartFile file) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "User not found."));
        }

        String uploadDir = "uploads/";
        File directory = new File(System.getProperty("user.dir") + File.separator + uploadDir);
        if (!directory.exists()) directory.mkdirs();

        String filename = username + "_" + file.getOriginalFilename();
        try {
            File destinationFile = new File(directory, filename);
            file.transferTo(destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Failed to save file."));
        }

        user.setProfilePicture(filename);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "filename", filename));
    }

    public ResponseEntity<Resource> loadProfilePicture(String filename) {
        try {
            File file = new File(System.getProperty("user.dir") + File.separator + "uploads" + File.separator + filename);
            Resource resource = new UrlResource(file.toURI());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    public ResponseEntity<?> transfer(TransferRequest request) {
        try {
            String fromCard = (String) request.getFromCard();
            String toCard = (String) request.getToCard();
            Double amount = Double.parseDouble(request.getAmount().toString());

            if (fromCard.equals(toCard)) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Cannot transfer to the same card."));
            }
            Budget fromBudget = budgetService.findByCardNumber(fromCard);
            Budget toBudget = budgetService.findByCardNumber(toCard);

            if (fromBudget == null || toBudget == null) {
                return ResponseEntity.status(404).body(Map.of("success", false, "message", "Card not found."));
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

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
