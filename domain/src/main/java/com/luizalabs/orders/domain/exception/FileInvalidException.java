package com.luizalabs.orders.domain.exception;

public class FileInvalidException extends RuntimeException {

    public FileInvalidException(String message) {
        super(message);
    }
}
