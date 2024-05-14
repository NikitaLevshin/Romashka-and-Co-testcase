package com.romashkaco.testcase.delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {

    private int id;
    private String name;
    private int amount;
}
