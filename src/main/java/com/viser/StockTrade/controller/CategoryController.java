package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.exceptions.ForeignKeyConstraintViolationException;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Transactional
    @PostMapping("/add")
    public String add(@ModelAttribute CategoryDto categoryDto, RedirectAttributes ra) throws NameExistException, IOException {
        try {
            categoryService.add(categoryDto);
            ra.addFlashAttribute("success", "Category saved successfully.");
            return "redirect:/category-page";
        } catch (NameExistException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/add-category-page";
        }
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, ForeignKeyConstraintViolationException, IOException {
        try {
            categoryService.delete(id);
            ra.addFlashAttribute("success", "The category with id " + id + " has been deleted.");
        } catch (NotFoundException | ForeignKeyConstraintViolationException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/category-page";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @ModelAttribute CategoryDto categoryDto, RedirectAttributes ra) throws NotFoundException, IOException {
        try {
            categoryService.edit(id, categoryDto);
            ra.addFlashAttribute("success", "Category update successfully!");
            return "redirect:/category-page";
        } catch (NotFoundException | IOException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/edit-category-page";
        }
    }
}
