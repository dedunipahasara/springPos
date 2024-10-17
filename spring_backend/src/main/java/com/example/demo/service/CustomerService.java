package com.example.demo.service;

import com.example.demo.dto.CustomerStatus;
import com.example.demo.dto.impl.CustomerDto;

import java.util.List;

public interface CustomerService {
    void saveCustomer(CustomerDto cusDTO);
    List<CustomerDto> getAllCustomers();
    CustomerStatus getCustomer(String cusId);
    void deleteCustomer(String cusId);
    void updateCustomer(String cusId,CustomerDto cusDTO);

    String generateCustomerId(); // Add this method to the interface


}
