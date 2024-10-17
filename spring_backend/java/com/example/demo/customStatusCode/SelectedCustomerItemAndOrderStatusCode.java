package com.example.demo.customStatusCode;

import com.example.demo.dto.CustomerStatus;
import com.example.demo.dto.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SelectedCustomerItemAndOrderStatusCode implements CustomerStatus, ItemStatus {
    private int statusCode;
    private String statusMessage;


}
