package com.romashkaco.testcase.delivery.mapper;

import com.romashkaco.testcase.delivery.dto.DeliveryDto;
import com.romashkaco.testcase.delivery.model.Delivery;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DeliveryMapper {

    public static Delivery fromDeliveryDto(DeliveryDto deliveryDto) {
        return new Delivery(deliveryDto.getId(),
                deliveryDto.getName(),
                deliveryDto.getAmount());
    }

    public static DeliveryDto toDeliveryDto(Delivery delivery) {
        return new DeliveryDto(delivery.getId(),
                delivery.getName(),
                delivery.getAmount());
    }

    public static List<DeliveryDto> toDeliveryDtoList(List<Delivery> deliveries) {
        return deliveries.stream()
                .map(DeliveryMapper::toDeliveryDto)
                .collect(Collectors.toList());
    }
}
