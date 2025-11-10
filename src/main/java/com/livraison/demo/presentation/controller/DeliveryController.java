package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.application.mapper.DeliveryMapper;
import com.livraison.demo.application.service.DeliveryService;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.enums.DeliveryStatus;
import com.livraison.demo.domain.exception.DeliveryNotDeletedException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/deliveries")
public class DeliveryController {

    private  DeliveryMapper deliveryMapper ;
    private   DeliveryService deliveryService;


    public DeliveryController(DeliveryMapper deliveryMapper, DeliveryService deliveryService) {
        this.deliveryMapper = deliveryMapper;
        this.deliveryService = deliveryService;
    }

    
    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  rechercherdelivery(){
        try{
            List<DeliveryDTO> deliveryDTOS = this.deliveryService.rechercherdelivery();
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

    @GetMapping(path="{id}",produces= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> getDeliveryById(@PathVariable Long id){
        try{
          DeliveryDTO deliveryDTO =  this.deliveryService.getDeliveryById(id);
         return ResponseEntity.ok(deliveryDTO);
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

    @DeleteMapping(path="{id}",produces= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity deleteDeliveryById(@PathVariable Long id){
        try{
            this.deliveryService.deleteDeliveryById(id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Suppression effectuée avec succès"));
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

    @PutMapping(path="{id}",produces= MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> updateDelivery(@RequestBody Delivery delivery, @PathVariable Long id ){
        try{
            this.deliveryService.updateDelivery(delivery, id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Modification effectuée avec succès"));
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

    @PatchMapping(path="{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateStatus(@PathVariable Long id ) {

        try{
            this.deliveryService.updateStatus(id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Modification effectuée avec succès"));
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


    @GetMapping("/search")
    public ResponseEntity<?> searchDeliveries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(defaultValue = "0.0") Double weightKg,
            @RequestParam(required = false) Double volumeM3,
            @RequestParam(required = false) DeliveryStatus status
    ) {
        try{
            double safeVolume = (volumeM3 != null) ? volumeM3 : 0.0;
            List<DeliveryDTO> deliveries = deliveryService.searchDeliveries(page, size, weightKg, safeVolume, status);
            return ResponseEntity.ok(deliveries);
        }catch(Exception e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }


    }


    }
