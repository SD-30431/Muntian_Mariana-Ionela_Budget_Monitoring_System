package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.BudgetService;
import com.example.demo.Model.Budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/create")
    public Budget createBudget(@RequestBody Budget budget) {
        return budgetService.save(budget);
    }

    @GetMapping("/{cardnumber}")
    public Budget getBudget(@PathVariable String cardnumber) {
        return budgetService.findByCardNumber(cardnumber);
    }
}
