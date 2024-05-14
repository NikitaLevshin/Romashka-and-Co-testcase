package com.romashkaco.testcase.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private int id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    private double price;
    private Boolean isOnStock;
    private int amount;
}
