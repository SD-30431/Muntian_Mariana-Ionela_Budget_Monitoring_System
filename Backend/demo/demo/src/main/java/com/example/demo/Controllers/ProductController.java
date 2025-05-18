package com.example.demo.Controllers;

import com.example.demo.DTO.CategoryExpenseRequest;
import com.example.demo.Model.Product;
import com.example.demo.BusinessLogic.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            Product savedProduct = productService.save(product);
            return ResponseEntity.ok(savedProduct);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/grouped-by-category")
    public ResponseEntity<List<CategoryExpenseRequest>> getExpensesGroupedByCategory() {
        return ResponseEntity.ok(productService.getExpensesGroupedByCategory());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Product>> getProductsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(productService.getProductsByUser(userId));
    }
}
