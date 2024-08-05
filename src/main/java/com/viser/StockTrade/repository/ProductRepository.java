package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategoryId(int categoryId);
}
