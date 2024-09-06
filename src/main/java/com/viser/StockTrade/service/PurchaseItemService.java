package com.viser.StockTrade.service;

import com.viser.StockTrade.repository.PurchaseItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseItemService {
    private final PurchaseItemRepository repo;

    public List<Object[]> countPurchasesByCategory() {
        return repo.countPurchasesByCategory();
    }

    public List<Object[]> sumQuantityByProduct() {
        return repo.sumQuantityByProduct();
    }

    public List<Object[]> sumQuantotyBySupplier() {
        return repo.sumQuantotyBySupplier();
    }
}
