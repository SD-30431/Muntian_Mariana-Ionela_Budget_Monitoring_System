package com.example.demo.Model;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() {}

    public Product(String name, Double price, LocalDate date, Category category) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.category = category;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
