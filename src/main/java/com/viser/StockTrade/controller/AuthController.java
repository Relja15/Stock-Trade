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

    /**
     * Handles the login request for users.
     *
     * This method processes the login attempt by calling the {@link AuthService} to authenticate the user.
     * If the authentication is successful, the user is redirected to the index page.
     *
     * @param userDto the {@link UserDto} object containing the user's login credentials
     * @param response the {@link HttpServletResponse} object used to set cookies or headers for the login session
     * @return a redirect URL to the index page if login is successful
     */
    @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto, HttpServletResponse response) {
        authService.login(userDto, response);
        return "redirect:/index";
    }

    /**
     * Handles the user registration request.
     *
     * This method processes the registration attempt by calling the authentication service to register the user.
     * If the registration is successful, a success message is added to the redirect attributes and the user is redirected
     * to the users page. If there are validation errors or the username already exists, appropriate exceptions are thrown.
     *
     * @param userDto the {@link UserDto} object containing the user's registration details
     * @param result the {@link BindingResult} object that holds validation errors, if any
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the users page upon successful registration
     */
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserDto userDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NameExistException {
        authService.register(userDto, result);
        ra.addFlashAttribute("success", "New user saved successfully.");
        return "redirect:/users-page";
    }

    /**
     * Handles the user logout request.
     *
     * This method processes the logout request by calling the authentication service to log out the user.
     * After logging out, the user is redirected to the login page.
     *
     * @param response the {@link HttpServletResponse} object used to manage the logout process and session
     * @return a redirect URL to the login page after successful logout
     */
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        authService.logout(response);
        return "redirect:/login-page";
    }
}
