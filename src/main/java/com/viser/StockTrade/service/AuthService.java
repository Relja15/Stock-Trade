package com.viser.StockTrade.service;

import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.security.JwtGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private JwtGenerator jwtGenerator;


    public void login(String username, String password, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void logout(HttpServletResponse response){
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
    }

    public void register(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode((password)));
        Role roles = roleService.getByName("USER");
        user.setRoles(Collections.singletonList(roles));
        userService.save(user);
    }
}
