package com.example.demo.Controllers;

import com.example.demo.DTO.CategoryExpenseRequest;
import com.example.demo.Model.Product;
import com.example.demo.BusinessLogic.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/grouped-by-category")
    public ResponseEntity<List<CategoryExpenseRequest>> getExpensesGroupedByCategory() {
        List<CategoryExpenseRequest> results = productService.getExpensesGroupedByCategory();
        return ResponseEntity.ok(results);
    }

    // New endpoint to retrieve products purchased by a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUser(@PathVariable Long userId) {
        List<Product> products = productService.getProductsByUser(userId);
        return ResponseEntity.ok(products);
    }
}
