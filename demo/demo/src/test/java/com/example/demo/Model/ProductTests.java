package com.example.demo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class ProductTests {

    @Test
    public void testDefaultConstructorAndSetters() {
        Product product = new Product();
        assertNull(product.getId());
        product.setName("Test Product");
        product.setPrice(19.99);
        LocalDate date = LocalDate.now();
        product.setDate(date);
        Category category = new Category("Test Category");
        product.setCategory(category);
        assertEquals("Test Product", product.getName());
        assertEquals(19.99, product.getPrice());
        assertEquals(date, product.getDate());
        assertEquals(category, product.getCategory());
    }

    @Test
    public void testParameterizedConstructor() {
        Category category = new Category("Books");
        LocalDate date = LocalDate.of(2020, 1, 1);
        Product product = new Product("Book", 29.99, date, category);
        assertNull(product.getId());
        assertEquals("Book", product.getName());
        assertEquals(29.99, product.getPrice());
        assertEquals(date, product.getDate());
        assertEquals(category, product.getCategory());
    }
}
