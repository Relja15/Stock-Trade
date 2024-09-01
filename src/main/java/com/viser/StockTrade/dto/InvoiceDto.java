package com.viser.StockTrade.dto;

import lombok.Data;

@Data
public class InvoiceDto {
    private int quantity;
    private String product;
    private double unitPrice;
    private double lineTotal;
}
