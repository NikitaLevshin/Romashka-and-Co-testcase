package com.romashkaco.testcase.product.repository;

import com.romashkaco.testcase.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Integer> {
}
