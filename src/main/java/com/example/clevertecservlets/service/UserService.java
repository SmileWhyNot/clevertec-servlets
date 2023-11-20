package com.example.clevertecservlets.service;

import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.exceptions.user.UserNotFoundException;
import com.example.clevertecservlets.exceptions.user.UserOperationException;
import com.example.clevertecservlets.exceptions.user.UsernameNotUniqueException;
import com.example.clevertecservlets.repository.UserRepository;
import com.example.clevertecservlets.utils.Validator;

import java.util.List;

public class UserService {
    private final UserRepository userRepository;
    private final Validator validator;

    public UserService(Validator validator) {
        this.validator = validator;
        this.userRepository = new UserRepository();
    }

    public User createUser(User user) {
        if (userRepository.isNameUnique(user.getUsername()) && validator.validateUser(user)) {
            return userRepository.create(user).orElseThrow(() -> new UserOperationException("Failed to update"));
        }
        throw new UsernameNotUniqueException("Username is not unique or user validation failed");
    }

    public User updateUser(User user) {
        User existingUser = userRepository.getById(user.getId()).orElseThrow(() -> new UserNotFoundException("Couldn't update, previous user not found"));
        if (validator.validateUser(user) &&
                (existingUser.getUsername().equals(user.getUsername())
                        || userRepository.isNameUnique(user.getUsername()))
        ) {
            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setRoles(user.getRoles());
            return userRepository.update(existingUser).orElseThrow(() -> new UserOperationException("Failed to update"));
        }
        throw new UsernameNotUniqueException("Username is not unique or user validation failed");
    }

    public boolean deleteUser(Long id) {
        if (userRepository.deleteById(id)) {
            return true;
        } else {
            throw new UserOperationException("Failed to delete user");
        }
    }

    public User getUser(Long id) {
        return userRepository.getById(id).orElseThrow(() -> new UserNotFoundException("No user with id" + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username).orElseThrow(() -> new UserNotFoundException("No user with username" + username));
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}
