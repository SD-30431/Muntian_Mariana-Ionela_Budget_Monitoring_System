package com.example.demo.BusinessLogic;

import com.example.demo.DTO.CategoryExpenseRequest;
import com.example.demo.Model.Product;
import com.example.demo.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public List<Product> findAllWithCategory() {
        return productRepository.findAllWithCategory();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<CategoryExpenseRequest> getExpensesGroupedByCategory() {
        return productRepository.findExpensesGroupedByCategory();
    }

    // New method to get purchased products for a specific user
    public List<Product> getProductsByUser(Long userId) {
        return productRepository.findByUserId(userId);
    }
}
