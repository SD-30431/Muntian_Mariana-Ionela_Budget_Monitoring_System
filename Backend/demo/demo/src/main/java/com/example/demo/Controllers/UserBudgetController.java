package com.example.demo.Controllers;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.BusinessLogic.UserBudgetService;
import com.example.demo.BusinessLogic.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userbudget")
@CrossOrigin(origins = "http://localhost:4200")
public class UserBudgetController {

    private final UserBudgetService userBudgetService;
    private final UserService userService;
    private final BudgetService budgetService;

    public UserBudgetController(UserBudgetService userBudgetService, UserService userService, BudgetService budgetService) {
        this.userBudgetService = userBudgetService;
        this.userService = userService;
        this.budgetService = budgetService;
    }

    @PostMapping("/link")
    public ResponseEntity<?> linkUserBudget(@RequestParam String username, @RequestParam String cardnumber) {
        try {
            User user = userService.findByUsername(username);
            Budget budget = budgetService.findByCardNumber(cardnumber);

            if (user == null || budget == null) {
                return ResponseEntity.badRequest().body("{\"message\": \"User or budget not found.\"}");
            }

            UserBudget linkedBudget = userBudgetService.linkUserToBudget(user, budget);
            return ResponseEntity.ok(linkedBudget);

        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("{\"message\": \"" + ex.getMessage() + "\"}");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("{\"message\": \"Unexpected error occurred.\"}");
        }
    }
}
