package com.viser.StockTrade.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends Throwable{
    private final String redirectUrl;

    public ValidationException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }
}
