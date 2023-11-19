package com.example.clevertecservlets.exceptions.role;

public class RoleNameNotUniqueException extends RuntimeException {
    public RoleNameNotUniqueException(String message) {
        super(message);
    }
}
