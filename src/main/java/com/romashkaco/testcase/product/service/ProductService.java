package com.romashkaco.testcase.product.service;

import com.romashkaco.testcase.product.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAll();

    ProductDto getById(int id);

    ProductDto create(ProductDto productDto);

    ProductDto upgrade(int id, ProductDto productDto);

    void delete(int id);

}
