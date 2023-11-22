package com.example.clevertecservlets.servlets;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.exceptions.user.UserNotFoundException;
import com.example.clevertecservlets.exceptions.user.UserOperationException;
import com.example.clevertecservlets.exceptions.user.UsernameNotUniqueException;
import com.example.clevertecservlets.service.UserService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@WebServlet(value = "/user")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private Gson gson;

    @Override
    public void init() {
        this.userService = new UserService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            if (req.getParameter("id") != null) {
                Long id = Long.parseLong(req.getParameter("id"));
                User user = userService.getUser(id);
                sendResp(resp, user, 200);
            } else {
                List<User> users = userService.getAllUsers();
                sendResp(resp, users, 200);
            }
        } catch (UserNotFoundException e) {
            sendErrorResp(resp, e.getMessage(), 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = getFromRequest(req);
            User user1 = userService.createUser(user);
            req.getSession().setAttribute("roles", user.getRoles());
            sendResp(resp, user1, 201);
        } catch (UsernameNotUniqueException e) {
            sendErrorResp(resp, e.getMessage(), 400);
        } catch (UserOperationException e) {
            sendErrorResp(resp, e.getMessage(), 500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User updatedUser = getFromRequest(req);
            User result = userService.updateUser(updatedUser);
            req.getSession().setAttribute("roles", updatedUser.getRoles());
            sendResp(resp, result, 200);
        } catch (UserNotFoundException | UsernameNotUniqueException e) {
            sendErrorResp(resp, e.getMessage(), 400);
        } catch (UserOperationException e) {
            sendErrorResp(resp, e.getMessage(), 500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Long userIdToDelete = Long.parseLong(req.getParameter("id"));
            Set<Role> userRoles = (Set<Role>) req.getSession().getAttribute("roles");
            boolean isAdmin = userRoles.stream().anyMatch(role -> "ADMIN".equals(role.getRoleName()));

            if (isAdmin) {
                boolean deletionResult = userService.deleteUser(userIdToDelete);

                if (deletionResult) {
                    req.getSession().invalidate();
                    sendResp(resp, "User deleted successfully", 200);
                } else {
                    sendResp(resp, "Failed to delete user", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().println("Access denied");
            }
        } catch (UserOperationException e) {
            sendErrorResp(resp, e.getMessage(), 500);
        }
    }

    private void sendResp(HttpServletResponse response, Object o, int code) throws IOException {
        String user1 = gson.toJson(o);
        response.getWriter().write(user1);
        response.setStatus(code);
        response.setContentType("application/json");
    }

    private User getFromRequest(HttpServletRequest request) {
        String res = request.getAttribute("body").toString();
        return gson.fromJson(res, User.class);
    }

    private void sendErrorResp(HttpServletResponse response, String errorMessage, int code) throws IOException {
        String error = gson.toJson(errorMessage);
        response.getWriter().write(error);
        response.setStatus(code);
        response.setContentType("application/json");
    }
}
