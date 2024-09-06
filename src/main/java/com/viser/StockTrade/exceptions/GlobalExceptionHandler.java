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

    @ExceptionHandler(NameExistException.class)
    public RedirectView handleNameExistException(NameExistException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }

    @ExceptionHandler(ForeignKeyConstraintViolationException.class)
    public RedirectView handleForeignKeyConstraintViolationException(ForeignKeyConstraintViolationException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }

    @ExceptionHandler(NotFoundException.class)
    public RedirectView handleNotFoundException(NotFoundException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleIOException(IOException e) {
    }

    @ExceptionHandler(BadCredentialsException.class)
    public RedirectView handleBadCredentialsException(BadCredentialsException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", "Invalid username or password.");
        return new RedirectView("/login-page");
    }

    @ExceptionHandler(ValidationException.class)
    public RedirectView handleValidationException(ValidationException e, RedirectAttributes ra) {
        ra.addFlashAttribute("error", e.getMessage());
        return new RedirectView(e.getRedirectUrl());
    }
}
