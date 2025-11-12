package com.livraison.demo.testService;

import com.livraison.demo.application.mapper.DeliveryHistoryMapper;
import com.livraison.demo.application.service.DeliveryHistoryService;
import com.livraison.demo.domain.dao.DeliveryHistoryDAO;
import com.livraison.demo.domain.entity.DeliveryHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.annotation.meta.When;
import java.util.ArrayList;
import java.util.List;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

public class testUnit {

    @Mock
    private DeliveryHistoryMapper deliveryHistoryMapper ;
    @Mock
    private DeliveryHistoryDAO deliveryHistoryDAO ;
    @InjectMocks
    private DeliveryHistoryService deliveryHistoryService ;



    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDeliveryHistoryTest(){
         when(deliveryHistoryDAO.findAll()).thenReturn(new ArrayList<DeliveryHistory>());
        List<DeliveryHistory> deliveryHistories = deliveryHistoryDAO.findAll();
        assertThat(deliveryHistories).isEmpty();
        Mockito.verify(deliveryHistoryDAO, Mockito.times(1)).findAll();
    }
}
