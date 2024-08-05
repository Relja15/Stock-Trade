package com.viser.StockTrade.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryDto {
    private MultipartFile categoryIcon;
    private String name;
    private String description;
}
