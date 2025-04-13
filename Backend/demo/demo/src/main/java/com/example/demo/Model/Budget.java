package com.example.demo.Model;

import com.example.demo.Model.UserBudget;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    // Getters and setters
    public Long getId() { return id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getCardnumber() { return cardnumber; }
    public void setCardnumber(String cardnumber) { this.cardnumber = cardnumber; }
    public Set<UserBudget> getUserBudgets() { return userBudgets; }
    public void setUserBudgets(Set<UserBudget> userBudgets) { this.userBudgets = userBudgets; }
}
