package com.romashkaco.testcase.storage;

import com.romashkaco.testcase.model.Product;

import java.util.Collection;

public interface ProductStorage {
    Collection<Product> getAll();

    Product getById(int id);

    Product create(Product product);

    Product upgrade(int id, Product product);

    void delete(int id);

}
