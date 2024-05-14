package com.romashkaco.testcase.delivery.repository;

import com.romashkaco.testcase.delivery.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
}
