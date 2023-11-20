package com.example.clevertecservlets.filters;

import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.service.UserService;
import com.example.clevertecservlets.utils.Validator;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@WebFilter(urlPatterns = {"/user", "/role"}, filterName = "0")
public class AuthFilter implements Filter {

    private final UserService userService = new UserService();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        // Если это POST на /user (регистрация), то пропустить без проверки авторизации
        if (req.getMethod().equalsIgnoreCase("POST") && req.getServletPath().equals("/user")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = req.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String base64Credentials = authorizationHeader.substring("Basic ".length()).trim();
            Base64.Decoder decoder = Base64.getDecoder();
            String credentials = new String(decoder.decode(base64Credentials), StandardCharsets.UTF_8);
            String[] credentialParts = credentials.split(":", 2);

            String username = credentialParts[0];
            String password = credentialParts[1];

            User user = userService.getUserByUsername(username);

            if (user.getPassword().equals(password)) {
                req.getSession().setAttribute("roles", user.getRoles());
                chain.doFilter(request, response);
            } else {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
