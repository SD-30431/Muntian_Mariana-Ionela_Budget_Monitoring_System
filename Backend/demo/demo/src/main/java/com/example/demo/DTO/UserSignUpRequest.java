package com.example.demo.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserSignUpRequest {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Income is required")
    private Double income;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Double getIncome() { return income; }
    public void setIncome(Double income) { this.income = income; }
}
