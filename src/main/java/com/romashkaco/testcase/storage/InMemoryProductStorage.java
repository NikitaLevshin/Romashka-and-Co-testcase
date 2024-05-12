package com.romashkaco.testcase.storage;

import com.romashkaco.testcase.exception.NotFoundException;
import com.romashkaco.testcase.model.Product;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryProductStorage implements ProductStorage {

    private final HashMap<Integer, Product> products = new HashMap<>();
    private int id = 1;

    @Override
    public Collection<Product> getAll() {
        log.info("Получаем список всех товаров");
        if (products.isEmpty()) {
            throw new NotFoundException("Товар не найден");
        } else {
            return products.values();
        }
    }

    @Override
    public Product getById(int id) {
        log.info("Получаем товар с id {}", id);
        if (!products.containsKey(id)) {
            throw new NotFoundException("Товар не найден");
        } else {
            return products.get(id);
        }
    }

    @Override
    public Product create(Product product) {
        log.info("Создаем новый товар");
        if (product.getPrice() == null) {
            product.setPrice(0.0);
        } if (product.getIsOnStock() == null) {
            product.setIsOnStock(false);
        }
        product.setId(id);
        id++;
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public Product upgrade(int id, Product product) {
        log.info("Обновляем товар с id {}", id);
        if (!products.containsKey(id)) {
            throw new NotFoundException("Товар не найден");
        } else {
        Product productOnUpdate = products.get(id);
        if (product.getName() != null) {
            productOnUpdate.setName(product.getName());
        } if (product.getDescription() != null) {
            productOnUpdate.setDescription(product.getDescription());
        } if (product.getPrice() != null) {
            productOnUpdate.setPrice(product.getPrice());
        } if (product.getIsOnStock() != null) {
            productOnUpdate.setIsOnStock(product.getIsOnStock());
        }
        validate(productOnUpdate);

        products.replace(id, productOnUpdate);
        return productOnUpdate;
        }
    }

    @Override
    public void delete(int id) {
        log.info("Удаляем товар с id {}", id);
        if (!products.containsKey(id)) {
            throw new NotFoundException("Товар не найден");
        } else {
            products.remove(id);
        }
    }

    private void validate(Product product) {
        if (product.getName() == null || product.getName().isEmpty() || product.getName().isBlank()) {
            throw new ValidationException("Название товара не может быть пустым");
        } else if (product.getName().length() > 255) {
            throw new ValidationException("Название товара не может быть длиннее 255 символов");
        } else if (product.getDescription().length() > 4096) {
            throw new ValidationException("Длина описания не может быть длиннее 4096 символов");
        } else if (product.getPrice() < 0) {
            throw new ValidationException("Цена не может быть отрицательной");
        }
    }
}
