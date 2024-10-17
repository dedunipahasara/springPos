package com.example.demo.service;

import com.example.demo.dto.CustomerStatus;
import com.example.demo.dto.ItemStatus;
import com.example.demo.dto.impl.CustomerDto;
import com.example.demo.dto.impl.ItemDto;

import java.util.List;

public interface ItemService {
    void saveItem(ItemDto itemDTO);
    List<ItemDto> getAllItems();
    ItemStatus getItem(String itemId);
    void deleteItem(String itemId);
    void updateItem(String itemId, ItemDto itemDTO);
    String generateItemId(); // Add this method to the interface

}
