package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.UserRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<Budget> getBudgetsByUsername(String username) {
        User user = findByUsername(username);
        if (user == null || user.getUserBudgets() == null) {
            return new ArrayList<>();
        }
        return user.getUserBudgets().stream()
                .map(UserBudget::getBudget)
                .collect(Collectors.toList());
    }
}
