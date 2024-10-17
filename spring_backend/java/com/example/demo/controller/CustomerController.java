package com.example.demo.controller;

import com.example.demo.customStatusCode.SelectedCustomerItemAndOrderStatusCode;
import com.example.demo.dto.CustomerStatus;
import com.example.demo.dto.impl.CustomerDto;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.DataPersistException;
import com.example.demo.service.CustomerService;
import com.example.demo.service.impl.CustomerServiceImpl;
import com.example.demo.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // Create customer
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveCustomer(@RequestBody CustomerDto customerDto) {
        try {
            String cusId = customerService.generateCustomerId();
            customerDto.setCustomerId(cusId);

            // Save customer
            customerService.saveCustomer(customerDto);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve specific customer by ID
    @GetMapping(value = "/{cusId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerStatus getSelectedCustomer(@PathVariable("cusId") String cusId) {
        String regexForCustomerID = "^Customer-[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$";
        Pattern regexPattern = Pattern.compile(regexForCustomerID);
        Matcher regexMatcher = regexPattern.matcher(cusId);

        if (!regexMatcher.matches()) {
            return new SelectedCustomerItemAndOrderStatusCode(1, "Customer ID is not valid");
        }

        return customerService.getCustomer(cusId);
    }

    // Delete a customer by ID
    @DeleteMapping(value = "/{cusId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("cusId") String cusId) {
        try {
            customerService.deleteCustomer(cusId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all customers
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // Update customer by ID
    @PutMapping(value = "/{cusId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCustomer(
            @RequestBody CustomerDto customerDto,
            @PathVariable("cusId") String cusId) {
        try {
            // Set the customer ID from path
            customerDto.setCustomerId(cusId);

            customerService.updateCustomer( cusId,customerDto);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CustomerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/generateCustomerId")
    public ResponseEntity<Map<String, String>> generateCustomerId() {
        String customerId = customerService.generateCustomerId();
        Map<String, String> response = new HashMap<>();
        response.put("id", customerId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
