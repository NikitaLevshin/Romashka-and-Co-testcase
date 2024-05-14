package com.romashkaco.testcase.sale.service;

import com.romashkaco.testcase.sale.dto.SaleFullDto;
import com.romashkaco.testcase.sale.dto.SaleNewDto;

import java.util.List;

public interface SaleService {
    List<SaleFullDto> getAll();

    SaleFullDto getById(int id);

    SaleFullDto create(SaleNewDto saleDto);

    SaleFullDto upgrade(int id, SaleNewDto saleDto);

    void delete(int id);
}
