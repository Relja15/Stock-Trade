package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.SupplierDto;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @PostMapping("/add")
    public String add(@ModelAttribute SupplierDto supplierDto, RedirectAttributes ra){
        if(!supplierService.existByName(supplierDto.getName())){
            supplierService.add(supplierDto);
            ra.addFlashAttribute("success", "Supplier saved successfully.");
        } else {
            ra.addFlashAttribute("error", "A supplier with this name already exists. Please choose a different name.");
        }
        return "redirect:/supplier-page";
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException {
        boolean isDelete = supplierService.delete(id);
        if(isDelete){
            ra.addFlashAttribute("message", "The supplier with id " + id + " has been deleted.");
        } else {
            ra.addFlashAttribute("message", "The category cannot be deleted because it has associated products.");
        }
        return "redirect:/supplier-page";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute SupplierDto supplierDto, RedirectAttributes ra) throws NotFoundException{
        try {
            supplierService.edit(id, supplierDto);
            ra.addFlashAttribute("message", "Supplier update successfully!");
            return "redirect:/supplier-page";
        }catch (Exception e){
            ra.addFlashAttribute("message", "Error updating supplier: " + e.getMessage());
            return "redirect:/edit-supplier-page";
        }
    }
}
