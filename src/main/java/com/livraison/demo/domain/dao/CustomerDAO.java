package com.livraison.demo.domain.dao;

import com.livraison.demo.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDAO extends JpaRepository<Customer,Integer> {
}
