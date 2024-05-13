package com.romashkaco.testcase;

import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.mapper.ProductMapper;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.repository.ProductRepository;
import com.romashkaco.testcase.product.service.ProductService;
import com.romashkaco.testcase.product.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureDataJpa
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;
    private ProductService productService;
    private Product goodHammer;
    private Product badHammer;
    private Product goodScrewdriver;
    private Product badScrewdriver;
    private Product goodJackHammer;
    private Product badJackHammer;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository);
        goodHammer = new Product("Молоток", "Хороший молоток", 10, true);
        badHammer = new Product("Молоток", "Плохой молоток", 5, false);
        goodScrewdriver = new Product("Отвёртка", "Хорошая отвертка", 20, true);
        badScrewdriver = new Product("Отвёртка", "Плохая отвертка", 15, false);
        goodJackHammer = new Product("Отбойный молоток", "Хороший отбойный молоток", 50, true);
        badJackHammer = new Product("Отбойный молоток", "Плохой отбойный молоток", 40, false);
    }

    @Test
    void saveTest() {
        Product testProduct = productRepository.save(goodHammer);
        assertEquals(1, testProduct.getId());
        assertEquals(goodHammer.getName(), testProduct.getName());
    }

    @Test
    void getAllTest() {
        Product testProduct = productRepository.save(goodHammer);
        productRepository.save(badHammer);
        productRepository.save(goodScrewdriver);
        List<Product> testProducts = productRepository.findAll();
        assertEquals(3, testProducts.size());
        assertTrue(testProducts.contains(testProduct));
    }

    @Test
    void getByIdTest() {
        Product testProduct = productRepository.save(goodHammer);
        Product receivedTest = productRepository.getReferenceById(testProduct.getId());
        assertEquals(testProduct.getId(), receivedTest.getId());
        assertEquals(testProduct.getName(), receivedTest.getName());
        assertEquals(testProduct.getPrice(),receivedTest.getPrice());

    }

    @Test
    void shouldThrowAnExceptionWithWrongId() {
        final NotFoundException e = assertThrows(
            NotFoundException.class,
                () -> {
                    productService.getById(3);
                }
        );
        assertEquals("Товар с этим id не найден", e.getMessage());
    }

    @Test
    void upgradeTest() {
        ProductDto oldProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto upgradedProduct = productService.upgrade(oldProduct.getId(), ProductMapper.toProductDto(badHammer));
        assertEquals(oldProduct.getId(), upgradedProduct.getId());
        assertEquals(badHammer.getName(), upgradedProduct.getName());
        assertEquals(badHammer.isOnStock(), upgradedProduct.getIsOnStock());
    }

    @Test
    void deleteTest() {
        Product testProduct = productRepository.save(goodHammer);
        assertEquals(goodHammer.getName(), testProduct.getName());
        productService.delete(testProduct.getId());
        final NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> {
                    productService.getById(testProduct.getId());
                }
        );
        assertEquals("Товар с этим id не найден", e.getMessage());

    }
}
