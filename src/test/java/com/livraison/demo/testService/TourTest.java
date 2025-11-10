package com.livraison.demo.testService;

import com.livraison.demo.application.dto.*;
import com.livraison.demo.application.mapper.TourMapper;
import com.livraison.demo.application.service.TourService;
import com.livraison.demo.domain.dao.*;
import com.livraison.demo.domain.entity.*;
import com.livraison.demo.domain.enums.DeliveryStatus;
import com.livraison.demo.domain.enums.VehicleType;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.*;

public class TourTest {
    @Mock
    private TourDAO tourDAO ;

    @InjectMocks
    private TourService tourService ;

    @Mock
    private TourMapper tourMapper ;

    @Mock
    private WarehouseDAO warehouseDAO;

    @Mock
    private VehicleDAO vehicleDAO;

    @Mock
    private CustomerDAO customerDAO;
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateTour_Success() {
        Warehouse warehouse = Warehouse.builder()
                .id(1L)
                .name("Main Warehouse")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .model("micor")
                .type(VehicleType.TRUCK)
                .maxWeightKg(5000.0)
                .maxVolumeM3(40.0)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .name("mohamed")
                .address("zagoura")
                .latitude(80.0209)
                .longitude(-6.8417)
                .preferredTimeSlot("12:00")
                .build();

        Tour tour = Tour.builder()
                .tour_day(LocalDateTime.parse("2025-10-25T08:30:00"))
                .total_distance_km(0)
                .warehouse(warehouse)
                .vehicle(vehicle)
                .deliveries(new ArrayList<>())
                .build();

        Delivery delivery = Delivery.builder()
                .id(1L)
                .latitude(80.0209)
                .longitude(-6.8417)
                .weightKg(0.8)
                .volumeM3(0.02)
                .status(DeliveryStatus.PENDING)
                .tour(tour)
                .customer(customer)
                .build();
        tour.getDeliveries().add(delivery);

        when(warehouseDAO.findById(1)).thenReturn(Optional.of(warehouse));
        when(vehicleDAO.findById(1L)).thenReturn(Optional.of(vehicle));
        when(customerDAO.findById(1)).thenReturn(Optional.of(customer));
        when(tourDAO.save(Mockito.any(Tour.class))).thenReturn(tour);

        tourService.createTour(tour);

        verify(tourDAO, times(1)).save(Mockito.any(Tour.class));
    }


    @Test
    void testServiseGetAllTour_Success() {
        Warehouse warehouse =Warehouse.builder().id(1L).name("Main Warehouse").build() ;
        Vehicle vehicle = Vehicle.builder().id(1L).model("micor").build();
        Tour tour = Tour.builder()
                .tour_day(LocalDateTime.parse("2025-10-25T08:30:00"))
                .total_distance_km(0)
                .warehouse(warehouse)
                .vehicle(vehicle)
                .build();
        List<DeliveryDTO> deliveries = List.of(
                DeliveryDTO.builder()
                        .latitude(80.0209)
                        .longitude(-6.8417)
                        .weightKg(0.8)
                        .volumeM3(0.02)
                        .timeSlot("11:00-13:00")
                        .status("PENDING")
                        .tourId(3)
                        .build()
        );

        TourDTO tourDTO = TourDTO.builder()
                .tour_day(LocalDateTime.parse("2025-10-25T08:30:00"))
                .total_distance_km(0)
                .vehicleId(1L)
                .warehouseId(1L)
                .deliverys(deliveries).build();

        when(warehouseDAO.findById(1)).thenReturn(Optional.of(warehouse));
        when(vehicleDAO.findById(1L)).thenReturn(Optional.of(vehicle));
        when(tourDAO.findAllWithDeliveries()).thenReturn(Collections.singletonList(tour));
        when(tourMapper.toDTO(tour)).thenReturn(tourDTO);

        List<TourDTO> result = tourService.recherchertour();
        AssertionsForInterfaceTypes.assertThat(result).isNotEmpty();
        verify(tourDAO,times(1)).findAllWithDeliveries();
    }
    @Test
    void testServiseDeleteTourById_Success() {
        doNothing().when(tourDAO).deleteById(1);
        tourService.deleteTourById(1L);
        verify(tourDAO,times(1)).deleteById(1);
    }
}
