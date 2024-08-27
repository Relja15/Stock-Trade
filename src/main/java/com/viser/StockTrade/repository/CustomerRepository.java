package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findById(int id);

    boolean existsByName(String name);

}
