package com.viser.StockTrade.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/login-page")
    public String showLoginPage() {
        return "login-page";
    }

    @GetMapping("/index")
    public  String showIndexPage() {return "index";}

    @GetMapping("/category-page")
    public  String showCategoryPage() { return  "category-page";}

    @GetMapping("/product-page")
    public  String showProductPage() { return  "product-page";}

    @GetMapping("/customer-page")
    public String showCustomerPage() {return "customer-page";}

    @GetMapping("/purchases-page")
    public String showPurchasesPage() {return "purchases-page";}

    @GetMapping("/sales-page")
    public String showSalesPage() {return "sales-page";}

    @GetMapping("/supplier-page")
    public String showSupplierPage() {return "supplier-page";}

    @GetMapping("/roles-page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRolesPage() {return "roles-page";}

    @GetMapping("/users-page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUsersPage() {return "users-page";}
}
