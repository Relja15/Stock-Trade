package com.viser.StockTrade.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasesItemDto {
    private String product;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    private double price;
}