package com.example.clevertecservlets.filters;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(value = "/user", filterName = "2")
public class UpdateUserFilter implements Filter {

    private UserService userService;
    private Gson gson;

    @Override
    public void init(FilterConfig filterConfig) {
        this.userService = new UserService();
        this.gson = new Gson();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (req.getMethod().equalsIgnoreCase("PUT")) {
            updateUser(req, resp, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    private void updateUser(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {

        String res = req.getAttribute("body").toString();
        User updatedUser = gson.fromJson(res, User.class);

        Set<Role> userRoles = (Set<Role>) req.getSession().getAttribute("roles");

        if (userRoles != null) {
            User existingUser = userService.getUser(updatedUser.getId());

            if (canUpdateUser(updatedUser, existingUser, userRoles)) {
                chain.doFilter(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().println("Access denied");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().println("Access denied");
        }
    }

    private boolean canUpdateUser(User updatedUser, User existingUser, Set<Role> userRoles) {
        boolean isAdmin = userRoles.stream().anyMatch(role -> "ADMIN".equals(role.getRoleName()));

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().equals(existingUser.getRoles())) {
            return isAdmin;
        }
        return true;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
