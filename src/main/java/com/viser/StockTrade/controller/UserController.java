package com.viser.StockTrade.controller;

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
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra){
        try {
            userService.delete(id);
            ra.addFlashAttribute("message", "The user with id " + id + " has been deleted.");
        }catch (NotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/users-page";
    }

    @PostMapping("edit/{id}")
    public String editUser(@PathVariable("id") Integer id, String username, String password, RedirectAttributes ra){
        try {
            userService.edit(id, username, password);
            ra.addFlashAttribute("message", "User updated successfully.");
            return "redirect:/users-page";
        } catch (NotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/users-page";
        }
    }
}
