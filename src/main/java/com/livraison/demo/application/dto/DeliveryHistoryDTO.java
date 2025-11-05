package com.livraison.demo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryHistoryDTO {

    private Long id;
    private Long customerId;
    private Long deliveryId;
    private Long tourId ;
    private LocalTime planned_time ;
    private LocalTime actual_time;
    private String delay ;
    private String dayOfWeek ;
    private String address ;

}
