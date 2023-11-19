package com.example.clevertecservlets.service;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.exceptions.role.RoleNameNotUniqueException;
import com.example.clevertecservlets.exceptions.role.RoleNotFoundException;
import com.example.clevertecservlets.exceptions.role.RoleOperationException;
import com.example.clevertecservlets.repository.RoleRepository;
import com.example.clevertecservlets.utils.Validator;

public class RoleService {

    private final RoleRepository roleRepository;
    private final Validator validator;

    public RoleService(Validator validator) {
        this.validator = validator;
        this.roleRepository = new RoleRepository();
    }

    public Role createRole(Role role) {
        if (roleRepository.isNameUnique(role.getRoleName()) && validator.validateRole(role)) {
            return roleRepository.create(role).orElseThrow(() -> new RoleOperationException("Failed to create role"));
        }
        throw new RoleNameNotUniqueException("Role name is not unique or role validation failed");
    }
    public Role updateRole(Role role) {
        Role existingRole = roleRepository.getById(role.getId()).orElseThrow(() -> new RoleNotFoundException("Couldn't update, previous role not found"));
        if (validator.validateRole(role) &&
                (existingRole.getRoleName().equals(role.getRoleName())
                        || roleRepository.isNameUnique(role.getRoleName()))
        ) {
            existingRole.setRoleName(role.getRoleName());
            return roleRepository.update(existingRole).orElseThrow(() -> new RoleOperationException("Failed to update role"));
        }
        throw new RoleNameNotUniqueException("Role name is not unique or role validation failed");
    }

    public boolean deleteRole(Long id) {
        if (roleRepository.deleteById(id)) {
            return true;
        } else {
            throw new RoleOperationException("Failed to delete role");
        }
    }

    public Role getRole(Long id) {
        return roleRepository.getById(id).orElseThrow(() -> new RoleNotFoundException("No role with id " + id));
    }
}
