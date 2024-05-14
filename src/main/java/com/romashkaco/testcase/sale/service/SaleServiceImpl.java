package com.romashkaco.testcase.sale.service;

import com.romashkaco.testcase.exceptions.NotFoundException;
import com.romashkaco.testcase.product.model.Product;
import com.romashkaco.testcase.product.repository.ProductRepository;
import com.romashkaco.testcase.sale.dto.SaleFullDto;
import com.romashkaco.testcase.sale.dto.SaleNewDto;
import com.romashkaco.testcase.sale.mapper.SaleMapper;
import com.romashkaco.testcase.sale.model.Sale;
import com.romashkaco.testcase.sale.repository.SaleRepository;
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
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;

    @Override
    public List<SaleFullDto> getAll() {
        log.info("Получаем список всех продаж");
        return SaleMapper.toSaleDtoList(saleRepository.findAll());
    }

    @Override
    public SaleFullDto getById(int id) {
        log.info("Получаем продажу с id документа {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Документа о продаже с таким id нет"));
        return SaleMapper.toSaleFullDto(sale);
    }

    @Override
    @Transactional
    public SaleFullDto create(SaleNewDto saleNewDto) {
        log.info("Создаем продажу товара");
        Sale sale = SaleMapper.fromSaleNewDto(saleNewDto);
        sale.setSum(sumOfSaleCalculator(sale)); //при продаже автоматически высчитываем сумму
        reduceProductAmount(sale.getName(), sale.getAmount()); //и уменьшаем количество товара на складе
        return SaleMapper.toSaleFullDto(saleRepository.save(sale));
    }

    @Override
    @Transactional
    public SaleFullDto upgrade(int id, SaleNewDto saleNewDto) {
        log.info("Обновляем документ о продаже с id {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Документа о продаже с таким id нет"));
        if (!sale.getName().equals(saleNewDto.getName())) { //при смене имени в продаже
            upgradeProductAmount(sale.getName(), sale.getAmount()); //увеличиваем количество товара, который не продали
            reduceProductAmount(saleNewDto.getName(), saleNewDto.getAmount()); //уменьшаем количество нужного товара
            sale.setName(saleNewDto.getName());
        } else {
            upgradeProductAmount(sale.getName(), sale.getAmount());
            reduceProductAmount(saleNewDto.getName(), saleNewDto.getAmount());
            sale.setAmount(saleNewDto.getAmount());
            sale.setSum(sumOfSaleCalculator(SaleMapper.fromSaleNewDto(saleNewDto)));
        }
        return SaleMapper.toSaleFullDto(saleRepository.save(sale));
    }

    @Override
    @Transactional
    public void delete(int id) {
        log.info("Удаляем документ о продаже с id {}", id);
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Документа о продаже с таким id нет"));
        upgradeProductAmount(sale.getName(), sale.getAmount());
        saleRepository.delete(sale);
    }

    private double sumOfSaleCalculator(Sale sale) {
        Product product = productRepository.findByName(sale.getName())
                .orElseThrow(() -> new NotFoundException("Такого товара не существует"));
        return sale.getAmount() * product.getPrice();
    }

    private void reduceProductAmount(String name, int amount) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Такого товара не существует"));
        product.setAmount(product.getAmount() - amount);
        if (product.getAmount() == 0) product.setOnStock(false);
        if (product.getAmount() < 0) throw new ValidationException("Такого количества товара нет в наличии");
    }

    private void upgradeProductAmount(String name, int amount) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Такого товара не существует"));
        product.setAmount(product.getAmount() + amount);
        if (!product.isOnStock()) product.setOnStock(true);
        productRepository.save(product);
    }


}
