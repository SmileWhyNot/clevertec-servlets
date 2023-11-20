package com.example.clevertecservlets.utils;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.entity.User;

import java.util.Set;

public class Validator {

    private Validator() {}

    public static boolean validateRole(Role role) {
        return role != null
                && validateString(role.getRoleName());
    }

    public static boolean validateUser(User user) {
        return user != null
                && validateString(user.getUsername())
                && validateString(user.getPassword())
                && validateRoles(user.getRoles());
    }

    private static boolean validateString(String value) {
        return value != null && !value.isBlank();
    }
    private static boolean validateRoles(Set<Role> roles) {
        return roles != null && roles.stream().allMatch(Validator::validateRole);
    }
}
