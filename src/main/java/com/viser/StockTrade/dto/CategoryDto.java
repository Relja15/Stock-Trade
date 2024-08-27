package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryDto {
    private MultipartFile icon;
    @BasicValidation(message = "Name must be at least {minLength} characters long and cannot be just spaces.")
    private String name;
    @BasicValidation(minLength = 10, message = "Description must be at least {minLength} characters long and cannot be just spaces.")
    private String description;
}
