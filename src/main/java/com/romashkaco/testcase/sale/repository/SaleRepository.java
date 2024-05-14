package com.romashkaco.testcase.sale.repository;

import com.romashkaco.testcase.sale.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
