package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Column(unique = true)
    private String cardnumber;

    @OneToMany(mappedBy = "budget", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "budget-userBudget")
    private Set<UserBudget> userBudgets = new HashSet<>();

    public Budget() {}

    public Budget(Double amount, String cardnumber) {
        this.amount = amount;
        this.cardnumber = cardnumber;
    }

    // Getters & Setters

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public void setUserBudgets(Set<UserBudget> userBudgets) {
        this.userBudgets = userBudgets;
    }
}
