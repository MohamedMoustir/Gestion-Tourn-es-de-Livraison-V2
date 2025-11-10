package com.livraison.demo.testService;

import com.livraison.demo.application.mapper.CustomerMapper;
import com.livraison.demo.application.service.CustomerServise;
import com.livraison.demo.domain.dao.CustomerDAO;
import com.livraison.demo.domain.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CustomerTest {

    @InjectMocks
    private CustomerServise customerServise ;

    @Mock
    private CustomerMapper customerMapper ;

    @Mock
    private CustomerDAO customerDAO ;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testServiseCreateCustomer_Success() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("mohamed")
                .address("zagoura")
                .latitude(80.0209)
                .longitude(-6.8417)
                .preferredTimeSlot("12:00")
                .build();



        //create Mock in Customer
        when(customerDAO.save(Mockito.any(Customer.class))).thenReturn(customer);
        customerServise.createCustomer(customer);
        //check how many method save call
        verify(customerDAO ,times(1)).save(Mockito.any(Customer.class));
    }

}
