package com.romashkaco.testcase.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Product {

    private int id;
    @NotBlank(message = "Название товара не может быть пустым")
    @Size(max = 255, message = "Название товара не должно быть длиннее {max} символов")
    private String name;
    @Size(max = 4096, message = "Описание товара не должно быть длиннее {max} символов")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @Min(value = 0, message = "Цена не может быть отрицательной")
    private Double price;
    private Boolean isOnStock;


    public Product(String name, String description, double price, boolean isOnStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.isOnStock = isOnStock;
    }

}
