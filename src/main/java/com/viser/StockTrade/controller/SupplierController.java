package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.SupplierDto;
import com.viser.StockTrade.exceptions.ForeignKeyConstraintViolationException;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @Transactional
    @PostMapping("/add")
    public String add(@ModelAttribute SupplierDto supplierDto, RedirectAttributes ra) throws NameExistException {
        try {
            supplierService.add(supplierDto);
            ra.addFlashAttribute("success", "Supplier saved successfully.");
            return "redirect:/supplier-page";
        } catch (NameExistException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/add-supplier-page";
        }
    }

    @Transactional
    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, ForeignKeyConstraintViolationException {
        try {
            supplierService.delete(id);
            ra.addFlashAttribute("success", "The supplier with id " + id + " has been deleted.");
        } catch (NotFoundException | ForeignKeyConstraintViolationException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/supplier-page";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute SupplierDto supplierDto, RedirectAttributes ra) throws NotFoundException {
        try {
            supplierService.edit(id, supplierDto);
            ra.addFlashAttribute("success", "Supplier update successfully!");
            return "redirect:/supplier-page";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/edit-supplier-page";
        }
    }
}
