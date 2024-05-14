package com.romashkaco.testcase.sale.mapper;

import com.romashkaco.testcase.sale.dto.SaleFullDto;
import com.romashkaco.testcase.sale.dto.SaleNewDto;
import com.romashkaco.testcase.sale.model.Sale;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SaleMapper {
    public static Sale fromSaleNewDto(SaleNewDto saleDto) {
        return new Sale(saleDto.getId(),
                saleDto.getName(),
                saleDto.getAmount());
    }

    public static SaleFullDto toSaleFullDto(Sale sale) {
        return new SaleFullDto(sale.getId(),
                sale.getName(),
                sale.getAmount(),
                sale.getSum());
    }

    public static List<SaleFullDto> toSaleDtoList(List<Sale> sales) {
        return sales.stream()
                .map(SaleMapper::toSaleFullDto)
                .collect(Collectors.toList());
    }
}
