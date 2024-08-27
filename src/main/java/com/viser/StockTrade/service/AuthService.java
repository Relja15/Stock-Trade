package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.exceptions.ExceptionHelper;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.security.JwtGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    public void login(UserDto userDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        Cookie cookie = getCookie(token);
        response.addCookie(cookie);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
    }

    public void register(UserDto userDto, BindingResult result) throws ValidationException, NameExistException {
        ExceptionHelper.throwValidationException(result, "/add-user-page");
        if (userService.existByUsername(userDto.getUsername())) {
            throw new NameExistException("A user with this username already exists. Please choose a different username.", "/add-user-page");
        }
        User user = new User();
        setUserFields(user, userDto);
        userService.save(user);
    }

    private Cookie getCookie(String token) {
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(900);
        cookie.setPath("/");
        return cookie;
    }

    private void setUserFields(User user, UserDto userDto) {
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roles = roleService.getByName("USER");
        user.setRoles(Collections.singletonList(roles));
    }
}
