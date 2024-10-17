package com.example.demo.dao;

import com.example.demo.entity.impl.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao extends JpaRepository<Item,String> {
    @Query(value = "SELECT i.itemId FROM Item i ORDER BY i.itemId DESC")
    String findLastItemId();
}
