package com.example.clevertecservlets.repository;


import com.example.clevertecservlets.entity.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository implements CRUDRepository<User> {

    private static final AtomicLong id = new AtomicLong(0L);
    private static final Map<Long, User> users = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<User> create(User user) {
        user.setId(id.incrementAndGet());
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> getById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return users.remove(id) != null;
    }

    @Override
    public boolean isNameUnique(String name) {
        return users.values().stream().noneMatch(u -> u.getUsername().equals(name));
    }

    public Optional<User> getUserByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }
}
