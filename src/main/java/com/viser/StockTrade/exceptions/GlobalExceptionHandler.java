package com.viser.StockTrade.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link NameExistException} by adding an error message to the flash attributes and redirecting to a specified URL.
     *
     * This method is invoked when a {@link NameExistException} is thrown. It adds the exception message to the flash attributes
     * so that it can be displayed to the user. Then, it redirects the user to the URL specified in the exception's redirect URL.
     *
     * @param e the {@link NameExistException} that was thrown
     * @param ra the {@link RedirectAttributes} used to add flash attributes
     * @return a {@link RedirectView} that redirects the user to the URL specified in the exception
     */
    @ExceptionHandler(NameExistException.class)
    public RedirectView handleNameExistException(NameExistException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }

    /**
     * Handles {@link ForeignKeyConstraintViolationException} by adding an error message to the flash attributes and redirecting to a specified URL.
     *
     * This method is invoked when a {@link ForeignKeyConstraintViolationException} is thrown. It adds the exception message to the flash attributes
     * so that it can be displayed to the user. Then, it redirects the user to the URL specified in the exception's redirect URL.
     *
     * @param e the {@link ForeignKeyConstraintViolationException} that was thrown
     * @param ra the {@link RedirectAttributes} used to add flash attributes
     * @return a {@link RedirectView} that redirects the user to the URL specified in the exception
     */
    @ExceptionHandler(ForeignKeyConstraintViolationException.class)
    public RedirectView handleForeignKeyConstraintViolationException(ForeignKeyConstraintViolationException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }

    /**
     * Handles {@link NotFoundException} by adding an error message to the flash attributes and redirecting to a specified URL.
     *
     * This method is invoked when a {@link NotFoundException} is thrown. It adds the exception message to the flash attributes
     * so that it can be displayed to the user. Then, it redirects the user to the URL specified in the exception's redirect URL.
     *
     * @param e the {@link NotFoundException} that was thrown
     * @param ra the {@link RedirectAttributes} used to add flash attributes
     * @return a {@link RedirectView} that redirects the user to the URL specified in the exception
     */
    @ExceptionHandler(NotFoundException.class)
    public RedirectView handleNotFoundException(NotFoundException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }

    /**
     * Handles {@link IOException} by setting the HTTP response status to {@link HttpStatus#BAD_REQUEST}.
     *
     * This method is invoked when an {@link IOException} is thrown. It sets the HTTP response status to 400 (Bad Request)
     * to indicate that there was a problem with the request. This method does not return any response body.
     *
     * @param e the {@link IOException} that was thrown
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleIOException(IOException e) {
    }

    /**
     * Handles {@link BadCredentialsException} by adding an error message to the flash attributes and redirecting to the login page.
     *
     * This method is invoked when a {@link BadCredentialsException} is thrown, typically due to invalid username or password.
     * It adds an error message to the flash attributes so that it can be displayed to the user on the login page.
     * The user is then redirected to the login page to try again.
     *
     * @param e the {@link BadCredentialsException} that was thrown
     * @param ra the {@link RedirectAttributes} used to add flash attributes for the redirect
     * @return a {@link RedirectView} that redirects the user to the login page
     */
    @ExceptionHandler(BadCredentialsException.class)
    public RedirectView handleBadCredentialsException(BadCredentialsException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", "Invalid username or password.");
        return new RedirectView("/login-page");
    }

    /**
     * Handles {@link ValidationException} by adding an error message to the flash attributes and redirecting to the specified URL.
     *
     * This method is invoked when a {@link ValidationException} is thrown, typically due to validation errors in user input.
     * It adds the error message from the exception to the flash attributes so that it can be displayed to the user on the redirected page.
     * The user is then redirected to the URL specified in the exception.
     *
     * @param e the {@link ValidationException} that was thrown
     * @param ra the {@link RedirectAttributes} used to add flash attributes for the redirect
     * @return a {@link RedirectView} that redirects the user to the URL specified in the exception
     */
    @ExceptionHandler(ValidationException.class)
    public RedirectView handleValidationException(ValidationException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }
}
