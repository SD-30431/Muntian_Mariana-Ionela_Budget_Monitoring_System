package com.example.demo.BusinessLogic;

import com.example.demo.Model.Budget;
import com.example.demo.Model.User;
import com.example.demo.Model.UserBudget;
import com.example.demo.Repository.UserBudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserBudgetServiceTests {

    @Mock
    private UserBudgetRepository userBudgetRepository;

    @InjectMocks
    private UserBudgetService userBudgetService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        UserBudget userBudget = new UserBudget();
        when(userBudgetRepository.save(userBudget)).thenReturn(userBudget);
        UserBudget result = userBudgetService.save(userBudget);
        assertNotNull(result);
        verify(userBudgetRepository, times(1)).save(userBudget);
    }

    @Test
    public void testFindAll() {
        UserBudget ub1 = new UserBudget();
        UserBudget ub2 = new UserBudget();
        List<UserBudget> list = Arrays.asList(ub1, ub2);
        when(userBudgetRepository.findAll()).thenReturn(list);
        List<UserBudget> result = userBudgetService.findAll();
        assertEquals(2, result.size());
        verify(userBudgetRepository, times(1)).findAll();
    }

    @Test
    public void testFindByUser() {
        User user = new User();
        User otherUser = new User();
        UserBudget ub1 = new UserBudget();
        ub1.setUser(user);
        UserBudget ub2 = new UserBudget();
        ub2.setUser(otherUser);
        UserBudget ub3 = new UserBudget();
        ub3.setUser(user);
        List<UserBudget> list = Arrays.asList(ub1, ub2, ub3);
        when(userBudgetRepository.findAll()).thenReturn(list);
        List<UserBudget> result = userBudgetService.findByUser(user);
        assertEquals(2, result.size());
        for (UserBudget ub : result) {
            assertEquals(user, ub.getUser());
        }
        verify(userBudgetRepository, times(1)).findAll();
    }

    @Test
    public void testLinkUserToBudget() {
        User user = new User();
        Budget budget = new Budget();
        UserBudget userBudget = new UserBudget(user, budget);
        when(userBudgetRepository.save(any(UserBudget.class))).thenReturn(userBudget);
        UserBudget result = userBudgetService.linkUserToBudget(user, budget);
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(budget, result.getBudget());
        verify(userBudgetRepository, times(1)).save(any(UserBudget.class));
    }
}
