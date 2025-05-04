package com.example.demo.DTO;

import javax.validation.constraints.*;

public class CategoryExpenseRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String categoryName;

    @NotNull(message = "Total expense is required")
    @PositiveOrZero(message = "Total must be zero or a positive value")
    private Double total;

    public CategoryExpenseRequest(String categoryName, Double total) {
        this.categoryName = categoryName;
        this.total = total;
    }

    // Getters and setters
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
