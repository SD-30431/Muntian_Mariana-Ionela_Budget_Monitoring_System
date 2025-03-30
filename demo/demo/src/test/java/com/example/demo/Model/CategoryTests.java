package com.example.demo.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryTests {

    @Test
    public void testDefaultConstructorAndSetters() {
        Category category = new Category();
        assertNull(category.getId());
        category.setName("Electronics");
        List<Product> products = new ArrayList<>();
        category.setProducts(products);
        assertEquals("Electronics", category.getName());
        assertEquals(products, category.getProducts());
    }

    @Test
    public void testParameterizedConstructor() {
        Category category = new Category("Books");
        assertNull(category.getId());
        assertEquals("Books", category.getName());
        assertTrue(category.getProducts().isEmpty());
    }
}
