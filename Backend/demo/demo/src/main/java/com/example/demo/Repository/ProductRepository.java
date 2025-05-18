package com.example.demo.Repository;

import com.example.demo.DTO.CategoryExpenseRequest;
import com.example.demo.DTO.MonthlyExpenseDTO;
import com.example.demo.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAllWithCategory();

    @Query("SELECT new com.example.demo.DTO.CategoryExpenseRequest(p.category.name, SUM(p.price)) " +
            "FROM Product p GROUP BY p.category.name")
    List<CategoryExpenseRequest> findExpensesGroupedByCategory();

    List<Product> findByUserId(Long userId);
    List<Product> findByUserUsername(String username);
    @Query("SELECT new com.example.demo.DTO.MonthlyExpenseDTO(FUNCTION('to_char', p.date, 'FMMonth'), SUM(p.price)) " +
            "FROM Product p WHERE p.user.username = :username " +
            "GROUP BY FUNCTION('to_char', p.date, 'FMMonth'), MONTH(p.date) " +
            "ORDER BY MONTH(p.date)")
    List<MonthlyExpenseDTO> findMonthlyExpensesByUsername(@Param("username") String username);

}
