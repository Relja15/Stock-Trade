package com.viser.StockTrade.controller;

import com.viser.StockTrade.service.ChartService;
import com.viser.StockTrade.service.PurchaseItemService;
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

    @GetMapping("/purchase-category-data")
    public Map<String, Long> getPurchaseByCategory() {
        return chartService.chartPurchaseByCategoryData();
    }

    @GetMapping("/purchase-supplier-data")
    public Map<String, Long> getPurchaseBySupplier() { return chartService.chartPurschaseBySupplierData(); }

    @GetMapping("/purchase-product-data")
    public Map<String, Long> getPurchasesByProduct() {
        return chartService.chartPurchaseByProductData();
    }
}
