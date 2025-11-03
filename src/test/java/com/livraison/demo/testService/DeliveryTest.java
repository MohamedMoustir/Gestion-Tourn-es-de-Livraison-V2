package com.livraison.demo.testService;

import com.livraison.demo.application.dto.DeliveryDTO;
import com.livraison.demo.application.mapper.DeliveryMapper;
import com.livraison.demo.application.service.DeliveryService;
import com.livraison.demo.domain.dao.DeliveryDAO;
import com.livraison.demo.domain.entity.Delivery;
import com.livraison.demo.domain.enums.DeliveryStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

public class DeliveryTest {
    @Mock
    private DeliveryDAO deliveryDAO ;
    @InjectMocks
    private DeliveryService deliveryService ;
    @Mock
    private DeliveryMapper deliveryMapper ;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testServiseGetAllDelivrey_Success() {
    Delivery delivery = Delivery.builder()
            .latitude(80.0209)
            .longitude(-6.8417)
            .weightKg(0.8)
            .volumeM3(0.02)
            .timeSlot("11:00-13:00")
            .status(DeliveryStatus.PENDING).build();

        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                 .latitude(80.0209)
                .longitude(-6.8417)
                .weightKg(0.8)
                .volumeM3(0.02)
                .timeSlot("11:00-13:00")
                .tourId(2)
                .status(String.valueOf(DeliveryStatus.PENDING)).build();


        when(deliveryDAO.findAll()).thenReturn(Collections.singletonList(delivery));
        when(deliveryMapper.toDTO(delivery)).thenReturn(deliveryDTO);
        List<DeliveryDTO> result = deliveryService.rechercherdelivery();
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getLatitude()).isEqualTo(80.0209);
        assertThat(result.get(0).getStatus()).isEqualTo("PENDING");
    verify(deliveryDAO,times(1)).findAll();
}


    @Test
    void testServiseGetAllDelivreyById_Success() {
        Delivery delivery = Delivery.builder()
                .latitude(80.0209)
                .longitude(-6.8417)
                .weightKg(7.8)
                .volumeM3(0.02)
                .timeSlot("11:00-13:00")
                .status(DeliveryStatus.PENDING).build();
        when(deliveryDAO.findById(1L)).thenReturn(delivery);
        DeliveryDTO delivery1 = deliveryService.getDeliveryById(1L);
        verify(deliveryDAO ,times(1)).findById(1L);

    }
    @Test
    void testServiseDeleteDelivreyById_Success() {
       doNothing().when(deliveryDAO).deleteById(1);
       deliveryService.deleteDeliveryById(1L);
       verify(deliveryDAO ,times(1)).deleteById(1);
    }


}
