package com.livraison.demo.application.service;

import com.livraison.demo.application.dto.VehicleDTO;
import com.livraison.demo.application.mapper.VehicleMapper;
import com.livraison.demo.domain.dao.VehicleDAO;
import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.enums.VehicleType;
import com.livraison.demo.domain.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Service
public class VehicleService {

    private final VehicleDAO vehicleDAO;
    private final VehicleMapper vehicleMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(VehicleService.class);
    @Autowired
    public VehicleService(VehicleDAO vehicleDAO, VehicleMapper vehicleMapper) {
        this.vehicleDAO = vehicleDAO;
        this.vehicleMapper = vehicleMapper;
    }

    public void createVehicle(@Valid   Vehicle vehicle) {
        Vehicle checkvehicle = this.vehicleDAO.findByRegistrationNumber(vehicle.getRegistrationNumber());

        if (checkvehicle == null) {

            applyVehicleConstraints(vehicle);
            System.out.println("Vehicle =================>" +vehicle);
            this.vehicleDAO.save(vehicle);
            LOGGER.info("Creating vehicle: {}", vehicle.getRegistrationNumber());
        } else {
            LOGGER.warn("Vehicle already exists: {}", vehicle.getRegistrationNumber());
            throw new VehicleFoundException("Vehicle already exists with registration number: " + vehicle.getRegistrationNumber());
        }



    }

        public List<VehicleDTO> recherchervehicle() {
            return this.vehicleDAO.findAll().stream()
                    .map(vehicleMapper::toDTO)
                    .collect(Collectors.toList());
        }

    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> vehicle = this.vehicleDAO.findById(id);
        return vehicle.orElse(null);
    }

    public void deleteVehicleById(Long id) {
        try{
            this.vehicleDAO.deleteById(id);
            LOGGER.info("suppression vehicle avec successful avec id : {}", id);

        }catch(Exception e){
         throw new VehicleNotDeletedException("Error sur suppression vehicle ");
        }
    }

    public Vehicle updateVehicleById( @Valid Vehicle vehicle, Long id) {
        return this.vehicleDAO.findById(id).
                map(existingVehicle ->{
                    existingVehicle.setMaxWeightKg(vehicle.getMaxWeightKg());
                    existingVehicle.setMaxVolumeM3(vehicle.getMaxVolumeM3());
                    existingVehicle.setModel(vehicle.getModel());
                    existingVehicle.setRegistrationNumber(vehicle.getRegistrationNumber());
                    existingVehicle.setType(vehicle.getType());
                    applyVehicleConstraints(vehicle);
                    return this.vehicleDAO.save(existingVehicle);
                })
                .orElseThrow(()->{
                    LOGGER.warn("Erreur lors de la mise à jour du véhicule avec id: {}", vehicle.getId());
                    new VehicleFoundException(
                            "Vehicle with id " + id + " does not exist"
                    );
                    return null;
                });

    }

    private void applyVehicleConstraints(Vehicle vehicle){
        switch (vehicle.getType()) {
            case BIKE -> {
                vehicle.setMaxWeightKg(50.0);
                vehicle.setMaxVolumeM3(0.5);
            }
            case VAN -> {
                vehicle.setMaxWeightKg(1000.0);
                vehicle.setMaxVolumeM3(8.0);
            }
            case TRUCK -> {
                vehicle.setMaxWeightKg(5000.0);
                vehicle.setMaxVolumeM3(40.0);
            }
        }
    }

}
