package com.livraison.demo.application.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehouseDTO {
    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private List<Integer> tourIds;
}
