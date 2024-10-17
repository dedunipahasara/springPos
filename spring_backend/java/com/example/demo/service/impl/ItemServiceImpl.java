package com.example.demo.service.impl;

import com.example.demo.customStatusCode.SelectedCustomerItemAndOrderStatusCode;
import com.example.demo.dao.ItemDao;
import com.example.demo.dto.ItemStatus;
import com.example.demo.dto.impl.ItemDto;
import com.example.demo.entity.impl.Item;
import com.example.demo.exception.DataPersistException;
import com.example.demo.exception.ItemNotFoundException;
import com.example.demo.service.ItemService;
import com.example.demo.util.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private Mapping mapping;
    @Override
    public void saveItem(ItemDto itemDTO) {
        Item savedItem =
                itemDao.save(mapping.toItemEntity(itemDTO));
        if (savedItem == null) {
            throw new DataPersistException("Item not saved");
        }
    }

    @Override
    public List<ItemDto> getAllItems() {
        List<Item> allItems = itemDao.findAll();
        return mapping.asItemDTOList(allItems);    }

    @Override
    public ItemStatus getItem(String itemId) {
        if(itemDao.existsById(itemId)){
            Item selectedItem = itemDao.getReferenceById(itemId);
            return mapping.toItemDTO(selectedItem);
        }else {
            return new SelectedCustomerItemAndOrderStatusCode(2, "Item with id " + itemId + " not found");
        }
    }

    @Override
    public void deleteItem(String itemId) {
        Optional<Item> existedItem = itemDao.findById(itemId);
        if(!existedItem.isPresent()){
            throw new ItemNotFoundException( "Item with id " + itemId + " not found");
        }else {
            itemDao.deleteById(itemId);
        }

    }

    @Override
    public void updateItem(String itemId, ItemDto itemDTO) {
        Optional<Item> tmpItem = itemDao.findById(itemId);
        if(tmpItem.isPresent()) {
            tmpItem.get().setItemId(itemDTO.getItemId());
            tmpItem.get().setItemName(itemDTO.getItemName());
            tmpItem.get().setItemQty(itemDTO.getItemQty());
            tmpItem.get().setItemPrice((itemDTO.getItemPrice()));
        }


    }

    @Override
    public String generateItemId() {
        String lastItemId = itemDao.findLastItemId();  // Get the last customer ID from the database
        if (lastItemId == null || lastItemId.isEmpty()) {
            return "I00-001";  // If no customers exist, start with C00-001
        }

        // Extract the numeric part of the last customer ID
        int lastId = Integer.parseInt(lastItemId.split("-")[1]);

        // Increment the numeric part
        lastId += 1;

        // Return the next customer ID in the format C00-XXX
        return String.format("I00-%03d", lastId);
    }

}
