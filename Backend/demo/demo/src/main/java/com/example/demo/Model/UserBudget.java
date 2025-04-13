package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;

@Entity
@Table(name = "user_budget")
public class UserBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-userBudget")
    private com.example.demo.Model.User user;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    @JsonBackReference(value = "budget-userBudget")
    private Budget budget;

    public UserBudget() {}

    public UserBudget(User user, Budget budget) {
        this.user = user;
        this.budget = budget;
    }

    // Getters and setters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }
}
