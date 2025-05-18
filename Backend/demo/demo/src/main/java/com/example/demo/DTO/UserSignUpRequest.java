package com.example.demo.DTO;

import javax.validation.constraints.*;

public class UserSignUpRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 3 characters long")
    private String password;

    @NotNull(message = "Income is required")
    @PositiveOrZero(message = "Income must be zero or a positive number")
    private Double income;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Double getIncome() { return income; }
    public void setIncome(Double income) { this.income = income; }
}
