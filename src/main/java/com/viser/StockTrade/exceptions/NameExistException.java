package com.viser.StockTrade.exceptions;

import lombok.Getter;

@Getter
public class NameExistException extends Throwable {
    private final String redirectUrl;

    public NameExistException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }
}
