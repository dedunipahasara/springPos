package com.example.demo.dto.impl;

import com.example.demo.dto.ItemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto implements ItemStatus {
    private String itemId;
    private String itemName;
    private double itemQty;
    private double itemPrice;
}
