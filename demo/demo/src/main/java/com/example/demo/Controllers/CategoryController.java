package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.CategoryService;
import com.example.demo.Model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/create")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.save(category);
    }

    @GetMapping("/all")
    public List<Category> getAll() {
        return categoryService.findAll();
    }
}
