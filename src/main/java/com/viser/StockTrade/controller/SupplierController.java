package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.SupplierDto;
import com.viser.StockTrade.exceptions.ForeignKeyConstraintViolationException;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @Transactional
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute SupplierDto supplierDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        supplierService.add(supplierDto, result);
        ra.addFlashAttribute("success", "Supplier saved successfully.");
        return "redirect:/supplier-page";
    }

    @Transactional
    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, ForeignKeyConstraintViolationException {
        supplierService.delete(id);
        ra.addFlashAttribute("success", "The supplier with id " + id + " has been deleted.");
        return "redirect:/supplier-page";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute SupplierDto supplierDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        supplierService.edit(id, supplierDto, result);
        ra.addFlashAttribute("success", "Supplier update successfully!");
        return "redirect:/supplier-page";
    }
}
