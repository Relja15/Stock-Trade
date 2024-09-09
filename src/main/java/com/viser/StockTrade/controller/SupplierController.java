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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    /**
     * Handles the request to add a new supplier.
     *
     * This method processes the addition of a new supplier by calling the supplier service to save the details
     * provided in the {@link SupplierDto} object. If the addition is successful, a success message is added to
     * the redirect attributes, and the user is redirected to the supplier page. Validation errors are captured
     * in the {@link BindingResult} object, and appropriate exceptions are thrown if validation fails or if a supplier
     * with the same name already exists.
     *
     * @param supplierDto the {@link SupplierDto} object containing the details of the supplier to be added
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the supplier page upon successful addition of the supplier
     */
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute SupplierDto supplierDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        supplierService.add(supplierDto, result);
        ra.addFlashAttribute("success", "Supplier saved successfully.");
        return "redirect:/supplier-page";
    }

    /**
     * Handles the request to delete a supplier by its ID.
     *
     * This method processes the deletion of a supplier with the specified ID by calling the supplier service
     * to remove the supplier. If the deletion is successful, a success message is added to the redirect attributes,
     * and the user is redirected to the supplier page. If the supplier with the specified ID is not found or if
     * there are any foreign key constraints preventing the deletion, appropriate exceptions are thrown.
     *
     * @param id the ID of the supplier to be deleted
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the supplier page upon successful deletion of the supplier
     */
    @DeleteMapping("delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, ForeignKeyConstraintViolationException {
        supplierService.delete(id);
        ra.addFlashAttribute("success", "The supplier with id " + id + " has been deleted.");
        return "redirect:/supplier-page";
    }

    /**
     * Handles the request to update an existing supplier.
     *
     * This method processes the update of a supplier with the specified ID by calling the supplier service
     * to modify the details of the supplier as provided in the {@link SupplierDto} object. If the update is successful,
     * a success message is added to the redirect attributes, and the user is redirected to the supplier page.
     * Validation errors are captured in the {@link BindingResult} object, and appropriate exceptions are thrown
     * if the supplier with the specified ID is not found or if a supplier with the same name already exists.
     *
     * @param id the ID of the supplier to be updated
     * @param supplierDto the {@link SupplierDto} object containing the updated details of the supplier
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the supplier page upon successful update of the supplier
     */
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute SupplierDto supplierDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        supplierService.edit(id, supplierDto, result);
        ra.addFlashAttribute("success", "Supplier update successfully!");
        return "redirect:/supplier-page";
    }
}
