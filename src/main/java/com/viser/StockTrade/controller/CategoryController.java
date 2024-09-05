package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.exceptions.ForeignKeyConstraintViolationException;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException, IOException {
        categoryService.add(categoryDto, result);
        ra.addFlashAttribute("success", "Category saved successfully.");
        return "redirect:/category-page";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, ForeignKeyConstraintViolationException, IOException {
        categoryService.delete(id);
        ra.addFlashAttribute("success", "The category with id " + id + " has been deleted.");
        return "redirect:/category-page";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute CategoryDto categoryDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException, IOException {
        categoryService.edit(id, categoryDto, result);
        ra.addFlashAttribute("success", "Category update successfully!");
        return "redirect:/category-page";
    }
}
