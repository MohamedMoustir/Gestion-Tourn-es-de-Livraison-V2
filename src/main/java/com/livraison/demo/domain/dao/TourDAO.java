package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TourDAO extends JpaRepository<Tour, Integer> {

    @Query("SELECT DISTINCT t FROM Tour t LEFT JOIN FETCH t.deliveries")
    List<Tour> findAllWithDeliveries();
    @Query("SELECT t FROM Tour t LEFT JOIN FETCH t.deliveries WHERE t.id = :id")
    Tour findDeliverieById(@Param("id") Long id);
}
