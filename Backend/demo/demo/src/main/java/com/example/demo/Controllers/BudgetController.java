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
        return ResponseEntity.ok(budgetService.save(budget));
    }

    @GetMapping("/{cardnumber}")
    public ResponseEntity<Budget> getBudget(@PathVariable String cardnumber) {
        Budget budget = budgetService.findByCardNumber(cardnumber);
        return budget != null ? ResponseEntity.ok(budget) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budgetDetails) {
        Budget updated = budgetService.updateAmount(id, budgetDetails.getAmount());
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}
