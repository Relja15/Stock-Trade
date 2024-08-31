package com.viser.StockTrade.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChartService {
    private final PurchaseItemService purchaseItemService;

    public Map<String, Long> chartPurchaseByCategoryData(){
        List<Object[]> results = purchaseItemService.countPurchasesByCategory();
        Map<String, Long> categoryData = new HashMap<>();
        results.forEach(result -> categoryData.put((String) result[0], (Long) result[1]));
        return categoryData;
    }

    public Map<String, Long> chartPurschaseBySupplierData(){
        List<Object[]> results = purchaseItemService.sumQuantotyBySupplier();
        Map<String, Long> supplierData = new HashMap<>();
        results.forEach(result -> supplierData.put((String) result[0], (Long) result[1]));
        return supplierData;
    }

    public Map<String, Long> chartPurchaseByProductData(){
        List<Object[]> results = purchaseItemService.sumQuantityByProduct();
        Map<String, Long> productData = new HashMap<>();
        results.forEach(result -> productData.put((String) result[0], (Long) result[1]));
        return productData;
    }


}
