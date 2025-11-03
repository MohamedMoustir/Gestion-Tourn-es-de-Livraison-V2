package com.livraison.demo.application.mapper;

import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.application.dto.TourDTO;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.Tour;
import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.entity.Warehouse;
import com.livraison.demo.domain.enums.DeliveryStatus;

import java.util.stream.Collectors;

public class TourMapper {
    public TourDTO toDTO(Tour tour){
        if(tour == null) return null;
            return TourDTO.builder()
                    .id(tour.getId())
                    .tour_day(tour.getTour_day())
                    .total_distance_km(tour.getTotal_distance_km())
                    .warehouseName(tour.getWarehouse() != null ? tour.getWarehouse().getName():null)
                    .warehouseId(tour.getWarehouse() != null ? tour.getWarehouse().getId() : null)
                    .vehicleType(tour.getVehicle() != null ? tour.getVehicle().getType().name() : null)
                    .vehicleId(tour.getVehicle() != null ? tour.getVehicle().getId() : null)
                    .deliverys(tour.getDeliveries() != null ?
                                    tour.getDeliveries().stream()
                                            .map(d -> DeliveryDTO.builder()
                                                    .id(d.getId())
                                                    .latitude(d.getLatitude())
                                                    .longitude(d.getLongitude())
                                                    .weightKg(d.getWeightKg())
                                                    .volumeM3(d.getVolumeM3())
                                                    .timeSlot(d.getTimeSlot())
                                                    .status(String.valueOf(d.getStatus()))
                                                    .tourId(Math.toIntExact(tour.getId()))
                                                    .build())
                                            .collect(Collectors.toList()):null
                            ).build();

    }

    public Tour toEntity(TourDTO dto){
        if(dto == null) return null ;
        return Tour.builder()
                .id(dto.getId())
                .tour_day(dto.getTour_day())
                .total_distance_km(dto.getTotal_distance_km())

                .warehouse(dto.getWarehouseId() != null ?
                        Warehouse.builder().id(dto.getWarehouseId()).build() : null)

                .vehicle(dto.getVehicleId() != null ?
                        Vehicle.builder().id(dto.getVehicleId()).build() : null)

                .deliveries(dto.getDeliverys() != null ?
                        dto.getDeliverys().stream()
                                .map(d -> Delivery.builder()
                                        .id(d.getId())
                                        .latitude(d.getLatitude())
                                        .longitude(d.getLongitude())
                                        .weightKg(d.getWeightKg())
                                        .volumeM3(d.getVolumeM3())
                                        .timeSlot(d.getTimeSlot())
                                        .status(DeliveryStatus.valueOf(d.getStatus()))
                                        .build())
                                .collect(Collectors.toList()) : null)
                .build();

    }
}
