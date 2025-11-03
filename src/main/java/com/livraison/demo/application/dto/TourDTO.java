package com.livraison.demo.application.dto;

import lombok.AllArgsConstructor;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourDTO {
    private Long id ;
    private LocalDateTime tour_day  ;
    private double total_distance_km ;
    private String warehouseName ;
    private Long warehouseId;
    private String vehicleType ;
    private Long vehicleId;
    private List<DeliveryDTO> deliverys ;
}
