package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Supplier findById(int id);
    boolean existsByName(String name);
}
