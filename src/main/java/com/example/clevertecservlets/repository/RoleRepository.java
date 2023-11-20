package com.example.clevertecservlets.repository;


import com.example.clevertecservlets.entity.Role;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class RoleRepository implements CRUDRepository<Role> {

    private static final AtomicLong id = new AtomicLong(0L);
    private static final Map<Long, Role> roles = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<Role> create(Role role) {
        role.setId(id.incrementAndGet());
        roles.put(role.getId(), role);
        return Optional.of(role);
    }

    @Override
    public Optional<Role> update(Role role) {
        roles.put(role.getId(), role);
        return Optional.of(role);
    }

    @Override
    public Optional<Role> getById(Long id) {
        return Optional.ofNullable(roles.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        return roles.remove(id) != null;
    }

    @Override
    public boolean isNameUnique(String name) {
        return roles.values().stream().noneMatch(r -> r.getRoleName().equals(name));
    }

    public List<Role> getAll() {
        return roles.values().stream().toList();
    }
}
