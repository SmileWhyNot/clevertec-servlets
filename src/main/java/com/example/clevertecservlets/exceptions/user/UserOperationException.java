package com.example.clevertecservlets.exceptions.user;

public class UserOperationException extends RuntimeException {
    public UserOperationException(String message) {
        super(message);
    }
}
