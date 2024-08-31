package com.viser.StockTrade.repository;

import com.viser.StockTrade.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Integer> {

    @Query("SELECT p.productCategory, SUM(p.quantity) FROM PurchaseItem p GROUP BY p.productCategory")
    List<Object[]> countPurchasesByCategory();

    @Query("SELECT p.productName, SUM(p.quantity) FROM PurchaseItem p GROUP BY p.productName")
    List<Object[]> sumQuantityByProduct();

    @Query("SELECT p.purchase.supplierName, SUM(p.quantity) FROM PurchaseItem p GROUP BY p.purchase.supplierName")
    List<Object[]> sumQuantotyBySupplier();
}
