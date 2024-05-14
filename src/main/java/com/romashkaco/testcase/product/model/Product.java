package com.romashkaco.testcase.product.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Название товара не может быть пустым")
    @Size(max = 255, message = "Название товара не должно быть длиннее {max} символов")
    private String name;
    @Size(max = 4096, message = "Описание товара не должно быть длиннее {max} символов")
    private String description;
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private double price;
    @Column(name = "on_stock")
    private boolean isOnStock;
    @Min(value = 0, message = "Количество товара не может быть отрицательным")
    private int amount;


    public Product(String name, String description, double price, boolean isOnStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isOnStock = isOnStock;
    }

    public Product(String name, String description, double price, boolean isOnStock, Integer amount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isOnStock = isOnStock;
        this.amount = amount;
    }
}
