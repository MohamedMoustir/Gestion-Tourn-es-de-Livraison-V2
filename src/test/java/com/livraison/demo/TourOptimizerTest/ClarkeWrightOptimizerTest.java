package com.livraison.demo.TourOptimizerTest;

import com.livraison.demo.application.mapper.TourMapper;
import com.livraison.demo.application.service.optimizer.ClarkeWrightOptimizer;
import com.livraison.demo.domain.dao.TourDAO;
import com.livraison.demo.domain.dao.VehicleDAO;
import com.livraison.demo.domain.dao.WarehouseDAO;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.entity.Tour;
import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.entity.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class ClarkeWrightOptimizerTest {

    @Mock
    private TourDAO tourDAO;

    @Mock
    private TourMapper tourMapper;

    @Mock
    private WarehouseDAO warehouseDAO;

    @Mock
    private VehicleDAO vehicleDAO;

    @InjectMocks
    private ClarkeWrightOptimizer clarkeWrightOptimizer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testServiseClarkeWrightOptimizer_Success() {
        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .latitude(31.63)
                .longitude(-8.00)
                .name("Main Warehouse")
                .build();
        Vehicle vehicle = Vehicle.builder().id(1L).model("micor").build();

        Tour tour = Tour.builder()
                .tour_day(LocalDateTime.parse("2025-10-25T08:30:00"))
                .total_distance_km(0)
                .warehouse(warehouse)
                .vehicle(vehicle)
                .build();

        Delivery d1 = Delivery.builder().latitude(31.63).longitude(-8.00).tour(tour).build();
        Delivery d2 = Delivery.builder().latitude(31.64).longitude(-7.98).tour(tour).build();
        Delivery d3 = Delivery.builder().latitude(31.62).longitude(-7.97).tour(tour).build();

        List<Delivery> deliveries = new ArrayList<>(List.of(d1, d2, d3));
        tour.setDeliveries(deliveries);

        when(warehouseDAO.findById(1)).thenReturn(Optional.of(warehouse));
        when(vehicleDAO.findById(1L)).thenReturn(Optional.of(vehicle));

        List<Map<String,Object>> result = clarkeWrightOptimizer.calculateOptimalTour(tour);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }
}
