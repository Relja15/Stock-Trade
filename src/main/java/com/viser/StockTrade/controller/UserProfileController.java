package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/api/user-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;
    @PostMapping("/update")
    public String updateUserProfile(@ModelAttribute UserProfileDto userProfileDto, Principal principal, RedirectAttributes ra) {
        try {
            userProfileService.updateUserProfile(userProfileDto, principal.getName());
            ra.addFlashAttribute("successUpdate", "Profile update successfully!");
            return "redirect:/profile-page";
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "Error updating profile: " + e.getMessage());
            return "redirect:/edit-profile";
        }
    }
}
