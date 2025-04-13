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
        User user = userService.findByUsername(username);
        Budget budget = budgetService.findByCardNumber(cardnumber);
        if (user != null && budget != null) {
            UserBudget linkedBudget = userBudgetService.linkUserToBudget(user, budget);
            return ResponseEntity.ok(linkedBudget);
        }
        return ResponseEntity.badRequest().body("{\"message\": \"User or budget not found.\"}");
    }
}
