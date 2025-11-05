package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.VehicleDTO;
import com.livraison.demo.application.mapper.VehicleMapper;
import com.livraison.demo.application.service.VehicleService;
import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.exception.VehicleNotDeletedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/vehicles")
public class VehicleController {

    private VehicleService vehicleService ;
    private VehicleMapper vehicleMapper ;
    public VehicleController(VehicleService vehicleService  ,VehicleMapper vehicleMapper){
        this.vehicleService =  vehicleService ;
        this.vehicleMapper = vehicleMapper ;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = APPLICATION_JSON_VALUE)

    public ResponseEntity<?> create(@RequestBody  VehicleDTO vehicleDTO){
        try{
            Vehicle vehicle =  this.vehicleMapper.toEntity(vehicleDTO);
            this.vehicleService.createVehicle(vehicle);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Vehicle created"));
        }catch(VehicleNotDeletedException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }
    }

   @GetMapping(produces=APPLICATION_JSON_VALUE)
    public ResponseEntity<?> recherchervehicle(){
       try{
           List<VehicleDTO> vehicle =  this.vehicleService.recherchervehicle();
           return ResponseEntity
                   .status(HttpStatus.CREATED)
                   .body(Map.of("success", "Vehicle updated"));
       }catch(VehicleNotDeletedException e){
           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(Map.of("error", e.getMessage()));
       }catch (Exception e) {
           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
       }
    }


   @GetMapping(path="{id}",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<?>  getVehicleById(@PathVariable Long id){

       try{
           Vehicle vehicle =  this.vehicleService.getVehicleById(id);;
           return ResponseEntity.ok(vehicle);

       }catch(VehicleNotDeletedException e){
           return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(Map.of("error", e.getMessage()));
       }catch (Exception e) {
           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
       }
       }


    @DeleteMapping(path="{id}",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<?> deleteVehicleById(@PathVariable Long id){
        try{
        this.vehicleService.deleteVehicleById(id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Suppression effectuée avec succès"));
        }catch(VehicleNotDeletedException e){
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Une erreur interne est survenue : " + e.getMessage()));
        }

    }

    @PutMapping(path="{id}",produces=APPLICATION_JSON_VALUE )
    public ResponseEntity<?> updateVehicleById(@RequestBody Vehicle vehicle ,@PathVariable Long id ){
        try{
             this.vehicleService.updateVehicleById(vehicle ,id);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Modification effectuée avec succès"));

        }catch(VehicleNotDeletedException e){
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
