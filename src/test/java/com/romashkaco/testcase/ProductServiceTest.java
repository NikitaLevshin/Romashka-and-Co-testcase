package com.romashkaco.testcase;

import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.dto.ProductDto;
import com.romashkaco.testcase.product.mapper.ProductMapper;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.model.ProductSort;
import com.romashkaco.testcase.product.repository.ProductRepository;
import com.romashkaco.testcase.product.service.ProductService;
import com.romashkaco.testcase.product.service.ProductServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        badScrewdriver = new Product("Отвёртка", "Плохая отвертка", 15.15, false);
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
        assertEquals(testProduct.getPrice(), receivedTest.getPrice());

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

    @Test
    void filterByNotFullNameTest() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(goodScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        String filter = "лоток";
        List<ProductDto> testList = productService.getAll(filter, null, null, null,
                null, 100);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(savedProduct));
        assertTrue(testList.contains(savedProduct2));
        assertFalse(testList.contains(savedProduct1));
    }

    @Test
    void filterByPriceMoreTest() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        Double price = 15.1;
        List<ProductDto> testList = productService.getAll(null, price, null, null,
                null, 100);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(savedProduct1));
        assertTrue(testList.contains(savedProduct2));
        assertFalse(testList.contains(savedProduct));
    }

    @Test
    void filterByPriceLessTest() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        Double price = 15.1;
        List<ProductDto> testList = productService.getAll(null, null, price, null,
                null, 100);
        assertEquals(1, testList.size());
        assertTrue(testList.contains(savedProduct));
        assertFalse(testList.contains(savedProduct2));
        assertFalse(testList.contains(savedProduct1));
    }

    @Test
    void filterByPriceMoreAndLessTest () {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        Double priceMore = 11.2;
        Double priceLess = 16.1;
        List<ProductDto> testList = productService.getAll(null, priceMore, priceLess, null,
                null, 100);
        assertEquals(1, testList.size());
        assertTrue(testList.contains(savedProduct1));
        assertFalse(testList.contains(savedProduct));
        assertFalse(testList.contains(savedProduct2));
    }

    @Test
    void filterByOnStockTrue() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        List<ProductDto> testList = productService.getAll(null, null, null, true,
                null, 100);
        assertEquals(2, testList.size());
        assertTrue(testList.contains(savedProduct));
        assertTrue(testList.contains(savedProduct2));
        assertFalse(testList.contains(savedProduct1));
    }

    @Test
    void filterByOnStockFalse() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        List<ProductDto> testList = productService.getAll(null, null, null, false,
                null, 100);
        assertEquals(1, testList.size());
        assertTrue(testList.contains(savedProduct1));
        assertFalse(testList.contains(savedProduct));
        assertFalse(testList.contains(savedProduct2));
    }

    @Test
    void sortingByPrice() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodHammer));
        List<ProductDto> testList = productService.getAll(null, null, null, null,
                ProductSort.PRICEASC, 100);
        assertEquals(savedProduct2, testList.get(0));
        assertEquals(savedProduct1, testList.get(1));
        assertEquals(savedProduct, testList.get(2));
    }

    @Test
    void sortingByName() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(goodHammer));
        List<ProductDto> testList = productService.getAll(null, null, null, null,
                ProductSort.NAMEASC, 100);
        assertEquals(savedProduct1, testList.get(0));
        assertEquals(savedProduct, testList.get(1));
    }

    @Test
    void sizeTest() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodHammer));
        int size = 1;
        List<ProductDto> testList = productService.getAll(null, null, null, null,
                ProductSort.NAMEASC, size);
        assertEquals(size, testList.size());
    }

    @Test
    void allFiltersAndSortingTest() {
        ProductDto savedProduct = ProductMapper.toProductDto(productRepository.save(goodHammer));
        ProductDto savedProduct1 = ProductMapper.toProductDto(productRepository.save(goodScrewdriver));
        ProductDto savedProduct2 = ProductMapper.toProductDto(productRepository.save(goodJackHammer));
        ProductDto savedProduct3 = ProductMapper.toProductDto(productRepository.save(badHammer));
        ProductDto savedProduct4 = ProductMapper.toProductDto(productRepository.save(badScrewdriver));
        ProductDto savedProduct5 = ProductMapper.toProductDto(productRepository.save(badJackHammer));
        String text = "лоток";
        Double priceMore = 6.0;
        Double priceLess = 55.0;
        Boolean isOnStock = true;
        List<ProductDto> testList = productService.getAll(text, priceMore, priceLess, isOnStock,
                ProductSort.NAMEDESC, 100);
        assertEquals(2, testList.size());
        assertEquals(savedProduct2, testList.get(0));
        assertEquals(savedProduct, testList.get(1));
    }

    @Test
    void shouldThrowAnExceptionWithWrongPriceFilterTest() {
        Double price = -5.0;
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> {
                    productService.getAll(null, price, null, null, null, 100);
                }
        );
        assertEquals("Цена не может быть отрицательной", e.getMessage());
    }

    @Test
    void shouldThrowAnExceptionWithWrongPriceMoreAndLessFilterTest() {
        Double priceMore = 40.0;
        Double priceLess = 15.0;
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> {
                    productService.getAll(null, priceMore, priceLess, null, null, 100);
                }
        );
        assertEquals("Высший порог цены не может быть дороже низшего", e.getMessage());
    }

    @Test
    void shouldThrowAnExceptionWithBlankNameFilter() {
        String name = " ";
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> {
                    productService.getAll(name, null, null, null, null, 100);
                }
        );
        assertEquals("Неверно задан фильтр названия", e.getMessage());
    }
}
