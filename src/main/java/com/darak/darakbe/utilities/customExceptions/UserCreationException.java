package com.darak.darakbe.utilities.customExceptions;

public class UserCreationException extends RuntimeException {
    public UserCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
