package com.example.clevertecservlets.filters;

import com.example.clevertecservlets.entity.Role;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Set;

@WebFilter(value = "/role", filterName = "3")
public class RoleAccessFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Set<Role> userRoles = (Set<Role>) req.getSession().getAttribute("roles");

        // Проверка наличия роли ADMIN в сессии пользователя
        if (userRoles != null && userRoles.stream().anyMatch(role -> "ADMIN".equals(role.getRoleName()))) {
            // Если роль ADMIN присутствует, пропускаем запрос дальше по цепочке фильтров
            chain.doFilter(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().println("Access denied");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
