package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "user_budget")
public class UserBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-one relationship to user
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-userBudget") // Ensure the User class has matching @JsonManagedReference if needed.
    private User user;

    // Many-to-one relationship to budget
    @ManyToOne
    @JoinColumn(name = "budget_id")
    @JsonBackReference(value = "budget-userBudget")
    private Budget budget;

    public UserBudget() {}

    public UserBudget(User user, Budget budget) {
        this.user = user;
        this.budget = budget;
    }

    // Getters & Setters

    public void setUser(User user) {
        this.user = user;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
