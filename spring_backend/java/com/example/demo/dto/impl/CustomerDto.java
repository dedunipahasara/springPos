package com.example.demo.dto.impl;

import com.example.demo.dto.CustomerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDto implements CustomerStatus {
    private String customerId;
    private String customerName;

    private String customerAddress;

    private double customerSalary;
}
