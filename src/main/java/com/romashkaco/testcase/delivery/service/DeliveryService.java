package com.romashkaco.testcase.delivery.service;

import com.romashkaco.testcase.delivery.dto.DeliveryDto;

import java.util.List;

public interface DeliveryService {

    List<DeliveryDto> getAll();
    DeliveryDto getById(int id);
    DeliveryDto create(DeliveryDto deliveryDto);
    DeliveryDto upgrade(int id, DeliveryDto deliveryDto);
    void delete(int id);
}
