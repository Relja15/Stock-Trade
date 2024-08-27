package com.viser.StockTrade.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends Throwable{
    private final String redirectUrl;
    public NotFoundException(String message, String redirectUrl) {
        super(message);
        this.redirectUrl = redirectUrl;
    }
}
