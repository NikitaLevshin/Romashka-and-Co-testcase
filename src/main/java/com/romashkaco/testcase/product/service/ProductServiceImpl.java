package com.romashkaco.testcase.product.service;

import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.mapper.ProductMapper;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.model.ProductSort;
import com.romashkaco.testcase.product.repository.ProductRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAll(String name, Double priceMore, Double priceLess,
                                   Boolean isOnStock, ProductSort sort, Integer size) {
        log.info("Получаем список всех товаров");
        filterValidation(name, priceMore, priceLess);
        Pageable pageable;
        if (sort != null) {
            switch (sort) {
                case NAMEASC -> pageable = PageRequest.of(0, size, Sort.by("name"));
                case NAMEDESC -> pageable = PageRequest.of(0, size, Sort.by("name").descending());
                case PRICEASC -> pageable = PageRequest.of(0, size, Sort.by("price"));
                case PRICEDESC -> pageable = PageRequest.of(0, size, Sort.by("price").descending());
                default -> throw new ValidationException("Такого метода сортировки нет");
            }
        } else {
            pageable = PageRequest.of(0, size);
        }
        if (priceMore != null && priceLess != null) {
            return ProductMapper.toProductDtoList(
                    productRepository.getAllWithFiltersAndSortBothPrices(name, priceMore, priceLess, isOnStock, pageable));
        } else if (priceLess == null) {
            return ProductMapper.toProductDtoList(
                    productRepository.getAllWithFiltersAndSortPriceMore(name, priceMore, isOnStock, pageable));
        } else {
            return ProductMapper.toProductDtoList(
                    productRepository.getAllWithFiltersAndSortPriceLess(name, priceLess, isOnStock, pageable));
        }
    }

    @Override
    public ProductDto getById(int id) {
        log.info("Получаем товар с id {}", id);
        return ProductMapper.toProductDto(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Товар с этим id не найден")));
    }

    @Override
    @Transactional
    public ProductDto create(ProductDto productDto) {
        log.info("Создаем новый товар");
        Product product = ProductMapper.fromProductDto(productDto);
        product.setOnStock(stockAvailableCalculator(product));
        return ProductMapper.toProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
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
        product.setOnStock(stockAvailableCalculator
                (ProductMapper.fromProductDto(productDto)));
        productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар с этим id не найден"));
        return ProductMapper.toProductDto(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("Удаляем товар с id {}", id);
        productRepository.findById(id).orElseThrow(() -> new NotFoundException("Товар с этим id не найден"));
        productRepository.deleteById(id);
    }

    private void filterValidation(String name, Double priceMore, Double priceLess) {
        if (name != null && (name.isBlank() || name.isEmpty())) {
            throw new ValidationException("Неверно задан фильтр названия");
        }
        if (priceMore != null && priceLess != null) {
            if (priceMore > priceLess) throw new ValidationException("Высший порог цены не может быть дороже низшего");
        }
        if (priceMore != null && priceMore < 0) throw new ValidationException("Цена не может быть отрицательной");
        if (priceLess != null && priceLess < 0) throw new ValidationException("Цена не может быть отрицательной");
    }

    private Boolean stockAvailableCalculator(Product product) {
        if (!product.isOnStock() && product.getAmount() > 0) {
            return true;
        }
        if (product.isOnStock() && product.getAmount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
