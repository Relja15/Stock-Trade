package com.viser.StockTrade.controller;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.NotFoundException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Handles the request to delete a user by its ID.
     *
     * This method processes the deletion of a user with the specified ID by calling the user service
     * to remove the user. If the deletion is successful, a success message is added to the redirect attributes,
     * and the user is redirected to the users page. If the user with the specified ID is not found or if there are
     * issues during the deletion process, appropriate exceptions are thrown.
     *
     * @param id the ID of the user to be deleted
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the users page upon successful deletion of the user
     */
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes ra) throws NotFoundException, IOException {
        userService.delete(id);
        ra.addFlashAttribute("success", "The user with id " + id + " has been deleted.");
        return "redirect:/users-page";
    }

    /**
     * Handles the request to update an existing user.
     *
     * This method processes the update of a user with the specified ID by calling the user service
     * to modify the details of the user as provided in the {@link UserDto} object. If the update is successful,
     * a success message is added to the redirect attributes, and the user is redirected to the users page.
     * Validation errors are captured in the {@link BindingResult} object, and appropriate exceptions are thrown
     * if the user with the specified ID is not found or if a user with the same name already exists.
     *
     * @param id the ID of the user to be updated
     * @param userDto the {@link UserDto} object containing the updated details of the user
     * @param result the {@link BindingResult} object that holds validation errors
     * @param ra the {@link RedirectAttributes} object used to pass flash attributes to the redirected page
     * @return a redirect URL to the users page upon successful update of the user
     */
    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Integer id, @Valid @ModelAttribute UserDto userDto, BindingResult result, RedirectAttributes ra) throws ValidationException, NotFoundException, NameExistException {
        userService.edit(id, userDto, result);
        ra.addFlashAttribute("success", "User updated successfully.");
        return "redirect:/users-page";
    }
}
