package com.romashkaco.testcase.product.service;

import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.mapper.ProductMapper;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAll() {
        log.info("Получаем список всех товаров");
        return productRepository.findAll().stream()
                .map(ProductMapper::toProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getById(int id) {
        log.info("Получаем товар с id {}", id);
        return ProductMapper.toProductDto(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар с этим id не найден")));
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        log.info("Создаем новый товар");
        Product product = ProductMapper.fromProductDto(productDto);
        return ProductMapper.toProductDto(productRepository.save(product));
    }

    @Override
    public ProductDto upgrade(int id, ProductDto productDto) {
        log.info("Обновляем товар с id {}", id);
        Product product = ProductMapper.fromProductDto(getById(id));
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != 0) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getIsOnStock() != null) {
            product.setOnStock((productDto.getIsOnStock()));
        }
        productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар с этим id не найден"));
        return ProductMapper.toProductDto(productRepository.save(product));
    }

    @Override
    public void delete(int id) {
        log.info("Удаляем товар с id {}", id);
        productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар с этим id не найден"));
        productRepository.deleteById(id);
    }
}
