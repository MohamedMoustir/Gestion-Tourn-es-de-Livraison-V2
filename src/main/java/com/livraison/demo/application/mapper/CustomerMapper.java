package com.livraison.demo.application.mapper;

import com.livraison.demo.application.dto.CustomerDTO;
import com.livraison.demo.domain.entity.Customer;
import com.livraison.demo.domain.entity.Delivery;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer customer){
        if(customer == null) return null;
        return  CustomerDTO.builder()
                 .name(customer.getName())
                 .address(customer.getAddress())
                 .latitude(customer.getLatitude())
                 .longitude(customer.getLongitude())
                 .preferredTimeSlot(customer.getPreferredTimeSlot()).build();
    }


    public Customer toEntity(CustomerDTO dto){
        if(dto == null) return null;
        return Customer.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .preferredTimeSlot(dto.getPreferredTimeSlot())
                .build();
    }
}
