package com.livraison.demo.testService;

import com.livraison.demo.application.dto.VehicleDTO;
import com.livraison.demo.application.mapper.VehicleMapper;
import com.livraison.demo.application.service.VehicleService;
import com.livraison.demo.domain.dao.VehicleDAO;
import com.livraison.demo.domain.entity.Vehicle;
import com.livraison.demo.domain.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


public class VehicleTest {

    @Mock
    private  VehicleDAO vehicleDAO;

    @InjectMocks
    private VehicleService vehicleService;

    //@Mock
    //private Vehicle vehicle;
    @Mock
    private VehicleMapper vehicleMapper;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testServiseCreateVehicle_Success() {
       Vehicle vehicle1 = Vehicle.builder()
               .model("Transit")
               .type(VehicleType.TRUCK)
               .maxWeightKg(5000.0)
               .maxVolumeM3(40.0)
               .registrationNumber("ADE1234").build();

        VehicleDTO vehicleDTO =VehicleDTO.builder()
                .model("Transit")
                .type(String.valueOf(VehicleType.TRUCK))
                .maxWeightKg(5000.0)
                .maxVolumeM3(40.0)
                .registrationNumber("ADE1234").build();

        //create Mock in vehicle
      when(vehicleDAO.save(Mockito.any(Vehicle.class))).thenReturn(vehicle1);
        vehicleService.createVehicle(vehicle1);
        //check how many method save call
        verify(vehicleDAO ,times(1)).save(Mockito.any(Vehicle.class));
    }

    @Test
    void testServiseGetAllVehicle_Success() {
        Vehicle vehicle1 = Vehicle.builder()
                .model("Transit")
                .type(VehicleType.TRUCK)
                .registrationNumber("ADE1234").build();

        VehicleDTO vehicleDTO = VehicleDTO.builder()
                .model("Transit")
                .type("TRUCK")
                .registrationNumber("ADE1234")
                .build();

    when(vehicleDAO.findAll()).thenReturn((Collections.singletonList(vehicle1)));
        when(vehicleMapper.toDTO(vehicle1)).thenReturn((vehicleDTO));
    List<VehicleDTO> result = vehicleService.recherchervehicle();
    verify(vehicleDAO,times(1)).findAll();

    //chek is data retun in result like this data
        assertThat(result.get(0).getModel()).isEqualTo("Transit");
        assertThat(result.get(0).getType()).isEqualTo("TRUCK");
        assertThat(result.get(0).getRegistrationNumber()).isEqualTo("ADE1234");
    }

    @Test
    void testServiseGetVehicleByID_Success() {
        Vehicle vehicle1 = Vehicle.builder()
                .model("Transit")
                .type(VehicleType.TRUCK)
                .registrationNumber("ADE1234").build();

        when(vehicleDAO.findById(1L)).thenReturn(Optional.ofNullable((vehicle1)));
        Vehicle vehicle = vehicleService.getVehicleById(1L);
        verify(vehicleDAO,times(1)).findById(1L);
    }

    @Test
    void testServiseDeleteVehicleByID_Success() {
        doNothing().when(vehicleDAO).deleteById(1L);
        vehicleService.deleteVehicleById(1L);
        verify(vehicleDAO,times(1)).deleteById(1L);
    }
}
