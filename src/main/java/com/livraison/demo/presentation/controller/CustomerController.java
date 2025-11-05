package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.CustomerDTO;
import com.livraison.demo.application.mapper.CustomerMapper;
import com.livraison.demo.application.service.CustomerServise;
import com.livraison.demo.domain.entity.Customer;
import com.livraison.demo.domain.exception.ExceptionCustomerNotCreated;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    private final CustomerMapper customerMapper ;
    private final CustomerServise customerServise ;

    public CustomerController(CustomerMapper customerMapper, CustomerServise customerServise) {
        this.customerMapper = customerMapper;
        this.customerServise = customerServise;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDTO dto){
        try{
            Customer customer = this.customerMapper.toEntity(dto);
            Customer customer1 =  this.customerServise.createCustomer(customer);
            return ResponseEntity.ok(customer1);
        }catch(ExceptionCustomerNotCreated e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }
    }



}
