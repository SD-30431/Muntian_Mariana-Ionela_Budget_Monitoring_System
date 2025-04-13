package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.CategoryService;
import com.example.demo.Model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(com.example.demo.controller.CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testCreateCategory() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        when(categoryService.save(any(Category.class))).thenReturn(category);
        String categoryJson = "{\"name\": \"Test Category\"}";
        mockMvc.perform(post("/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Category"));
    }

    @Test
    public void testGetAll() throws Exception {
        Category category1 = new Category();
        category1.setName("Category 1");
        Category category2 = new Category();
        category2.setName("Category 2");
        List<Category> categories = Arrays.asList(category1, category2);
        when(categoryService.findAll()).thenReturn(categories);
        mockMvc.perform(get("/category/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Category 1"))
                .andExpect(jsonPath("$[1].name").value("Category 2"));
    }
}
