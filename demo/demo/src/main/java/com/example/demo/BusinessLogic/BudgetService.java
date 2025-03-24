package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    public Budget findByCardNumber(String cardnumber) {
        return budgetRepository.findByCardnumber(cardnumber);
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    // Additional logic
}
