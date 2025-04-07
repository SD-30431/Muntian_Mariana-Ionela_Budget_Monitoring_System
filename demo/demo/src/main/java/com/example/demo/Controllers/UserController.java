package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.UserService;
import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.BusinessLogic.UserBudgetService;
import com.example.demo.Config.SecurityHelper;
import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityHelper securityHelper;

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private UserBudgetService userBudgetService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody SignUpRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                request.getPassword() == null || request.getPassword().trim().isEmpty() ||
                request.getIncome() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "All fields are required."));
        }

        String hashed = securityHelper.hashPassword(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(hashed);
        user.setSalary(request.getIncome());

        User savedUser = userService.save(user);

        String generatedCardNumber = "CARD-" + UUID.randomUUID().toString().substring(0, 8);
        Budget defaultBudget = new Budget(request.getIncome(), generatedCardNumber);
        Budget savedBudget = budgetService.save(defaultBudget);

        UserBudget linkedBudget = userBudgetService.linkUserToBudget(savedUser, savedBudget);

        savedUser.setPasswordHash(null);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "All fields are required."));
        }

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", "Invalid username."));
        }

        String hashedInput = securityHelper.hashPassword(password);
        if (!user.getPasswordHash().equals(hashedInput)) {
            return ResponseEntity.status(401)
                    .body(Map.of("success", false, "message", "Invalid password."));
        }

        user.setPasswordHash(null);
        return ResponseEntity.ok(Map.of("success", true, "user", user));
    }

    @GetMapping("/{username}/budgets")
    public ResponseEntity<?> getBudgetsByUsername(@PathVariable String username) {
        List<Budget> budgets = userService.getBudgetsByUsername(username);
        return ResponseEntity.ok(budgets);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty() ||
                request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Username and new password are required."));
        }
        User user = userService.findByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(404)
                    .body(Map.of("success", false, "message", "User not found."));
        }
        String hashed = securityHelper.hashPassword(request.getNewPassword());
        user.setPasswordHash(hashed);
        userService.save(user);
        return ResponseEntity.ok(Map.of("success", true, "message", "Password updated successfully."));
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {}

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class SignUpRequest {
        private String username;
        private String password;
        private Double income;

        public SignUpRequest() {}

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Double getIncome() { return income; }
        public void setIncome(Double income) { this.income = income; }
    }

    public static class ChangePasswordRequest {
        private String username;
        private String newPassword;

        public ChangePasswordRequest() {}

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}
