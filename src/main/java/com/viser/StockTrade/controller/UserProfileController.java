package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserProfileDto;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
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

    /**
     * Handles the request to update the user's profile.
     *
     * This method processes the update of the user's profile using the details provided in the {@link UserProfileDto} object.
     * The current user's username is retrieved from the {@link Principal} object. If the update is successful,
     * a success message is added to the redirect attributes, and the user is redirected to the profile page.
     * Validation errors are captured in the {@link BindingResult} object, and any issues encountered during the
     * update process, such as validation errors or I/O errors, are handled by throwing appropriate exceptions.
     *
     * @param userProfileDto the {@link UserProfileDto} object containing the updated profile details
     * @param result the {@link BindingResult} object that holds validation errors
     * @param principal the {@link Principal} object representing the currently authenticated user
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the profile page upon successful update of the user's profile
     */
    @PostMapping("/update")
    public String updateUserProfile(@Valid @ModelAttribute UserProfileDto userProfileDto, BindingResult result, Principal principal, RedirectAttributes ra) throws ValidationException, IOException {
        userProfileService.updateUserProfile(userProfileDto, principal.getName(), result);
        ra.addFlashAttribute("success", "Profile update successfully!");
        return "redirect:/profile-page";
    }
}
