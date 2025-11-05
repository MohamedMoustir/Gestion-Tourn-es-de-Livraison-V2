package com.livraison.demo.application.service;

import com.livraison.demo.application.dto.DeliveryHistoryDTO;
import com.livraison.demo.application.mapper.DeliveryHistoryMapper;
import com.livraison.demo.domain.dao.DeliveryHistoryDAO;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.DeliveryHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DeliveryHistoryService {

    private final DeliveryHistoryDAO historyDAO;
    private final DeliveryHistoryMapper deliveryHistoryMapper;

    @Autowired
    public DeliveryHistoryService(DeliveryHistoryDAO historyDAO, DeliveryHistoryMapper deliveryHistoryMapper) {
        this.historyDAO = historyDAO;
        this.deliveryHistoryMapper = deliveryHistoryMapper;
    }

    public void  createDeliveryHistory(Delivery deliveryHistory){
        Map<String ,LocalTime> mapTime = devisTime(deliveryHistory.getTimeSlot());

        LocalTime planned = mapTime.get("plannedTime");
        LocalTime actual = mapTime.get("Actual_time");
        long delayMinutes = java.time.Duration.between(planned, actual).toMinutes();
        String dayOfWeek = java.time.LocalDate.now().getDayOfWeek().name();

        DeliveryHistory deliveryHistoryDTO = DeliveryHistory.builder()
                .delivery(deliveryHistory)
                .customer(deliveryHistory.getCustomer())
                .tour(deliveryHistory.getTour())
                .delay(String.valueOf(delayMinutes))
                .address(deliveryHistory.getCustomer().getAddress())
                .planned_time(mapTime.get("plannedTime"))
                .actual_time(mapTime.get("Actual_time"))
                .dayOfWeek(dayOfWeek)
                .build();
             this.historyDAO.save(deliveryHistoryDTO);
    }

    public static  Map<String ,LocalTime> devisTime(String timeSlot){
      String startTime = timeSlot.split("-")[0];
      String endTime = timeSlot.split("-")[1];
        LocalTime plannedTime = LocalTime.parse(startTime);
        LocalTime Actual_time = LocalTime.parse(endTime);
        Map<String ,LocalTime> map = new HashMap<>();
        map.put("plannedTime" ,plannedTime);
        map.put("Actual_time" ,Actual_time);
        return map;
    }

    public List<DeliveryHistoryDTO> getAllDeliveryHistory(){
        return this.historyDAO.findAll().stream()
                .map(deliveryHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }

}
