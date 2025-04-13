package com.example.demo.Model;

import com.example.demo.Model.UserBudget;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String passwordHash;

    private Double salary;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-userBudget")
    private Set<UserBudget> userBudgets = new HashSet<>();

    // New field: list of products purchased by the user
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
}
