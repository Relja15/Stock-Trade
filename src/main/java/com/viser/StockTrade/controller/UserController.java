package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;


    @Transactional
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) {
        try {
            userService.delete(id);
            ra.addFlashAttribute("success", "The user with id " + id + " has been deleted.");
        } catch (NotFoundException | IOException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users-page";
    }

    @Transactional
    @PostMapping("edit/{id}")
    public String editUser(@PathVariable("id") Integer id, @ModelAttribute UserDto userDto, RedirectAttributes ra) {
        try {
            userService.edit(id, userDto);
            ra.addFlashAttribute("success", "User updated successfully.");
            return "redirect:/users-page";
        } catch (NotFoundException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/users-page";
        }
    }
}
