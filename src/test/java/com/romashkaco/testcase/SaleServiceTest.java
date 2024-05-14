package com.romashkaco.testcase;

import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.repository.ProductRepository;
import com.romashkaco.testcase.sale.dto.SaleFullDto;
import com.romashkaco.testcase.sale.mapper.SaleMapper;
import com.romashkaco.testcase.sale.model.Sale;
import com.romashkaco.testcase.sale.repository.SaleRepository;
import com.romashkaco.testcase.sale.service.SaleService;
import com.romashkaco.testcase.sale.service.SaleServiceImpl;
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
public class SaleServiceTest {
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private SaleService saleService;
    private Sale firstSale;
    private Sale secondSale;
    private Product goodHammer;
    private Product badHammer;

    @Autowired
    public SaleServiceTest(SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        saleService = new SaleServiceImpl(saleRepository, productRepository);
        goodHammer = new Product("Молоток модель 1", "Хороший молоток", 10, true, 10);
        badHammer = new Product("Молоток модель 2", "Плохой молоток", 5, false, 5);
        firstSale = new Sale("Молоток модель 1", 10);
        secondSale = new Sale("Молоток модель 2", 5);
    }

    @Test
    void createSaleShouldReduceProductAmountAndCalculateSum() {
        productRepository.save(goodHammer);
        int expectedAmount = goodHammer.getAmount() - firstSale.getAmount();
        double expectedSum = goodHammer.getPrice() * firstSale.getAmount();
        SaleFullDto saleFullDto = saleService.create(SaleMapper.toSaleFullDto(firstSale));
        assertEquals(expectedSum, saleFullDto.getSum());
        assertEquals(expectedAmount, goodHammer.getAmount());
    }

    @Test
    void shouldThrowAnExceptionWhenProductDoesntExists() {
        final NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> {
                    saleService.create(SaleMapper.toSaleFullDto(firstSale));
                }
        );
        assertEquals("Такого товара не существует", e.getMessage());
    }

    @Test
    void shouldThrowAnExceptionWhenTryingToSellMoreThanOnStock() {
        productRepository.save(goodHammer);
        firstSale.setAmount(50);
        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> {
                    saleService.create(SaleMapper.toSaleFullDto(firstSale));
                }
        );
        assertEquals("Такого количества товара нет в наличии", e.getMessage());
    }

    @Test
    void getAllSalesTest() {
        productRepository.save(goodHammer);
        productRepository.save(badHammer);
        SaleFullDto saleFullDto = saleService.create(SaleMapper.toSaleFullDto(firstSale));
        saleService.create(SaleMapper.toSaleFullDto(secondSale));
        List<SaleFullDto> testList = saleService.getAll();
        assertEquals(2, testList.size());
        assertTrue(testList.contains(saleFullDto));
    }

    @Test
    void getSaleById() {
        productRepository.save(goodHammer);
        SaleFullDto saleFullDto = saleService.create(SaleMapper.toSaleFullDto(firstSale));
        SaleFullDto saleFullDto1 = saleService.getById(saleFullDto.getId());
        assertEquals(saleFullDto.getId(), saleFullDto1.getId());
        assertEquals(saleFullDto.getName(), saleFullDto1.getName());
        assertEquals(saleFullDto.getAmount(), saleFullDto1.getAmount());
        assertEquals(saleFullDto.getSum(), saleFullDto1.getSum());
    }

    @Test
    void updateSaleTestShouldReturnAmountOfOldProductAndRemoveAmountFromNewProduct() {
        int goodHammerFirstAmount = goodHammer.getAmount();
        int goodHammerIntermediate = goodHammer.getAmount() - firstSale.getAmount();
        int badHammerExpectedAmount = badHammer.getAmount() - secondSale.getAmount();
        productRepository.save(goodHammer);
        productRepository.save(badHammer);
        SaleFullDto saleFullDto = saleService.create(SaleMapper.toSaleFullDto(firstSale));

        assertEquals(goodHammerIntermediate, goodHammer.getAmount());

        SaleFullDto upgradedSale = saleService.upgrade(saleFullDto.getId(), SaleMapper.toSaleFullDto(secondSale));

        assertEquals(saleFullDto.getId(), upgradedSale.getId());
        assertEquals(goodHammerFirstAmount, goodHammer.getAmount());
        assertEquals(badHammerExpectedAmount, badHammer.getAmount());
    }

    @Test
    void deleteTestShouldReturnAmountOfProduct() {
        int goodHammerFirstAmount = goodHammer.getAmount();
        int goodHammerIntermediate = goodHammer.getAmount() - firstSale.getAmount();
        productRepository.save(goodHammer);

        SaleFullDto saleFullDto = saleService.create(SaleMapper.toSaleFullDto(firstSale));
        assertEquals(goodHammerIntermediate, goodHammer.getAmount());

        saleService.delete(saleFullDto.getId());

        assertEquals(goodHammerFirstAmount, goodHammer.getAmount());

        final NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> {
                    saleService.getById(saleFullDto.getId());
                }
        );
        assertEquals("Документа о продаже с таким id нет", e.getMessage());
    }
}
