package com.viser.StockTrade.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class PurchasesItemDto {
    private String product;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
    private int price;

    public PurchasesItemDto(String product, int quantity, int price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}