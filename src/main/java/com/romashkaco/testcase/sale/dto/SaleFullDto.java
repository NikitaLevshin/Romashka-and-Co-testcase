package com.romashkaco.testcase.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaleFullDto extends SaleNewDto {
    private double sum;

    public SaleFullDto(int id, String name, int amount, double sum) {
        super(id, name, amount);
        this.sum = sum;
    }
}
