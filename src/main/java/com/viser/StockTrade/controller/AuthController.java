package com.viser.StockTrade.controller;

import com.viser.StockTrade.service.AuthService;
import com.viser.StockTrade.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpServletResponse response, Model model) {
        try {
            authService.login(username, password, response);
            return "redirect:/index";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid username or password.");
            return "login-page";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "redirect:/login-page";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model, RedirectAttributes ra) {
        if (!userService.existByUsername(username)) {
            authService.register(username, password);
            ra.addFlashAttribute("success", "New user saved successfully.");
            return "redirect:/users-page";
        }
        ra.addFlashAttribute("error", "Username is taken!");
        return "redirect:/add-user-page";
    }
}
