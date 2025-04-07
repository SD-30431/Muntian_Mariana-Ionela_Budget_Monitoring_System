package com.example.demo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTests {
    @Test
    public void testDefaultConstructorAndSetters() {
        User user = new User();
        assertNull(user.getId());
        user.setUsername("testUser");
        user.setPasswordHash("hashedPassword");
        user.setSalary(50000.0);
        assertEquals("testUser", user.getUsername());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(50000.0, user.getSalary());
        assertNotNull(user.getUserBudgets());
        assertTrue(user.getUserBudgets().isEmpty());
    }
    @Test
    public void testParameterizedConstructor() {
        User user = new User("testUser", "hashedPassword", 60000.0);
        assertNull(user.getId());
        assertEquals("testUser", user.getUsername());
        assertEquals("hashedPassword", user.getPasswordHash());
        assertEquals(60000.0, user.getSalary());
        assertNotNull(user.getUserBudgets());
        assertTrue(user.getUserBudgets().isEmpty());
    }
}
