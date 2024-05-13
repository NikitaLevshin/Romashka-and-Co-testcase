package com.romashkaco.testcase.product.mapper;

import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.model.Product;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductMapper {

    public static Product fromProductDto(ProductDto productDto) {
        return new Product(productDto.getId(),
                productDto.getName(),
                productDto.getDescription(),
                productDto.getPrice(),
                productDto.getIsOnStock() != null && productDto.getIsOnStock()
        );
    }

    public static ProductDto toProductDto(Product product) {
        return new ProductDto(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isOnStock());
    }

    public static List<ProductDto> toProductDtoList(List<Product> productList) {
        return productList.stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }
}
