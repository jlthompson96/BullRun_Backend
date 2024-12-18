package com.thompson.bullrun.exceptions;

public class StockDataException extends RuntimeException {
    public StockDataException(String message) {
        super(message);
    }

    public StockDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
