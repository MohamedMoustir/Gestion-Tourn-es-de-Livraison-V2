package com.livraison.demo.application.dto;

import lombok.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private String registrationNumber;
    private String model;
    private String type;
    private double maxWeightKg;
    private double maxVolumeM3;
    private List<Integer> tourIds;
}
