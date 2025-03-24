package com.example.demo.Repository;

import com.example.demo.Model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Budget findByCardnumber(String cardnumber);
}
