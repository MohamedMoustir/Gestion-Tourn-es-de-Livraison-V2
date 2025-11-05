package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.application.dto.DeliveryHistoryDTO;
import com.livraison.demo.application.service.DeliveryHistoryService;
import com.livraison.demo.domain.entity.DeliveryHistory;
import com.livraison.demo.domain.exception.DeliveryNotDeletedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/deliveryhistory")
public class DeliveryHistoryController {

    private  DeliveryHistoryService deliveryHistoryService;

    public DeliveryHistoryController(DeliveryHistoryService deliveryHistoryService) {
        this.deliveryHistoryService = deliveryHistoryService;
    }

    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rechercherDeliveryhistory(){
        try{
            List<DeliveryHistoryDTO> deliveryDTOS = this.deliveryHistoryService.getAllDeliveryHistory();
            return ResponseEntity.ok(deliveryDTOS);
        }catch(DeliveryNotDeletedException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }

    }
}
