package com.romashkaco.testcase.sale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleNewDto {

    private int id;

    private String name;

    private int amount;
}
