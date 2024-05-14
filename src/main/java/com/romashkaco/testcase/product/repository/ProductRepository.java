package com.romashkaco.testcase.product.repository;

import com.romashkaco.testcase.product.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE ((lower(p.name) LIKE concat('%',lower(:name),'%') OR :name IS NULL)) " +
            "AND (p.isOnStock IN (:onlyAvailable) OR :onlyAvailable IS NULL) AND (p.price >= (:price) OR :price IS NULL)")
    List<Product> getAllWithFiltersAndSortPriceMore(String name, Double price, Boolean onlyAvailable, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE ((lower(p.name) LIKE concat('%',lower(:name),'%') OR :name IS NULL)) " +
            "AND (p.isOnStock IN (:onlyAvailable) OR :onlyAvailable IS NULL) AND (p.price <= (:price) OR :price IS NULL)")
    List<Product> getAllWithFiltersAndSortPriceLess(String name, Double price, Boolean onlyAvailable, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE ((lower(p.name) LIKE concat('%',lower(:name),'%') OR :name IS NULL)) " +
            "AND (p.isOnStock IN (:onlyAvailable) OR :onlyAvailable IS NULL) " +
            "AND (p.price <= (:priceLess) AND p.price >= (:priceMore))")
    List<Product> getAllWithFiltersAndSortBothPrices(String name, Double priceMore, Double priceLess,
                                                     Boolean onlyAvailable, Pageable pageable);

    Optional<Product> findByName(String name);

}
