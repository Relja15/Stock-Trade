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

    /**
     * Retrieves purchase data grouped by category and returns it as a map.
     *
     * This method queries the `purchaseItemService` for the count of purchases grouped by category.
     * The results are then transformed into a `Map` where the key is the category name and the value
     * is the total number of purchases for that category.
     *
     * @return a {@link Map} where the key is the category name (a {@link String}) and the value
     *         is the total count of purchases (a {@link Long})
     */
    public Map<String, Long> chartPurchaseByCategoryData() {
        List<Object[]> results = purchaseItemService.countPurchasesByCategory();
        Map<String, Long> categoryData = new HashMap<>();
        results.forEach(result -> categoryData.put((String) result[0], (Long) result[1]));
        return categoryData;
    }

    /**
     * Retrieves purchase data grouped by supplier and returns it as a map.
     *
     * This method queries the `purchaseItemService` for the sum of quantities purchased grouped by supplier.
     * The results are then transformed into a `Map` where the key is the supplier name and the value
     * is the total quantity purchased from that supplier.
     *
     * @return a {@link Map} where the key is the supplier name (a {@link String}) and the value
     *         is the total quantity purchased from that supplier (a {@link Long})
     */
    public Map<String, Long> chartPurschaseBySupplierData() {
        List<Object[]> results = purchaseItemService.sumQuantotyBySupplier();
        Map<String, Long> supplierData = new HashMap<>();
        results.forEach(result -> supplierData.put((String) result[0], (Long) result[1]));
        return supplierData;
    }

    /**
     * Retrieves purchase data grouped by product and returns it as a map.
     *
     * This method queries the `purchaseItemService` for the sum of quantities purchased grouped by product.
     * The results are then transformed into a `Map` where the key is the product name and the value
     * is the total quantity purchased for that product.
     *
     * @return a {@link Map} where the key is the product name (a {@link String}) and the value
     *         is the total quantity purchased for that product (a {@link Long})
     */
    public Map<String, Long> chartPurchaseByProductData() {
        List<Object[]> results = purchaseItemService.sumQuantityByProduct();
        Map<String, Long> productData = new HashMap<>();
        results.forEach(result -> productData.put((String) result[0], (Long) result[1]));
        return productData;
    }
}
