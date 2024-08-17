package com.viser.StockTrade.exceptions;

public class ForeignKeyConstraintViolationException extends Throwable {
    public ForeignKeyConstraintViolationException(String message) {
        super(message);
    }
}
