package com.example.demo.Controllers;

import com.example.demo.Model.Budget;
import com.example.demo.BusinessLogic.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/create")
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        Budget savedBudget = budgetService.save(budget);
        return ResponseEntity.ok(savedBudget);
    }

    @GetMapping("/{cardnumber}")
    public ResponseEntity<Budget> getBudget(@PathVariable String cardnumber) {
        Budget budget = budgetService.findByCardNumber(cardnumber);
        if (budget == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(budget);
    }
    // Added update endpoint
    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budget) {
        Budget existingBudget = budgetService.findById(id);
        if (existingBudget == null) {
            return ResponseEntity.notFound().build();
        }
        // Update the budget amount based on the incoming value.
        existingBudget.setAmount(budget.getAmount());
        Budget updatedBudget = budgetService.save(existingBudget);
        return ResponseEntity.ok(updatedBudget);
    }
}
