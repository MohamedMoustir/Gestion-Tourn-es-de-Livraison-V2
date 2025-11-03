package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDAO extends JpaRepository<Vehicle, Long> {
    Vehicle findByRegistrationNumber(String registrationNumber);
}
