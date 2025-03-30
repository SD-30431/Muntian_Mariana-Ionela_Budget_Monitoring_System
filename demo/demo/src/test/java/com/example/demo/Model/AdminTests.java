package com.example.demo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdminTests {

    @Test
    public void testDefaultConstructorAndSetters() {
        Admin admin = new Admin();
        assertNull(admin.getId());
        admin.setUsername("adminUser");
        admin.setPassword("adminPass");
        assertEquals("adminUser", admin.getUsername());
        assertEquals("adminPass", admin.getPassword());
    }

    @Test
    public void testParameterizedConstructor() {
        Admin admin = new Admin("adminUser", "adminPass");
        assertNull(admin.getId());
        assertEquals("adminUser", admin.getUsername());
        assertEquals("adminPass", admin.getPassword());
    }
}
