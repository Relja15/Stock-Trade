package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.CategoryDto;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public String add(@ModelAttribute CategoryDto categoryDto, RedirectAttributes ra) throws IOException {
        if(!categoryService.existByName(categoryDto.getName())){
            categoryService.add(categoryDto);
            ra.addFlashAttribute("success", "Category saved successfully.");
            return "redirect:/category-page";
        } else {
            ra.addFlashAttribute("error", "A category with this name already exists. Please choose a different name.");
            return "redirect:/add-category-page";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, IOException {
        boolean isDelete = categoryService.delete(id);
        if(isDelete){
            ra.addFlashAttribute("message", "The category with id " + id + " has been deleted.");
        } else {
            ra.addFlashAttribute("message", "The category cannot be deleted because it has associated products.");
        }
        return "redirect:/category-page";
    }

    @PostMapping("/edit/{id}")
    private String edit(@PathVariable("id") Integer id, @ModelAttribute CategoryDto categoryDto, RedirectAttributes ra) throws NotFoundException, IOException {
        try {
            categoryService.edit(id, categoryDto);
            ra.addFlashAttribute("message", "Category update successfully!");
            return "redirect:/category-page";
        }catch (Exception e){
            ra.addFlashAttribute("message", "Error updating category: " + e.getMessage());
            return "redirect:/edit-category-page";
        }
    }
}
