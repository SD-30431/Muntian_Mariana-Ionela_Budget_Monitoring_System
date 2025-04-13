package com.example.demo.Controllers;

import com.example.demo.DTO.ChangePasswordRequest;
import com.example.demo.DTO.UserLoginRequest;
import com.example.demo.DTO.UserSignUpRequest;
import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.BusinessLogic.UserBudgetService;
import com.example.demo.BusinessLogic.UserService;
import com.example.demo.Config.SecurityHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BudgetService budgetService;
    private final UserBudgetService userBudgetService;
    private final SecurityHelper securityHelper;

    public UserController(UserService userService, BudgetService budgetService, UserBudgetService userBudgetService, SecurityHelper securityHelper) {
        this.userService = userService;
        this.budgetService = budgetService;
        this.userBudgetService = userBudgetService;
        this.securityHelper = securityHelper;
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

        // Create default budget and link it to the user
        String generatedCardNumber = "CARD-" + UUID.randomUUID().toString().substring(0, 8);
        Budget defaultBudget = new Budget(request.getIncome(), generatedCardNumber);
        Budget savedBudget = budgetService.save(defaultBudget);
        UserBudget linkedBudget = userBudgetService.linkUserToBudget(savedUser, savedBudget);

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
        user.setPasswordHash(null);
        return ResponseEntity.ok(Map.of("success", true, "user", user));
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
}
