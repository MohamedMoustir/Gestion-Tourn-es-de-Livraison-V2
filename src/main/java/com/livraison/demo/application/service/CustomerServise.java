package com.livraison.demo.application.service;

import com.livraison.demo.domain.dao.CustomerDAO;
import com.livraison.demo.domain.entity.Customer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServise {

    private final CustomerDAO customerDAO ;

    @Autowired
    public CustomerServise(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public Customer createCustomer( Customer customer){
        return this.customerDAO.save(customer);
    }


}
