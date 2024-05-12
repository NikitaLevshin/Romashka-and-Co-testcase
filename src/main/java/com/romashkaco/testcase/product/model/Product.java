package com.romashkaco.testcase.product.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private double price;
    @Column(name = "on_stock")
    private boolean isOnStock;


    public Product(String name, String description, double price, boolean isOnStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isOnStock = isOnStock;
    }

}
