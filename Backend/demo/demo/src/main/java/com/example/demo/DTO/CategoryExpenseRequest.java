// src/main/java/com/example/demo/dto/CategoryExpenseDto.java

package com.example.demo.DTO;

public class CategoryExpenseRequest {
    private String categoryName;
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
