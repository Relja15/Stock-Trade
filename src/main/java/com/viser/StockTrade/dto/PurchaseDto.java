package com.viser.StockTrade.dto;

import com.viser.StockTrade.validation.BasicValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseDto {
    private int id;
    private String supplier;
    @Future(message = "The delivery date cannot be in the past. Please enter a date in the future.")
    private LocalDate date;
    @NotNull(message = "Purchase items cannot be empty")
    @NotEmpty(message = "Purchase items cannot be empty")
    @Valid
    private List<PurchasesItemDto> purchaseItems;
    private double totalAmount;
}
