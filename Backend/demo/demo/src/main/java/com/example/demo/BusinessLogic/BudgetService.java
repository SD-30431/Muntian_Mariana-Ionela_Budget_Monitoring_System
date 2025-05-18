package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Repository.BudgetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BudgetService {

    private final BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget findByCardNumber(String cardnumber) {
        return budgetRepository.findByCardnumber(cardnumber);
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget findById(Long id) {
        return budgetRepository.findById(id).orElse(null);
    }

    public Budget updateAmount(Long id, double newAmount) {
        Budget budget = findById(id);
        if (budget != null) {
            budget.setAmount(newAmount);
            return budgetRepository.save(budget);
        }
        return null;
    }
}
