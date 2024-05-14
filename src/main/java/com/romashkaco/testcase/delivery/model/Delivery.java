package com.romashkaco.testcase.delivery.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Название товара не может быть пустым")
    @Size(max = 255, message = "Название товара не должно быть длиннее {max} символов")
    @JoinColumn(name = "name")
    private String name;

    @Min(value = 1, message = "Количество товара должно быть больше 0")
    private int amount;

    public Delivery(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
}
