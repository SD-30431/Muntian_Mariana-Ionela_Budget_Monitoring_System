package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Entity
@XmlRootElement(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Category must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    @JsonBackReference // Prevents infinite loop with Category if needed
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-product") // ✅ Matches User's @JsonManagedReference
    private User user;

    public Product() {}

    public Product(String name, Double price, LocalDate date, Category category, User user) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.category = category;
        this.user = user;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
