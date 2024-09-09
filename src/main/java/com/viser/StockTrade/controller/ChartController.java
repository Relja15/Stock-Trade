package com.viser.StockTrade.controller;

import com.viser.StockTrade.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/charts")
@RequiredArgsConstructor
public class ChartController {
    private final ChartService chartService;

    /**
     * Retrieves purchase data grouped by category.
     *
     * This method fetches purchase data categorized by different categories using the chart service.
     * It returns a map where the keys are category names and the values are the corresponding purchase totals.
     *
     * @return a {@link Map} where each key is a category name and each value is the total amount of purchases for that category
     */
    @GetMapping("/purchase-category-data")
    public Map<String, Long> getPurchaseByCategory() {
        return chartService.chartPurchaseByCategoryData();
    }

    /**
     * Retrieves purchase data grouped by supplier.
     *
     * This method fetches purchase data categorized by different suppliers using the chart service.
     * It returns a map where the keys are supplier names and the values are the corresponding purchase totals.
     *
     * @return a {@link Map} where each key is a supplier name and each value is the total amount of purchases for that supplier
     */
    @GetMapping("/purchase-supplier-data")
    public Map<String, Long> getPurchaseBySupplier() {
        return chartService.chartPurschaseBySupplierData();
    }

    /**
     * Retrieves purchase data grouped by product.
     *
     * This method fetches purchase data categorized by different products using the chart service.
     * It returns a map where the keys are product names and the values are the corresponding purchase totals.
     *
     * @return a {@link Map} where each key is a product name and each value is the total amount of purchases for that product
     */
    @GetMapping("/purchase-product-data")
    public Map<String, Long> getPurchasesByProduct() {
        return chartService.chartPurchaseByProductData();
    }
}
