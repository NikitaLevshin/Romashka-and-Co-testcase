package com.romashkaco.testcase.service;

import com.romashkaco.testcase.model.Product;
import com.romashkaco.testcase.storage.ProductStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductStorage productStorage;

    public Collection<Product> getAll() {
        return productStorage.getAll();
    }

    public Product getById(int id) {
        return productStorage.getById(id);
    }

    public Product create(Product product) {
        return productStorage.create(product);
    }

    public Product upgrade(int id, Product product) {
        return productStorage.upgrade(id, product);
    }

    public void delete(int id) {
        productStorage.delete(id);
    }
}
