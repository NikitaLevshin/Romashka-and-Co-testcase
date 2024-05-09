package com.romashkaco.testcase;

import com.romashkaco.testcase.model.Product;
import com.romashkaco.testcase.storage.InMemoryProductStorage;
import com.romashkaco.testcase.storage.ProductStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryProductStorageTest {
    private final ProductStorage productStorage = new InMemoryProductStorage();
    Product product;
    Product product1;

    @BeforeEach
    public void beforeEach() {
        product = new Product("Молоток", "Хороший молоток", 20.25, true);
        product1 = new Product("Отвертка", "Хорошая отвертка", 199, false);
    }

    @Test
    public void createProductAndGiveIdTest() {
        productStorage.create(product);
        productStorage.create(product1);

        assertEquals(1,product.getId());
        assertEquals(2, product1.getId());
    }

    @Test
    public void findAllProductsTest() {
        productStorage.create(product);
        productStorage.create(product1);
        List<Product> testProducts = List.of(product, product1);
        assertEquals(testProducts.size(), productStorage.getAll().size());
    }

    @Test
    public void findProductByIdTest() {
        productStorage.create(product);
        assertEquals(product.getName(), productStorage.getById(product.getId()).getName());
    }

    @Test
    public void upgradeProductTest() {
        productStorage.create(product);
        Product upgradedProduct = new Product("Молоток 2.0", "Улучшенный молоток",
                199.97, true);
        Product upgradedProduct1 = productStorage.upgrade(product.getId(), upgradedProduct);
        assertEquals(upgradedProduct.getName(), upgradedProduct1.getName());
        assertEquals(upgradedProduct.getDescription(), upgradedProduct1.getDescription());
        assertEquals(upgradedProduct.getPrice(), upgradedProduct1.getPrice());
        assertEquals(upgradedProduct.isOnStock(), upgradedProduct1.isOnStock());
    }

    @Test
    public void deleteProductTest() {
        productStorage.create(product);
        productStorage.create(product1);
        assertEquals(2, productStorage.getAll().size());
        productStorage.delete(product.getId());
        assertEquals(1, productStorage.getAll().size());
    }
}
