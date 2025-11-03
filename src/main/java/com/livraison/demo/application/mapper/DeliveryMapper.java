package com.livraison.demo.application.mapper;

import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.enums.DeliveryStatus;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {
    public DeliveryDTO toDTO(Delivery delivery){
        if(delivery == null) return null ;
        return DeliveryDTO.builder()
                .id(delivery.getId())
                .latitude(delivery.getLatitude())
                .longitude(delivery.getLongitude())
                .weightKg(delivery.getWeightKg())
                .volumeM3(delivery.getVolumeM3())
                .timeSlot(delivery.getTimeSlot())
                .status(delivery.getStatus() != null ? delivery.getStatus().name() :null )
                .tourId(Math.toIntExact(delivery.getTour() != null ? delivery.getTour().getId() : null))
                .build();
    }

    public Delivery toEntity(DeliveryDTO dto) {
        if (dto == null) return null;

        return Delivery.builder()
                .id(dto.getId())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .weightKg(dto.getWeightKg())
                .volumeM3(dto.getVolumeM3())
                .timeSlot(dto.getTimeSlot())
                .status(dto.getStatus() != null ? DeliveryStatus.valueOf(dto.getStatus()) : null)
                .build();
    }

}
