package com.livraison.demo.application.service;

import com.livraison.demo.domain.dao.CustomerDAO;
import com.livraison.demo.domain.entity.Customer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServise {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerServise(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }


    public Customer createCustomer(@Valid Customer customer) {
        return customerDAO.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerDAO.findAll();
    }

    public Optional<Customer> getCustomerById(Long id) {
        return customerDAO.findById(Math.toIntExact(id));
    }

    public Customer updateCustomer(Long id, @Valid Customer updatedCustomer) {
        return customerDAO.findById(Math.toIntExact(id))
                .map(customer -> {
                    customer.setName(updatedCustomer.getName());
                    customer.setAddress(updatedCustomer.getAddress());
                    customer.setLongitude(updatedCustomer.getLongitude());
                    customer.setLatitude(updatedCustomer.getLongitude());
                    customer.setPreferredTimeSlot(updatedCustomer.getPreferredTimeSlot());
                    return customerDAO.save(customer);
                })
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + id));
    }
    public void deleteCustomer(Long id) {
        if (customerDAO.existsById(Math.toIntExact(id))) {
            customerDAO.deleteById(Math.toIntExact(id));
        } else {
            throw new RuntimeException("Customer not found with ID: " + id);
        }
    }
}
