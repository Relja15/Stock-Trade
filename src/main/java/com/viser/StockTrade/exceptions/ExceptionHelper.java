package com.viser.StockTrade.exceptions;

import org.springframework.validation.BindingResult;

public class ExceptionHelper {

    public static void throwValidationException(BindingResult result, String redirecUrl) throws ValidationException {
        if(result.hasErrors()){
            throw new ValidationException(result.getAllErrors().getFirst().getDefaultMessage(), redirecUrl);
        }
    }
}
