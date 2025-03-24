package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.ProductService;
import com.example.demo.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @GetMapping("/all")
    public List<Product> getAll() {
        return productService.findAll();
    }
}
