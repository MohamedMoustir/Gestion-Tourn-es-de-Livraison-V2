package com.livraison.demo.application.service;

import com.livraison.demo.application.dto.TourDTO;
import com.livraison.demo.application.mapper.TourMapper;
import com.livraison.demo.application.service.optimizer.TourOptimizer;
import com.livraison.demo.domain.dao.*;
import com.livraison.demo.domain.entity.*;
import com.livraison.demo.domain.exception.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Validated
@Service
public class TourService {

    private final TourDAO tourDAO ;
    private final VehicleDAO vehicleDAO ;
    private final WarehouseDAO warehouseDAO ;
    private final TourMapper tourMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(TourService.class);
    private final Map<String, TourOptimizer> optimizerMap;
    private final CustomerDAO customerDAO;
    @Autowired
    public TourService(TourDAO tourDAO, VehicleDAO vehicleDAO, WarehouseDAO warehouseDAO, TourMapper tourMapper, Map<String, TourOptimizer> optimizerMap, CustomerDAO customerDAO) {
        this.tourDAO = tourDAO;
        this.vehicleDAO = vehicleDAO;
        this.warehouseDAO = warehouseDAO;
        this.tourMapper = tourMapper;
        this.optimizerMap = optimizerMap;
        this.customerDAO = customerDAO;
    }


    public void createTour(@Valid Tour tour) {

        Warehouse warehouse = warehouseDAO.findById(Math.toIntExact(tour.getWarehouse().getId()))
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));


        Vehicle vehicle = vehicleDAO.findById(tour.getVehicle().getId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Customer firstCustomer = tour.getDeliveries().stream()
                .map(Delivery::getCustomer)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        if (calculWeightKg(tour) > vehicle.getMaxWeightKg() || calculVolumeM3(tour) > vehicle.getMaxVolumeM3()) {
            throw new TourExccdsException("Tour exceeds vehicle maximum weight or volume");
        }

        tour.setWarehouse(warehouse);
        tour.setVehicle(vehicle);

        if (tour.getDeliveries() != null) {
            tour.getDeliveries().forEach(d -> {
                d.setTour(tour);
                d.setCustomer(firstCustomer);
                System.out.println("================>" + d.getVolumeM3());
            });

        }

        Tour savedTour = tourDAO.save(tour);
        LOGGER.info("Tour created with ID: {}", savedTour.getId());
    }


    public List<TourDTO> recherchertour() {
        return tourDAO.findAllWithDeliveries()
                .stream()
                .map(tourMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TourDTO getTourById(Long id) {
        Tour tour = this.tourDAO.findDeliverieById(id);
        return tourMapper.toDTO(tour);
    }

    public void deleteTourById(Long id) {
        try{
            this.tourDAO.deleteById(Math.toIntExact(id));
            LOGGER.info("suppression Tour avec successful avec id : {}", id);
        }catch(Exception e){
            throw new TourNotDeletedException("Error sur suppression Tour ");
        }
    }

    public void updateTour(  Tour tour, Long id) {
        Tour checkingTour =   tourDAO.findById(Math.toIntExact(id))
                 .orElseThrow(() -> new TourNotFoundException("tour with id " + id + " does not exist"));
                    checkingTour.setTour_day(tour.getTour_day());
                    checkingTour.setTotal_distance_km(tour.getTotal_distance_km());
                     this.tourDAO.save(checkingTour);

    }

    public List<Map<String, Object>> getOptimizedTour(Long id, String type) {
        Tour tour = tourDAO.findDeliverieById(id);
        if (tour == null) {
            throw new RuntimeException("Tour with id " + id + " not found");
        }

        TourOptimizer optimizer = optimizerMap.get(type);
        if (optimizer == null) {
            throw new IllegalArgumentException("Unknown optimizer type: " + type);
        }

        return optimizer.calculateOptimalTour(tour);
    }




     public static double calculWeightKg(Tour tour){


        double sum = tour.getDeliveries().stream()
                .mapToDouble(Delivery::getWeightKg)
                .sum();
         return sum;
     }

     public static double calculVolumeM3(Tour tour){
         double sum = tour.getDeliveries().stream()
                 .mapToDouble(Delivery::getVolumeM3)
                 .sum();

         return sum;
     }



}
