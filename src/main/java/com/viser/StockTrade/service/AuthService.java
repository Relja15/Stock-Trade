package com.viser.StockTrade.service;

import com.viser.StockTrade.dto.UserDto;
import com.viser.StockTrade.entity.Role;
import com.viser.StockTrade.entity.User;
import com.viser.StockTrade.exceptions.NameExistException;
import com.viser.StockTrade.exceptions.ValidationException;
import com.viser.StockTrade.security.JwtGenerator;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static com.viser.StockTrade.exceptions.ExceptionHelper.throwNameExistException;
import static com.viser.StockTrade.exceptions.ExceptionHelper.throwValidationException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;

    /**
     * Authenticates a user and adds a JWT token to the HTTP response as a cookie.
     *
     * This method takes a {@link UserDto} object containing user credentials and attempts to authenticate the user.
     * If authentication is successful, a JWT token is generated and added to the HTTP response as a cookie.
     * The JWT token is used for subsequent authentication and authorization of the user in the application.
     *
     * @param userDto the user credentials containing the username and password for authentication
     * @param response the {@link HttpServletResponse} object used to add the JWT token as a cookie
     */
    public void login(UserDto userDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        Cookie cookie = getCookie(token);
        response.addCookie(cookie);
    }

    /**
     * Logs out the current user by clearing the JWT token from the HTTP response and clearing the security context.
     *
     * This method creates a cookie with the name "jwtToken" and sets its value to null. The cookie's maximum age is set to zero,
     * effectively deleting it from the client. This ensures that the JWT token is removed and no longer available for subsequent requests.
     * Additionally, the security context is cleared to log out the user from the current session.
     *
     * @param response the {@link HttpServletResponse} object used to remove the JWT token cookie from the client's browser
     */
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        SecurityContextHolder.clearContext();
    }

    /**
     * Registers a new user by validating the provided user data and checking for username uniqueness.
     *
     * @param userDto the {@link UserDto} object containing the user data to be registered
     * @param result the {@link BindingResult} object that holds validation errors, if any
     * @throws ValidationException if there are validation errors with the provided user data
     * @throws NameExistException if a user with the same username already exists in the system
     */
    public void register(UserDto userDto, BindingResult result) throws ValidationException, NameExistException {
        throwValidationException(result, "/add-user-page");
        throwNameExistException(userService.existByUsername(userDto.getUsername()), "A user with this username already exists. Please choose a different username.", "/add-user-page");
        User user = new User();
        setUserFields(user, userDto);
        userService.save(user);
    }

    /**
     * Creates a new HTTP cookie containing the given JWT token.
     *
     * This method creates a {@link Cookie} with the name "jwtToken" and sets its value to the provided token.
     * The cookie is configured with the following properties:
     * - {@code HttpOnly}: This flag is set to true to prevent client-side scripts from accessing the cookie.
     * - {@code MaxAge}: The cookie's lifespan is set to 900 seconds (15 minutes).
     * - {@code Path}: The cookie is valid for the entire domain ("/").
     *
     * @param token the JWT token to be stored in the cookie
     * @return a {@link Cookie} object configured with the provided token
     */
    private Cookie getCookie(String token) {
        Cookie cookie = new Cookie("jwtToken", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(900);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * Sets the fields of the {@link User} object based on the provided {@link UserDto}.
     *
     * This method populates the given {@code User} object with values from the {@code UserDto}.
     * It sets the username and password of the user, and assigns a default role of "USER".
     * The password is encoded using a {@link PasswordEncoder} before being set.
     *
     * @param user the {@link User} object to be populated
     * @param userDto the {@link UserDto} object containing the user data
     */
    private void setUserFields(User user, UserDto userDto) {
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roles = roleService.getByName("USER");
        user.setRoles(Collections.singletonList(roles));
    }
}
