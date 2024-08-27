package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("/api/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @Transactional
    @PostMapping("/update")
    public String updateUserProfile(@Valid @ModelAttribute UserProfileDto userProfileDto, BindingResult result, Principal principal, RedirectAttributes ra) throws ValidationException, IOException {
        userProfileService.updateUserProfile(userProfileDto, principal.getName(), result);
        ra.addFlashAttribute("success", "Profile update successfully!");
        return "redirect:/profile-page";
    }
}
