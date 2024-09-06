package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.ProductDto;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute ProductDto productDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        productService.add(productDto, result);
        ra.addFlashAttribute("success", "Product saved successfully.");
        return "redirect:/product-page";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException {
        productService.delete(id);
        ra.addFlashAttribute("success", "The product with id " + id + " has been deleted.");
        return "redirect:/product-page";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute ProductDto productDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        productService.edit(id, productDto, result);
        ra.addFlashAttribute("success", "Product update successfully!");
        return "redirect:/product-page";
    }
}
