package com.example.demo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

public class BudgetTests {

    @Test
    public void testDefaultConstructorAndSetters() {
        Budget budget = new Budget();
        assertNull(budget.getId());
        budget.setAmount(150.0);
        budget.setCardnumber("1111-2222-3333-4444");
        Set<UserBudget> userBudgets = new HashSet<>();
        budget.setUserBudgets(userBudgets);
        assertEquals(150.0, budget.getAmount());
        assertEquals("1111-2222-3333-4444", budget.getCardnumber());
        assertEquals(userBudgets, budget.getUserBudgets());
    }

    @Test
    public void testParameterizedConstructor() {
        Budget budget = new Budget(250.0, "5555-6666-7777-8888");
        assertNull(budget.getId());
        assertEquals(250.0, budget.getAmount());
        assertEquals("5555-6666-7777-8888", budget.getCardnumber());
    }
}
