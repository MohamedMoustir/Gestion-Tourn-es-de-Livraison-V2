package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseDAO extends JpaRepository<Warehouse,Integer> {
    Warehouse findByAddress(String address);
    Warehouse findWarehouseById(Long id);
}
