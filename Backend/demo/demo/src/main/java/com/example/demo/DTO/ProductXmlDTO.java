package com.example.demo.DTO;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

@XmlRootElement(name = "product")
public class ProductXmlDTO {

    private String name;
    private Double price;
    private LocalDate date;
    private String categoryName;

    public ProductXmlDTO() {}

    public ProductXmlDTO(String name, Double price, LocalDate date, String categoryName) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.categoryName = categoryName;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public Double getPrice() {
        return price;
    }

    @XmlElement
    public LocalDate getDate() {
        return date;
    }

    @XmlElement
    public String getCategoryName() {
        return categoryName;
    }
}
