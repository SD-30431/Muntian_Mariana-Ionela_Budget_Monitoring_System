package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity
@XmlRootElement(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonManagedReference
    @XmlTransient
    private List<Product> products = new ArrayList<>();

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

    // Getters and setters
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }
}
