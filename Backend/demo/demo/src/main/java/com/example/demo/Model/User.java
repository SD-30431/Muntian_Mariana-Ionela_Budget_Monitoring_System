package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 2, message = "Password must be at least 2 characters long")
    @JsonIgnore
    private String passwordHash;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be non-negative")
    private Double salary;

    @Column(name = "profile_picture")
    private String profilePicture;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-userBudget")
    private Set<UserBudget> userBudgets = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> purchasedProducts = new ArrayList<>();

    public User() {}

    public User(String username, String passwordHash, Double salary) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salary = salary;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public Set<UserBudget> getUserBudgets() { return userBudgets; }
    public void setUserBudgets(Set<UserBudget> userBudgets) { this.userBudgets = userBudgets; }

    public List<Product> getPurchasedProducts() { return purchasedProducts; }
    public void setPurchasedProducts(List<Product> purchasedProducts) { this.purchasedProducts = purchasedProducts; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }
}
