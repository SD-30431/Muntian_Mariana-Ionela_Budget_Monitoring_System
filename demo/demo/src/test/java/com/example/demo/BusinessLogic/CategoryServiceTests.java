package com.example.demo.BusinessLogic;

import com.example.demo.Model.Category;
import com.example.demo.Repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Category category = new Category();
        category.setName("Test Category");

        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.save(category);

        assertNotNull(result);
        assertEquals("Test Category", result.getName());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testFindAll() {
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Category 1", result.get(0).getName());
        assertEquals("Category 2", result.get(1).getName());
        verify(categoryRepository, times(1)).findAll();
    }
}
