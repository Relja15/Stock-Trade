package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import com.viser.StockTrade.validation.ValidValue;
import lombok.Data;

@Data
public class SupplierDto {
    @BasicValidation(message = "Name must be at least {minLength} characters long and cannot be just spaces.")
    private String name;
    @BasicValidation(minLength = 5, message = "Address must be at least {minLength} characters long and cannot be just spaces.")
    private String address;
    @BasicValidation(minLength = 5, message = "Email must be at least {minLength} characters long and cannot be just spaces.")
    private String email;
    @ValidValue
    private String phone;
}
