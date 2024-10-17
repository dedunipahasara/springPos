package com.example.demo.util;

import com.example.demo.dto.impl.CustomerDto;
import com.example.demo.dto.impl.ItemDto;
import com.example.demo.entity.impl.Customer;
import com.example.demo.entity.impl.Item;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class Mapping {
    @Autowired
    private ModelMapper modelMapper;
    //for user mapping
    public Customer toCustomerEntity(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }
    public CustomerDto toCustomerDTO(Customer customerEntity) {
        return modelMapper.map(customerEntity, CustomerDto.class);
    }
    public List<CustomerDto> asCustomerDTOList(List<Customer> customerEntities) {
        return modelMapper.map(customerEntities, new TypeToken<List<CustomerDto>>() {}.getType());
    }
    public List<ItemDto> asItemDTOList(List<Item> itemEntities) {
        return modelMapper.map(itemEntities, new TypeToken<List<ItemDto>>() {}.getType());
    }
    public Item toItemEntity(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }
    public ItemDto toItemDTO(Item itemEntity) {
        return modelMapper.map(itemEntity, ItemDto.class);
    }

}
