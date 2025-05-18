package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "budget")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    @PositiveOrZero(message = "Amount must be zero or positive")
    private Double amount;

    @NotBlank(message = "Card number is required")
    @Size(min = 1, max = 20, message = "Card number must be between 1 and 20 characters")
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
