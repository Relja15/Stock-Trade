package com.viser.StockTrade.exceptions;

import org.springframework.validation.BindingResult;

public class ExceptionHelper {

    public static void throwValidationException(BindingResult result, String redirecUrl) throws ValidationException {
        if (result.hasErrors()) {
            throw new ValidationException(result.getAllErrors().getFirst().getDefaultMessage(), redirecUrl);
        }
    }

    public static <T> void throwNotFoundException(T entity, String message, String redirectUrl) throws NotFoundException {
        if (entity == null) {
            throw new NotFoundException(message, redirectUrl);
        }
        if (entity instanceof Boolean && !(Boolean) entity) {
            throw new NotFoundException(message, redirectUrl);
        }
    }

    public static void throwNameExistException(boolean nameExist, String message, String redirectUrl) throws NameExistException {
        if (nameExist) {
            throw new NameExistException(message, redirectUrl);
        }
    }
}
