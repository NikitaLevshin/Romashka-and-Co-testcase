package com.romashkaco.testcase.delivery.service;

import com.romashkaco.testcase.delivery.dto.DeliveryDto;
import com.romashkaco.testcase.delivery.mapper.DeliveryMapper;
import com.romashkaco.testcase.delivery.model.Delivery;
import com.romashkaco.testcase.delivery.repository.DeliveryRepository;
import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.repository.ProductRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;

    @Override
    public List<DeliveryDto> getAll() {
        log.info("Получаем список всех поставок");
        return DeliveryMapper.toDeliveryDtoList(deliveryRepository.findAll());
    }

    @Override
    public DeliveryDto getById(int id) {
        log.info("Получаем поставку по id документа {}", id);
        return DeliveryMapper.toDeliveryDto(
                deliveryRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Поставки с таким номером не существует")));
    }

    @Override
    @Transactional
    public DeliveryDto create(DeliveryDto deliveryDto) {
        log.info("Создаем поставку товара");
        upgradeProductAmount(deliveryDto.getName(), deliveryDto.getAmount()); //при создании поставки увеличиваем количество товара на складе
        Delivery delivery = DeliveryMapper.fromDeliveryDto(deliveryDto);
        return DeliveryMapper.toDeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    @Transactional
    public DeliveryDto upgrade(int id, DeliveryDto deliveryDto) {
        log.info("Обновляем поставку с id {}", id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Поставки с таким номером не существует"));
        if (!delivery.getName().equals(deliveryDto.getName())) {  //если поменялось имя товара, уменьшаем количество товара на складе старого имени
            reduceProductAmount(delivery.getName(), delivery.getAmount());
            upgradeProductAmount(deliveryDto.getName(), deliveryDto.getAmount()); //и увеличиваем количество с новым именем
            delivery.setName(deliveryDto.getName());
        } else {
            reduceProductAmount(delivery.getName(), delivery.getAmount());
            upgradeProductAmount(delivery.getName(), deliveryDto.getAmount());
            delivery.setAmount(deliveryDto.getAmount());
        }
        return DeliveryMapper.toDeliveryDto(deliveryRepository.save(delivery));

    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("Удаляем поставку с id {}", id);
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Поставки с таким номером не существует"));
        reduceProductAmount(delivery.getName(), delivery.getAmount());
        deliveryRepository.deleteById(id);
    }

    private void upgradeProductAmount(String name, int amount) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Такого товара не существует"));
        product.setAmount(product.getAmount() + amount);
        if (!product.isOnStock()) product.setOnStock(true);
        productRepository.save(product);
    }

    private void reduceProductAmount(String name, int amount) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Такого товара не существует"));
        product.setAmount(product.getAmount() - amount);
        if (product.getAmount() == 0) product.setOnStock(false);
        if (product.getAmount() < 0) throw new ValidationException("Такого количества товара нет в наличии");
    }
}
