package com.livraison.demo.testService;

import com.livraison.demo.application.service.DeliveryHistoryService;
import com.livraison.demo.application.service.TourService;
import com.livraison.demo.application.service.VehicleService;
import com.livraison.demo.domain.dao.*;
import com.livraison.demo.domain.entity.*;
import com.livraison.demo.domain.enums.DeliveryStatus;
import com.livraison.demo.domain.enums.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertFalse;

import java.time.LocalDateTime;
import java.time.LocalTime;

@SpringBootTest
@Transactional
class DeliveryHistoryIntegrationTest {

    @Autowired
    private TourService tourService;

    @Autowired
    private TourDAO tourDAO;

    @Autowired
    private DeliveryDAO deliveryDAO;

    @Autowired
    private DeliveryHistoryDAO deliveryHistoryDAO;

    @Autowired
    private WarehouseDAO warehouseDAO;

    @Autowired
    private DeliveryHistoryService deliveryHistoryService;


    @Autowired
    private VehicleDAO vehicleDAO;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private CustomerDAO customerDAO;
    @Test

    void shouldCreateDeliveryHistoryWhenTourIsCompleted() {

        var vehicle = Vehicle.builder()
                .type(VehicleType.VAN)
                .model("Transit Van")
                .registrationNumber("AB-1234")
                .build();
        vehicleService.createVehicle(vehicle);

        var warehouse = Warehouse.builder()
                .name("Main Warehouse")
                .address("123 Main St")
                .latitude(31.63)
                .longitude(-7.98)
                .openingTime(LocalTime.of(8,0))
                .closingTime(LocalTime.of(18,0))
                .build();
        warehouseDAO.save(warehouse);

        var tour = Tour.builder()
                .tour_day(LocalDateTime.now())
                .vehicle(vehicle)
                .warehouse(warehouse)
                .total_distance_km(0)
                .build();
        tourDAO.save(tour);

        var customer = Customer.builder()
                .name("Mohamed")
                .address("Zagoura")
                .latitude(80.0209)
                .longitude(-6.8417)
                .preferredTimeSlot("12:00")
                .build();
        customerDAO.save(customer);

        // إنشاء 3 deliveries مختلفة
        for (int i = 0; i < 3; i++) {
            var delivery = Delivery.builder()
                    .tour(tour)
                    .latitude(31.63 + i * 0.01)
                    .longitude(-7.98 + i * 0.01)
                    .weightKg(5.0 + i)
                    .volumeM3(0.2 + i * 0.01)
                    .timeSlot("08:00-10:00")
                    .status(DeliveryStatus.COMPLETED)
                    .customer(customer)
                    .build();
            deliveryDAO.save(delivery);

            // call service method
            deliveryHistoryService.createDeliveryHistory(delivery);
        }

        var histories = deliveryHistoryDAO.findAll();

        assertFalse("Aucun DeliveryHistory n’a été créé !", histories.isEmpty());
        assertEquals(3, histories.size());
    }

}
