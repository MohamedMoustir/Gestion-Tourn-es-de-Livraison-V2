package com.livraison.demo.application.service;

import com.livraison.demo.application.dto.WarehouseDTO;
import com.livraison.demo.application.mapper.WarehouseMapper;
import com.livraison.demo.domain.dao.WarehouseDAO;
import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.entity.Warehouse;
import com.livraison.demo.domain.exception.VehicleFoundException;
import com.livraison.demo.domain.exception.VehicleNotDeletedException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService.LOGGER;

@Validated
@Service
public class WarehouseService {

    private final WarehouseDAO warehouseDAO ;
     private final WarehouseMapper warehouseMapper ;
    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseService.class);
    @Autowired
    public WarehouseService(WarehouseDAO warehouseDAO, WarehouseMapper warehouseMapper){
        this.warehouseDAO = warehouseDAO ;
        this.warehouseMapper = warehouseMapper;
    }
    public void CreateWarehouse(Warehouse warehouse) {
        if (warehouse.getLatitude() == null || warehouse.getLongitude() == null || warehouse.getClosingTime() == null) {
            throw new IllegalArgumentException("Latitude, Longitude et ClosingTime sont obligatoires.");
        }

        Warehouse checkwarehouse= this.warehouseDAO.findByAddress(warehouse.getAddress());
        if (checkwarehouse == null) {
            this.warehouseDAO.save(warehouse);
            LOGGER.info("Creating vehicle: {}", warehouse.getAddress());
        } else {
            LOGGER.warn("Vehicle already exists: {}", warehouse.getAddress());
            throw new VehicleFoundException("Vehicle already exists with registration number: " + warehouse.getAddress());
        }
    }

    public List<WarehouseDTO> rechercherWarehouse() {
        
        return this.warehouseDAO.findAll().stream()
                .map(warehouseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WarehouseDTO getWarehouseById(Long id) {
        Warehouse warehouse = this.warehouseDAO.findWarehouseById(id);
        return warehouseMapper.toDTO(warehouse);
    }

    public void deleteWarehouseById(Long id) {
        try{
            this.warehouseDAO.deleteById(Math.toIntExact(id));
            LOGGER.info("suppression warehouse avec successful avec id : {}", id);

        }catch(Exception e){
            throw new VehicleNotDeletedException("Error sur suppression warehouse ");
        }
    }

    public Warehouse updateWarehouseById(Warehouse warehouse, Long id) {
        return this.warehouseDAO.findById(Math.toIntExact(id)).
                map(existingWarehouse ->{
                    existingWarehouse.setName(warehouse.getName());
                    existingWarehouse.setAddress(warehouse.getAddress());
                    existingWarehouse.setLatitude(warehouse.getLatitude());
                    existingWarehouse.setLongitude(warehouse.getLongitude());
                    existingWarehouse.setOpeningTime(warehouse.getOpeningTime());
                    existingWarehouse.setClosingTime(warehouse.getClosingTime());
                    return this.warehouseDAO.save(existingWarehouse);
                })
                .orElseThrow(()->{
                    LOGGER.warn("Erreur lors de la mise à jour du warehouse avec id: {}", warehouse.getId());
                    new VehicleFoundException(
                            "warehouse with id " + id + " does not exist"
                    );
                    return null;
                });
    }
}
