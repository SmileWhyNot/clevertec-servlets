package com.example.clevertecservlets.exceptions.user;

public class UsernameNotUniqueException extends RuntimeException {

    public UsernameNotUniqueException(String message) {
        super(message);
    }
}
