package com.example.demo.Controllers;

import com.example.demo.BusinessLogic.ProductService;
import com.example.demo.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

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

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @GetMapping("/grouped-by-category")
    public List<Map<String, Object>> getExpensesGroupedByCategory() {
        List<Product> products = productService.findAll();
        Map<String, Double> totals = products.stream().collect(
                Collectors.groupingBy(
                        p -> (p.getCategory() != null && p.getCategory().getName() != null)
                                ? p.getCategory().getName()
                                : "Uncategorized",
                        Collectors.summingDouble(Product::getPrice)
                )
        );
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : totals.entrySet()) {
            Map<String, Object> map = new HashMap<>();
            map.put("_id", entry.getKey());
            map.put("total", entry.getValue());
            result.add(map);
        }
        return result;
    }
}
