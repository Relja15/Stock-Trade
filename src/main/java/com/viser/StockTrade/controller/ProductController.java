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

    /**
     * Handles the request to add a new product.
     *
     * This method processes the addition of a new product by calling the product service to save the product details.
     * If the addition is successful, a success message is added to the redirect attributes, and the user is redirected
     * to the product page. If there are validation errors or if a product with the same name already exists,
     * appropriate exceptions are thrown.
     *
     * @param productDto the {@link ProductDto} object containing the details of the product to be added
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the product page upon successful addition of the product
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute ProductDto productDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        productService.add(productDto, result);
        ra.addFlashAttribute("success", "Product saved successfully.");
        return "redirect:/product-page";
    }

    /**
     * Handles the request to delete a product by its ID.
     *
     * This method processes the deletion of a product by calling the product service to remove the product
     * with the specified ID. If the deletion is successful, a success message is added to the redirect attributes,
     * and the user is redirected to the product page. If the product with the specified ID is not found,
     * an appropriate exception is thrown.
     *
     * @param id the ID of the product to be deleted
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the product page upon successful deletion of the product
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException {
        productService.delete(id);
        ra.addFlashAttribute("success", "The product with id " + id + " has been deleted.");
        return "redirect:/product-page";
    }

    /**
     * Handles the request to update an existing product.
     *
     * This method processes the update of a product by calling the product service to modify the details of the
     * product with the specified ID. If the update is successful, a success message is added to the redirect attributes,
     * and the user is redirected to the product page. If there are validation errors, if the product with the specified
     * ID is not found, or if a product with the same name already exists, appropriate exceptions are thrown.
     *
     * @param id the ID of the product to be updated
     * @param productDto the {@link ProductDto} object containing the updated details of the product
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the product page upon successful update of the product
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute ProductDto productDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        productService.edit(id, productDto, result);
        ra.addFlashAttribute("success", "Product update successfully!");
        return "redirect:/product-page";
    }
}
