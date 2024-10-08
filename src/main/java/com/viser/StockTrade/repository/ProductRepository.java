package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findById(int id);

    Product findByName(String name);

    boolean existsByName(String name);

    boolean existsBySupplierId(int id);

    long count();
}
