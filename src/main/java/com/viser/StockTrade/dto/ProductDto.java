package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import lombok.Data;

@Data
public class ProductDto {
    @BasicValidation(message = "Name must be at least {minLength} characters long and cannot be just spaces.")
    private String name;
    private String price;
    private String stockQty;
    private String categoryId;
    private String supplierId;
}
