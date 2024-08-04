package com.viser.StockTrade.controller;

import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.entity.UserProfile;
import com.viser.StockTrade.exceptions.UserNotFoundException;
import com.viser.StockTrade.service.UserProfileService;
import com.viser.StockTrade.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class ViewController {
    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserService userService;

    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login-page";
    }

    @GetMapping("/index")
    public  String showIndexPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "index";
    }

    @GetMapping("/category-page")
    public  String showCategoryPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return  "category-page";
    }

    @GetMapping("/product-page")
    public  String showProductPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return  "product-page";
    }

    @GetMapping("/customer-page")
    public String showCustomerPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "customer-page";
    }

    @GetMapping("/purchases-page")
    public String showPurchasesPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "purchases-page";
    }

    @GetMapping("/sales-page")
    public String showSalesPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "sales-page";
    }

    @GetMapping("/supplier-page")
    public String showSupplierPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "supplier-page";
    }

    @GetMapping("/roles-page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRolesPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "roles-page";
    }

    @GetMapping("/users-page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUsersPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("users", userService.getAllUsers());
        return "users-page";
    }

    @GetMapping("/profile-page")
    public String showUserProfile(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "profile-page";
    }

    @GetMapping("/edit-profile")
    public String shoeEditProfilePage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "edit-profile";
    }

    @GetMapping("/add-user-page")
    public String showAddUser(Model model, Principal principal){
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-user-page";
    }

    @GetMapping("/edit-user-page/{id}")
    public String showEditPage(@PathVariable("id") Integer id, Model model, Principal principal, RedirectAttributes ra){
        try {
            userService.getAllUsersDataInModel(model, principal.getName());
            User user = userService.getById(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");

            return "edit-user-page";
        } catch (UserNotFoundException e){
            ra.addFlashAttribute("message", e.getMessage());
            return "redirect:/users-page";
        }
    }
}
