// src/main/java/com/example/demo/DTO/UserSummaryDTO.java
package com.example.demo.DTO;

public class UserSummaryDTO {
    private String username;
    private Double salary;
    private String profilePicture;

    public UserSummaryDTO(String username, Double salary, String profilePicture) {
        this.username = username;
        this.salary = salary;
        this.profilePicture = profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public Double getSalary() {
        return salary;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
