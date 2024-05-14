package com.romashkaco.testcase.delivery.controller;

import com.romashkaco.testcase.delivery.dto.DeliveryDto;
import com.romashkaco.testcase.delivery.service.DeliveryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/product/delivery")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<DeliveryDto> getAll() {
        return deliveryService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto getById(@PathVariable(name = "id") int id) {
        return deliveryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryDto create(@Valid @RequestBody DeliveryDto deliveryDto) {
        return deliveryService.create(deliveryDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto upgrade(@PathVariable(name = "id") int id, @RequestBody DeliveryDto deliveryDto) {
        return deliveryService.upgrade(id, deliveryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        deliveryService.delete(id);
    }
}
