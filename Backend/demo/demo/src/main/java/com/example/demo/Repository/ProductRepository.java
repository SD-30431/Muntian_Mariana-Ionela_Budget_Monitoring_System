package com.example.demo.Repository;

import com.example.demo.DTO.CategoryExpenseRequest;
import com.example.demo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    @Query("SELECT new com.example.demo.DTO.CategoryExpenseRequest(p.category.name, SUM(p.price)) " +
            "FROM Product p GROUP BY p.category.name")
    List<CategoryExpenseRequest> findExpensesGroupedByCategory();

    // New query method to retrieve products purchased by a specific user
    List<Product> findByUserId(Long userId);
}
