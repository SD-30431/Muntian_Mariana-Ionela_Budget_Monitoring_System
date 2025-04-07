package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.UserBudgetService;
import com.example.demo.BusinessLogic.UserService;
import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.Model.UserBudget;
import com.example.demo.Model.User;
import com.example.demo.Model.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userbudget")
public class UserBudgetController {

    @Autowired
    private UserBudgetService userBudgetService;

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/link")
    public UserBudget linkUserBudget(@RequestParam String username, @RequestParam String cardnumber) {
        User user = userService.findByUsername(username);
        Budget budget = budgetService.findByCardNumber(cardnumber);

        if (user != null && budget != null) {
            return userBudgetService.linkUserToBudget(user, budget);
        }
        return null;
    }
}
