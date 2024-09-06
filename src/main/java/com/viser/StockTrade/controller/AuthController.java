package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto, HttpServletResponse response) {
        authService.login(userDto, response);
        return "redirect:/index";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDto userDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        authService.register(userDto, result);
        ra.addFlashAttribute("success", "New user saved successfully.");
        return "redirect:/users-page";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "redirect:/login-page";
    }
}
