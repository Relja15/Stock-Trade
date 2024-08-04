package com.viser.StockTrade.security;

public class SecurityConstants {
    public static final long jwtExpiration = 1200000;

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
            "/roles-page",
            "/users-page",
            "/api/auth/register",
            "/add-user-page",
            "/api/user/**",
            "/edit-user-page"
    };

    public static final String[] userRoot = {
            "/api/auth/logout",
            "/category-page",
            "/customer-page",
            "/index",
            "/product-page",
            "/purchases-page",
            "/sales-page",
            "/supplier-page",
            "/profile-page",
            "/edit-profile",
            "/uploads/**"
    };
}
