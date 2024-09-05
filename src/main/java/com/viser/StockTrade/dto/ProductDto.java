package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import lombok.Data;

@Data
public class ProductDto {
    @BasicValidation(message = "Name must be at least {minLength} characters long and cannot be just spaces.")
    private String name;
    private double price;
    private int stockQty;
    private int categoryId;
    private int supplierId;
}
