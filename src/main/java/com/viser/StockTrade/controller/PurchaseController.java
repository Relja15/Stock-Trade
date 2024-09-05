package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.PurchaseDto;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @Transactional
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute PurchaseDto purchaseDto, BindingResult result, RedirectAttributes ra) throws ValidationException {
        purchaseService.add(purchaseDto, result);
        ra.addFlashAttribute("success", "Purchase saved successfully.");
        return "redirect:/purchases-page";
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDto> getPurchase(@PathVariable("id") Integer id) {
        PurchaseDto purchaseDto = purchaseService.getPurchaseInDto(id);
        return ResponseEntity.ok(purchaseDto);
    }

}
