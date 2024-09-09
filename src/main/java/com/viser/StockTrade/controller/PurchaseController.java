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

    /**
     * Handles the request to add a new purchase.
     *
     * This method processes the addition of a new purchase by calling the purchase service to save the details provided
     * in the {@link PurchaseDto} object. If the addition is successful, a success message is added to the redirect attributes,
     * and the user is redirected to the purchases page. If there are validation errors, they are captured in the
     * {@link BindingResult} object, and a {@link ValidationException} is thrown if necessary.
     *
     * @param purchaseDto the {@link PurchaseDto} object containing the details of the purchase to be added
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the purchases page upon successful addition of the purchase
     */
    @Transactional
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute PurchaseDto purchaseDto, BindingResult result, RedirectAttributes ra) throws ValidationException {
        purchaseService.add(purchaseDto, result);
        ra.addFlashAttribute("success", "Purchase saved successfully.");
        return "redirect:/purchases-page";
    }

    /**
     * Retrieves the details of a specific purchase by its ID.
     *
     * This method fetches the details of the purchase associated with the given ID from the purchase service
     * and returns them encapsulated in a {@link ResponseEntity} with an HTTP 200 OK status. The response body
     * contains the purchase details as a {@link PurchaseDto} object.
     *
     * @param id the ID of the purchase to be retrieved
     * @return a {@link ResponseEntity} containing the {@link PurchaseDto} object with the purchase details
     */
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDto> getPurchase(@PathVariable("id") Integer id) {
        PurchaseDto purchaseDto = purchaseService.getPurchaseInDto(id);
        return ResponseEntity.ok(purchaseDto);
    }

}
