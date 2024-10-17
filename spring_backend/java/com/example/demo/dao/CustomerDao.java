package com.example.demo.dao;

import com.example.demo.entity.impl.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends JpaRepository<Customer,String> {
    @Query(value = "SELECT c.customerId FROM Customer c ORDER BY c.customerId DESC")
    String findLastCustomerId();
}
