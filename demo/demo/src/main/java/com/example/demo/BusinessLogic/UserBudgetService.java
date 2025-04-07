package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.UserBudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserBudgetService {

    @Autowired
    private UserBudgetRepository userBudgetRepository;

    public UserBudget save(UserBudget userBudget) {
        return userBudgetRepository.save(userBudget);
    }

    public List<UserBudget> findAll() {
        return userBudgetRepository.findAll();
    }

    public List<UserBudget> findByUser(User user) {
        return userBudgetRepository.findAll()
                .stream()
                .filter(ub -> ub.getUser().equals(user))
                .toList();
    }

    public UserBudget linkUserToBudget(User user, Budget budget) {
        UserBudget userBudget = new UserBudget(user, budget);
        return userBudgetRepository.save(userBudget);
    }
}
