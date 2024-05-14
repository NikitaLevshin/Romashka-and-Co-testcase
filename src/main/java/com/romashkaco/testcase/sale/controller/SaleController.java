package com.romashkaco.testcase.sale.controller;

import com.romashkaco.testcase.sale.dto.SaleFullDto;
import com.romashkaco.testcase.sale.dto.SaleNewDto;
import com.romashkaco.testcase.sale.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/product/sale")
@RequiredArgsConstructor
public class SaleController {
    private final SaleService saleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<SaleFullDto> getAll() {
        return saleService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SaleFullDto getById(@PathVariable(name = "id") int id) {
        return saleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleFullDto create(@Valid @RequestBody SaleNewDto saleDto) {
        return saleService.create(saleDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SaleFullDto upgrade(@PathVariable(name = "id") int id, @RequestBody SaleNewDto saleDto) {
        return saleService.upgrade(id, saleDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") int id) {
        saleService.delete(id);
    }

}
