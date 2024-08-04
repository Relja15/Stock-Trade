package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends JpaRepository<Category, Integer> {
}
