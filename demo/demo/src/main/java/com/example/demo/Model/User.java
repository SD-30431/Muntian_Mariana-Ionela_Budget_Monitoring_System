package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    // Store the hashed password
    private String passwordHash;

    private Double salary;

    // Fetch budgets eagerly to avoid lazy initialization issues
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "user-userBudget")
    private Set<UserBudget> userBudgets = new HashSet<>();

    public User() {}

    public User(String username, String passwordHash, Double salary) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.salary = salary;
    }

    // Getters & Setters
    public Long getId() { return id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Double getSalary() { return salary; }

    public void setSalary(Double salary) { this.salary = salary; }

    public Set<UserBudget> getUserBudgets() { return userBudgets; }

    public void setUserBudgets(Set<UserBudget> userBudgets) { this.userBudgets = userBudgets; }
}
