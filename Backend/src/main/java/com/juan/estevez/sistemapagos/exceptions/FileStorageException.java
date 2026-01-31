package com.juan.estevez.sistemapagos.exceptions;

import org.springframework.http.HttpStatus;

public class FileStorageException extends ApiException {

    public FileStorageException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message + ": " + cause.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        initCause(cause);
    }
}