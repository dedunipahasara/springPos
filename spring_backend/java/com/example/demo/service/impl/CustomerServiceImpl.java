package com.example.demo.service.impl;

import com.example.demo.customStatusCode.SelectedCustomerItemAndOrderStatusCode;
import com.example.demo.dao.CustomerDao;
import com.example.demo.dto.CustomerStatus;
import com.example.demo.dto.impl.CustomerDto;
import com.example.demo.dto.impl.ItemDto;
import com.example.demo.entity.impl.Customer;
import com.example.demo.entity.impl.Item;
import com.example.demo.exception.CustomerNotFoundException;
import com.example.demo.exception.DataPersistException;
import com.example.demo.service.CustomerService;
import com.example.demo.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao cusDao;
    @Autowired
    private Mapping mapping;

    @Override
    public void saveCustomer(CustomerDto cusDTO) {
        Customer savedCustomer =
                cusDao.save(mapping.toCustomerEntity(cusDTO));
        if (savedCustomer == null) {
            throw new DataPersistException("Customer not saved");
        }
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        List<Customer> allCustomers = cusDao.findAll();
        return mapping.asCustomerDTOList(allCustomers);
    }

    @Override
    public CustomerStatus getCustomer(String cusId) {
        if (cusDao.existsById(cusId)) {
            Customer selectedCustomer = cusDao.getReferenceById(cusId);
            return mapping.toCustomerDTO(selectedCustomer);
        } else {
            return new SelectedCustomerItemAndOrderStatusCode(2, "Customer with id " + cusId + " not found");
        }

    }

    @Override
    public void deleteCustomer(String cusId) {
        Optional<Customer> existedCustomer = cusDao.findById(cusId);
        if (!existedCustomer.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + cusId + " not found");
        } else {
            cusDao.deleteById(cusId);
        }
    }

    @Override
    public void updateCustomer(String cusId, CustomerDto customerDto) {
        Optional<Customer> tmpCustomer = cusDao.findById(cusId);
        if (tmpCustomer.isPresent()) {
            tmpCustomer.get().setCustomerId(customerDto.getCustomerId());
            tmpCustomer.get().setCustomerName(customerDto.getCustomerName());
            tmpCustomer.get().setCustomerAddress(customerDto.getCustomerAddress());
            tmpCustomer.get().setCustomerSalary(customerDto.getCustomerSalary());
        }
    }
    @Override
    public String generateCustomerId() {
        String lastCustomerId = cusDao.findLastCustomerId(); // Get the last customer ID from the DB
        if (lastCustomerId == null || lastCustomerId.isEmpty()) {
            return "C00-001";  // If no customers exist, start with C00-001
        }

        // Extract the numeric part of the last customer ID
        int lastId = Integer.parseInt(lastCustomerId.split("-")[1]);

        // Increment the numeric part
        lastId += 1;

        // Return the next customer ID in the format C00-XXX
        return String.format("C00-%03d", lastId);
    }

}


