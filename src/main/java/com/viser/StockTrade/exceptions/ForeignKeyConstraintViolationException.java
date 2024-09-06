package com.viser.StockTrade.exceptions;

import lombok.Getter;

@Getter
public class ForeignKeyConstraintViolationException extends Throwable {
    private final String redirectUrl;

    public ForeignKeyConstraintViolationException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }
}