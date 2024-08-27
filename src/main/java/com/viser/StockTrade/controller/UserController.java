package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, IOException {
        userService.delete(id);
        ra.addFlashAttribute("success", "The user with id " + id + " has been deleted.");
        return "redirect:/users-page";
    }

    @Transactional
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, @Valid @ModelAttribute UserDto userDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        userService.edit(id, userDto, result);
        ra.addFlashAttribute("success", "User updated successfully.");
        return "redirect:/users-page";
    }
}
