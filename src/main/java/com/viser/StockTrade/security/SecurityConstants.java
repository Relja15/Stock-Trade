package com.viser.StockTrade.security;

public class SecurityConstants {
    public static final long jwtExpiration = 900000;

    public static final String[] authWhiteList = {
            "/login-page",
            "/api/auth/login",
            "/css/**",
            "/js/**",
            "/img/**",
            "/webjars/**",
            "/vendor/**"
    };

    public static final String[] adminRoot = {
            "/users-page",
            "/api/auth/register",
            "/add-user-page",
            "/api/user/**",
            "/edit-user-page"
    };

    public static final String[] userRoot = {
            "/api/auth/logout",
            "/category-page",
            "/index",
            "/product-page",
            "/purchases-page",
            "/supplier-page",
            "/profile-page",
            "/edit-profile",
            "/uploads/**",
            "/add-category-page",
            "/edit-category-page",
            "/api/category/**",
            "/add-supplier-page",
            "/edit-supplier-page",
            "/api/supplier/**",
            "/add-product-page",
            "/edit-product-page",
            "/add-purchase-page",
            "/api/charts/**",
            "/invoice-page"
    };
}
