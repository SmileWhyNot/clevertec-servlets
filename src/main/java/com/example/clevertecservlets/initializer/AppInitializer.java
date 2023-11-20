package com.example.clevertecservlets.initializer;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.service.RoleService;
import com.example.clevertecservlets.service.UserService;
import com.example.clevertecservlets.utils.Validator;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.util.Set;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {
        RoleService roleService = new RoleService(new Validator());

        Role adminRole = new Role(1L, "ADMIN");
        Role userRole = new Role(2L, "USER");

        roleService.createRole(adminRole);
        roleService.createRole(userRole);
    }

    private void initializeUsers() {
        UserService userService = new UserService(new Validator());
        RoleService roleService = new RoleService(new Validator());

        User adminUser = new User(1L, "admin", "admin", Set.of(roleService.getRole(1L)));
        User normalUser = new User(2L, "user", "user", Set.of(roleService.getRole(2L)));
        userService.createUser(adminUser);
        userService.createUser(normalUser);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}

