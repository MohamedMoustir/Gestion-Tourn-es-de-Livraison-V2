package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDAO extends JpaRepository<Delivery,Integer> {

    Delivery findById(Long id);
}
