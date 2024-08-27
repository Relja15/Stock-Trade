package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDto {
    @BasicValidation(message = "Name must be at least {minLength} characters long and cannot be just spaces.")
    private String name;
    @BasicValidation(minLength = 5, message = "Address must be at least {minLength} characters long and cannot be just spaces.")
    private String address;
    @BasicValidation(minLength = 5, message = "Email must be at least {minLength} characters long and cannot be just spaces.")
    private String email;
    @Pattern(regexp = "\\d+", message = "The field must contain only numbers.")
    @Size(min = 8, max = 11, message = "The field must be between 8 and 11 characters long and contain only numbers")
    private String phone;
}
