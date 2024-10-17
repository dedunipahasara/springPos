package com.example.demo.controller;

import com.example.demo.customStatusCode.SelectedCustomerItemAndOrderStatusCode;
import com.example.demo.dto.ItemStatus;
import com.example.demo.dto.impl.ItemDto;
import com.example.demo.exception.DataPersistException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.service.ItemService;
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
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    // Create item
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> saveItem(@RequestBody ItemDto itemDto) {
        try {
            // Generate Item ID
           String itemId = itemService.generateItemId();
            itemDto.setItemId(itemId);

            // Save Item
            itemService.saveItem(itemDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (DataPersistException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Retrieve specific item by ID
    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ItemStatus getSelectedItem(@PathVariable("itemId") String itemId) {
        String regexForItemID = "^I00-[0-9]{3}$"; // Assuming your item ID format is I00-001
        Pattern regexPattern = Pattern.compile(regexForItemID);
        Matcher regexMatcher = regexPattern.matcher(itemId);

        if (!regexMatcher.matches()) {
            return new SelectedCustomerItemAndOrderStatusCode(1, "Item ID is not valid");
        }

        return itemService.getItem(itemId);
    }

    // Delete an item by ID
    @DeleteMapping(value = "/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") String itemId) {
        try {
            itemService.deleteItem(itemId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get all items
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ItemDto> getAllItems() {
        return itemService.getAllItems();
    }

    // Update item by ID
    @PutMapping(value = "/{itemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateItem(@RequestBody ItemDto itemDto, @PathVariable("itemId") String itemId) {
        try {
            itemDto.setItemId(itemId);
            itemService.updateItem(itemId, itemDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (ItemNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Generate item ID
    @GetMapping("/generateItemId")
    public ResponseEntity<Map<String, String>> generateItemId() {
        String itemId = itemService.generateItemId();
        Map<String, String> response = new HashMap<>();
        response.put("id", itemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
