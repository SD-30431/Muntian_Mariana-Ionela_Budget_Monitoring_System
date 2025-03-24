package com.example.demo.Repository;

import com.example.demo.Model.UserBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBudgetRepository extends JpaRepository<UserBudget, Integer> {
}
