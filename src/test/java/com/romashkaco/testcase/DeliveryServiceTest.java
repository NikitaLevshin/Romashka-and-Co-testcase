package com.romashkaco.testcase;

import com.romashkaco.testcase.delivery.dto.DeliveryDto;
import com.romashkaco.testcase.delivery.mapper.DeliveryMapper;
import com.romashkaco.testcase.delivery.model.Delivery;
import com.romashkaco.testcase.delivery.repository.DeliveryRepository;
import com.romashkaco.testcase.delivery.service.DeliveryService;
import com.romashkaco.testcase.delivery.service.DeliveryServiceImpl;
import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.repository.ProductRepository;
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
public class DeliveryServiceTest {


    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;
    private DeliveryService deliveryService;
    private Delivery firstDelivery;
    private Delivery secondDelivery;
    private Product goodHammer;
    private Product badHammer;

    @Autowired
    public DeliveryServiceTest(DeliveryRepository deliveryRepository, ProductRepository productRepository) {
        this.deliveryRepository = deliveryRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        deliveryService = new DeliveryServiceImpl(deliveryRepository, productRepository);
        goodHammer = new Product("Молоток модель 1", "Хороший молоток", 10, true, 1);
        badHammer = new Product("Молоток модель 2", "Плохой молоток", 5, false, 0);
        firstDelivery = new Delivery("Молоток модель 1", 5);
        secondDelivery = new Delivery("Молоток модель 2", 10);
    }

    @Test
    void getAllDeliveriesTest() {
        DeliveryDto deliveryDto = DeliveryMapper.toDeliveryDto(deliveryRepository.save(firstDelivery));
        deliveryRepository.save(secondDelivery);
        List<DeliveryDto> testList = deliveryService.getAll();
        assertEquals(2, testList.size());
        assertTrue(testList.contains(deliveryDto));
    }

    @Test
    void getDeliveryByIdTest() {
        DeliveryDto deliveryDto = DeliveryMapper.toDeliveryDto(deliveryRepository.save(firstDelivery));
        DeliveryDto deliveryDto1 = deliveryService.getById(deliveryDto.getId());
        assertEquals(deliveryDto.getId(), deliveryDto1.getId());
        assertEquals(deliveryDto.getName(), deliveryDto1.getName());
        assertEquals(deliveryDto.getAmount(), deliveryDto1.getAmount());
    }

    @Test
    void createDeliveryTestWithAmountChangingInProduct() {
        int expectedAmount = goodHammer.getAmount() + firstDelivery.getAmount();
        Product hammer = productRepository.save(goodHammer);
        DeliveryDto deliveryDto = deliveryService.create(DeliveryMapper.toDeliveryDto(firstDelivery));
        assertEquals(expectedAmount, hammer.getAmount());
    }

    @Test
    void shouldThrowAnExceptionWhileCreatingDeliveryWithNoSuchProduct() {
        final NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> {
                    deliveryService.create(DeliveryMapper.toDeliveryDto(firstDelivery));
                }
        );
        assertEquals("Такого товара не существует", e.getMessage());
    }

    @Test
    void upgradeDeliveryShouldChangeAmountOfProducts() {
        int firstAmount = goodHammer.getAmount();
        int intermediate = goodHammer.getAmount() + firstDelivery.getAmount();
        int expected = badHammer.getAmount() + secondDelivery.getAmount();

        productRepository.save(goodHammer);
        productRepository.save(badHammer);

        DeliveryDto deliveryDto = deliveryService.create(DeliveryMapper.toDeliveryDto(firstDelivery));

        assertEquals(intermediate, goodHammer.getAmount());

        deliveryService.upgrade(deliveryDto.getId(), DeliveryMapper.toDeliveryDto(secondDelivery));
        DeliveryDto upgradedDelivery = deliveryService.getById(deliveryDto.getId());

        assertEquals(upgradedDelivery.getName(), secondDelivery.getName());
        assertEquals(firstAmount, goodHammer.getAmount());
        assertEquals(expected, badHammer.getAmount());
    }

    @Test
    void deleteTestShouldChangeAmountOfProducts() {
        int firstAmount = goodHammer.getAmount();
        int intermediate = goodHammer.getAmount() + firstDelivery.getAmount();

        productRepository.save(goodHammer);
        DeliveryDto deliveryDto = deliveryService.create(DeliveryMapper.toDeliveryDto(firstDelivery));
        assertEquals(intermediate, goodHammer.getAmount());

        deliveryService.delete(deliveryDto.getId());
        assertEquals(firstAmount, goodHammer.getAmount());

        final NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> {
                    deliveryService.getById(deliveryDto.getId());
                }
        );
        assertEquals("Поставки с таким номером не существует", e.getMessage());
    }

    @Test
    void cantDeleteDeliveryWhenProductIsAlreadySold() {
        Product hammer = productRepository.save(goodHammer);
        DeliveryDto deliveryDto = deliveryService.create(DeliveryMapper.toDeliveryDto(firstDelivery));
        hammer.setAmount(2);
        productRepository.save(hammer);

        final ValidationException e = assertThrows(
                ValidationException.class,
                () -> {
                    deliveryService.delete(deliveryDto.getId());
                }
        );
        assertEquals("Такого количества товара нет в наличии", e.getMessage());
    }
}
