package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto, HttpServletResponse response, Model model) {
        try {
            authService.login(userDto, response);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password.");
            return "login-page";
        }
    }

    @Transactional
    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto, RedirectAttributes ra) throws NameExistException {
        try {
            authService.register(userDto);
            ra.addFlashAttribute("success", "New user saved successfully.");
            return "redirect:/users-page";
        } catch (NameExistException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/add-user-page";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "redirect:/login-page";
    }
}
