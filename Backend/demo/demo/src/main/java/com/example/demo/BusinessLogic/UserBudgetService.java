package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.UserBudgetRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserBudgetService {

    private final UserBudgetRepository userBudgetRepository;

    public UserBudgetService(UserBudgetRepository userBudgetRepository) {
        this.userBudgetRepository = userBudgetRepository;
    }

    public UserBudget save(UserBudget userBudget) {
        return userBudgetRepository.save(userBudget);
    }

    public List<UserBudget> findAll() {
        return userBudgetRepository.findAll();
    }

    public List<UserBudget> findByUser(User user) {
        return userBudgetRepository.findAll().stream()
                .filter(ub -> ub.getUser().equals(user))
                .collect(Collectors.toList());
    }

    public UserBudget linkUserToBudget(User user, Budget budget) {
        if (user == null || budget == null) {
            throw new IllegalArgumentException("User or Budget cannot be null.");
        }

        // Optional: avoid duplicates
        boolean alreadyLinked = userBudgetRepository.findAll().stream()
                .anyMatch(ub -> ub.getUser().equals(user) && ub.getBudget().equals(budget));
        if (alreadyLinked) {
            throw new IllegalStateException("This user is already linked to the given budget.");
        }

        UserBudget userBudget = new UserBudget(user, budget);
        return userBudgetRepository.save(userBudget);
    }
}
