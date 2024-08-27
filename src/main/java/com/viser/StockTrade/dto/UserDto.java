package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import lombok.Data;

@Data
public class UserDto {
    @BasicValidation(message = "Username must be at least {minLength} characters long and cannot be just spaces.")
    private String username;
    @BasicValidation(minLength = 5, message = "Password must be at least {minLength} characters long and cannot be just spaces.")
    private String password;
}
