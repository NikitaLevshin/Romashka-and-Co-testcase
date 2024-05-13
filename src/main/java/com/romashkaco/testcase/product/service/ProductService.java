package com.romashkaco.testcase.product.service;

import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.model.ProductSort;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAll(String name, Double priceMore, Double priceLess,
                            Boolean onlyAvailable, ProductSort sort, Integer size);

    ProductDto getById(int id);

    ProductDto create(ProductDto productDto);

    ProductDto upgrade(int id, ProductDto productDto);

    void delete(int id);

}
