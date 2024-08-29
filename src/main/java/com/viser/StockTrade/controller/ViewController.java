package com.viser.StockTrade.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viser.StockTrade.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ViewController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final PurchaseService purchaseService;

    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login-page";
    }

    @GetMapping("/index")
    public String showIndexPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "index";
    }

    @GetMapping("/category-page")
    public String showCategoryPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categories", categoryService.getAll());
        return "category-page";
    }

    @GetMapping("/product-page")
    public String showProductPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("products", productService.getAll());
        return "product-page";
    }

    @GetMapping("/customer-page")
    public String showCustomerPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("customers", customerService.getAll());
        return "customer-page";
    }

    @GetMapping("/purchases-page")
    public String showPurchasesPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("purchases", purchaseService.getAll());
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
        model.addAttribute("suppliers", supplierService.getAll());
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
        model.addAttribute("users", userService.getAll());
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
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showAddUser(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-user-page";
    }

    @GetMapping("/edit-user-page/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showEditUserPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
        return "edit-user-page";
    }

    @GetMapping("/add-category-page")
    public String showAddCategory(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-category-page";
    }

    @GetMapping("/edit-category-page/{id}")
    public String showEditCategoryPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("category", categoryService.getById(id));
        return "/edit-category-page";
    }

    @GetMapping("/add-supplier-page")
    public String showAddSupplier(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-supplier-page";
    }

    @GetMapping("/edit-supplier-page/{id}")
    public String showEditSupplierCategory(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("supplier", supplierService.getById(id));
        return "edit-supplier-page";
    }

    @GetMapping("/add-product-page")
    public String showAddProductPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("suppliers", supplierService.getAll());
        return "add-product-page";
    }

    @GetMapping("/edit-product-page/{id}")
    public String showEditProductPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("suppliers", supplierService.getAll());
        model.addAttribute("product", productService.getById(id));
        return "edit-product-page";
    }

    @GetMapping("/add-customer-page")
    public String showAddCustomerPage(Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        return "add-customer-page";
    }

    @GetMapping("/edit-customer-page/{id}")
    public String showEditCustomerPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("customer", customerService.getById(id));
        return "edit-customer-page";
    }

    @GetMapping("/add-purchase-page")
    public String showAddPurchasePage(Model model, Principal principal) throws JsonProcessingException {
        userService.getAllUsersDataInModel(model, principal.getName());
        model.addAttribute("suppliers", supplierService.getAll());
        model.addAttribute("products", productService.getProductListInJson());
        return "add-purchase-page";
    }
}
