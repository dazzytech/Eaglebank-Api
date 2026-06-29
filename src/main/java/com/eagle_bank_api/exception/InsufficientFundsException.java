package com.eagle_bank_api.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds to process transaction");
    }
}