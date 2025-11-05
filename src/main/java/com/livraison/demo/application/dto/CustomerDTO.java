package com.livraison.demo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private String name ;
    private String address;
    private double latitude;
    private double longitude;
    private String preferredTimeSlot;

}
