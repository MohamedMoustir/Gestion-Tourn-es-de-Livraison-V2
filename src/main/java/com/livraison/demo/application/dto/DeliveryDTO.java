package com.livraison.demo.application.dto;

import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryDTO {
    private Long id;
    private double latitude;
    private double longitude;
    private double weightKg;
    private double volumeM3;
    private String timeSlot;
    private String status;
    private int tourId;
    private int customerId;
}
