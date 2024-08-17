package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findById(int id);

    boolean existsByName(String name);

    boolean existsById(int id);
}
