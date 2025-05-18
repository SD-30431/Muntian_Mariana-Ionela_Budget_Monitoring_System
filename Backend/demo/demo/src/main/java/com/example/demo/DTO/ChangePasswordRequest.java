package com.example.demo.DTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotBlank(message = "New password is required")
    @Size(min = 3, message = "New password must be at least 3 characters long")
    private String newPassword;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
