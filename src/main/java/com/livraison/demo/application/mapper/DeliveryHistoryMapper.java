package com.livraison.demo.application.mapper;

import com.livraison.demo.application.dto.DeliveryHistoryDTO;
import com.livraison.demo.domain.entity.DeliveryHistory;
import org.springframework.stereotype.Component;

@Component
public class DeliveryHistoryMapper {

    public DeliveryHistoryDTO toDTO( DeliveryHistory deliveryHistory){

        return DeliveryHistoryDTO.builder()
                .id(deliveryHistory.getId())
                .deliveryId(deliveryHistory.getDelivery() != null ? deliveryHistory.getDelivery().getId():null)
                .customerId(deliveryHistory.getCustomer() != null ? deliveryHistory.getCustomer().getId():null)
                .tourId(deliveryHistory.getTour() != null ? deliveryHistory.getTour().getId():null)
                .delay(deliveryHistory.getDelay())
                .address(deliveryHistory.getAddress())
                .planned_time(deliveryHistory.getPlanned_time())
                .actual_time(deliveryHistory.getActual_time())
                .dayOfWeek(deliveryHistory.getDayOfWeek())
                .build();
    }


    public DeliveryHistory toEntity(DeliveryHistoryDTO dto){
        return DeliveryHistory.builder()
                .id(dto.getId())
                .delay(dto.getDelay())
                .actual_time(dto.getActual_time())
                .address(dto.getAddress())
                .planned_time(dto.getPlanned_time())
                .dayOfWeek(dto.getDayOfWeek()).build();
    }
}
