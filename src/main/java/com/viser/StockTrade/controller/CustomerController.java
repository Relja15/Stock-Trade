package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.CustomerDto;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Transactional
    @PostMapping("/add")
    public String add(@Valid @ModelAttribute CustomerDto customerDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        customerService.add(customerDto, result);
        ra.addFlashAttribute("success", "Customer saved successfully.");
        return "redirect:/customer-page";
    }

    @Transactional
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException {
        customerService.delete(id);
        ra.addFlashAttribute("success", "The customer with id " + id + " has been deleted.");
        return "redirect:/customer-page";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, @Valid @ModelAttribute CustomerDto customerDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        customerService.edit(id, customerDto, result);
        ra.addFlashAttribute("success", "Supplier update successfully!");
        return "redirect:/customer-page";
    }
}
