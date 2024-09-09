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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

@Controller
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Handles the request to add a new category.
     *
     * This method processes the addition of a new category by calling the category service to save the category details.
     * If the addition is successful, a success message is added to the redirect attributes and the user is redirected
     * to the category page. If there are validation errors, or if a category with the same name already exists,
     * appropriate exceptions are thrown.
     *
     * @param categoryDto the {@link CategoryDto} object containing the details of the category to be added
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the category page upon successful addition of the category
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute CategoryDto categoryDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException, IOException {
        categoryService.add(categoryDto, result);
        ra.addFlashAttribute("success", "Category saved successfully.");
        return "redirect:/category-page";
    }

    /**
     * Handles the request to delete a category by its ID.
     *
     * This method processes the deletion of a category by calling the category service to remove the category with
     * the specified ID. If the deletion is successful, a success message is added to the redirect attributes, and
     * the user is redirected to the category page. If the category is not found, or if there are issues such as
     * database constraints, appropriate exceptions are thrown.
     *
     * @param id the ID of the category to be deleted
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the category page upon successful deletion of the category
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, IOException, SQLIntegrityConstraintViolationException, ForeignKeyConstraintViolationException {
        categoryService.delete(id);
        ra.addFlashAttribute("success", "The category with id " + id + " has been deleted.");
        return "redirect:/category-page";
    }

    /**
     * Handles the request to edit an existing category.
     *
     * This method processes the update of a category by calling the category service to edit the details of the
     * category with the specified ID. If the update is successful, a success message is added to the redirect attributes,
     * and the user is redirected to the category page. If there are validation errors, if the category with the specified
     * ID is not found, or if a category with the same name already exists, appropriate exceptions are thrown.
     *
     * @param id the ID of the category to be updated
     * @param categoryDto the {@link CategoryDto} object containing the updated details of the category
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the category page upon successful update of the category
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute CategoryDto categoryDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException, IOException {
        categoryService.edit(id, categoryDto, result);
        ra.addFlashAttribute("success", "Category update successfully!");
        return "redirect:/category-page";
    }
}
