package com.livraison.demo.presentation.controller;

import com.livraison.demo.application.dto.WarehouseDTO;
import com.livraison.demo.application.mapper.WarehouseMapper;
import com.livraison.demo.application.service.WarehouseService;
import com.livraison.demo.domain.entity.Warehouse;
import com.livraison.demo.domain.exception.WarehouseNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/warehouse")

public class WarehouseController {
    private WarehouseService warehouseService ;
    private WarehouseMapper warehouseMapper ;

    public WarehouseController(WarehouseService warehouseService ,WarehouseMapper warehouseMapper){
        this.warehouseService = warehouseService ;
        this.warehouseMapper = warehouseMapper ;

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createWarehouse(@RequestBody WarehouseDTO warehouseDTO) {
        try {
            Warehouse warehouse = this.warehouseMapper.toEntity(warehouseDTO);
            this.warehouseService.CreateWarehouse(warehouse);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of("success", "Warehouse created successfully"));
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }


//    @GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> rechercherWarehouse() {
//        try {
//            List<WarehouseDTO> warehouses = this.warehouseService.rechercherWarehouse();
//
//            if (warehouses.isEmpty()) {
//                return ResponseEntity
//                        .status(HttpStatus.NOT_FOUND)
//                        .body(Map.of("error", "Aucun entrepôt trouvé"));
//            }
//
//            return ResponseEntity.ok(warehouses);
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Erreur lors de la récupération des entrepôts"));
//        }
//    }
//
//    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> getWarehouseById(@PathVariable Long id) {
//        try {
//            WarehouseDTO warehouse = this.warehouseService.getWarehouseById(id);
//            return ResponseEntity.ok(warehouse);
//        } catch (WarehouseNotFoundException e) {
//            return ResponseEntity
//                    .status(HttpStatus.NOT_FOUND)
//                    .body(Map.of("error", e.getMessage()));
//        } catch (Exception e) {
//            return ResponseEntity
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("error", "Erreur interne du serveur"));
//        }
//    }

    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteWarehouseById(@PathVariable Long id) {
        try {
            this.warehouseService.deleteWarehouseById(id);
            return ResponseEntity
                    .ok(Map.of("success", "Warehouse deleted successfully"));
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }

    @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateWarehouseById(@RequestBody Warehouse warehouse, @PathVariable Long id) {
        try {
            Warehouse updated = this.warehouseService.updateWarehouseById(warehouse, id);
            return ResponseEntity
                    .ok(Map.of("success", "Warehouse updated successfully"));
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error"));
        }
    }


    @GetMapping("/ask")
    public ResponseEntity<?> ask(@RequestParam String question) {
        System.out.println("zzzzzzzzzzzz");
//        String answer = aiServiceObtimizer.ask(question);
//        return ResponseEntity.ok(Map.of("question", question, "answer", answer));
        return null;
    }

}
