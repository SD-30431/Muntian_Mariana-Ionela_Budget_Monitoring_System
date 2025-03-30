package com.example.demo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserBudgetTests {

    @Test
    public void testDefaultConstructorAndSetters() {
        UserBudget userBudget = new UserBudget();
        assertNull(userBudget.getId());
        User user = new User();
        user.setUsername("testUser");
        Budget budget = new Budget(100.0, "1111-2222-3333-4444");
        userBudget.setUser(user);
        userBudget.setBudget(budget);
        assertEquals(user, userBudget.getUser());
        assertEquals(budget, userBudget.getBudget());
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User();
        user.setUsername("testUser");
        Budget budget = new Budget(200.0, "5555-6666-7777-8888");
        UserBudget userBudget = new UserBudget(user, budget);
        assertNull(userBudget.getId());
        assertEquals(user, userBudget.getUser());
        assertEquals(budget, userBudget.getBudget());
    }
}
