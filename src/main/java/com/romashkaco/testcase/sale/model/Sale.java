package com.romashkaco.testcase.sale.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Название товара не может быть пустым")
    @Size(max = 255, message = "Название товара не должно быть длиннее {max} символов")
    @JoinColumn(name = "name")
    private String name;
    @Min(value = 1, message = "Количество проданного товара должно быть больше 1")
    private int amount;
    private double sum;

    public Sale(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Sale(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
