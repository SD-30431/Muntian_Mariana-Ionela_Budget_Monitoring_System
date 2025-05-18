package com.example.demo.Model;

import com.example.demo.DTO.ProductXmlDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "products")
public class ProductsWrapper {

    private List<ProductXmlDTO> products;

    public ProductsWrapper() {}

    public ProductsWrapper(List<ProductXmlDTO> products) {
        this.products = products;
    }

    @XmlElement(name = "product")
    public List<ProductXmlDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductXmlDTO> products) {
        this.products = products;
    }
}
