package com.example.clevertecservlets.servlets;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.service.UserService;
import com.example.clevertecservlets.utils.Validator;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.dynalink.linker.LinkerServices;

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
        if (req.getParameter("id") != null) {
            Long id = Long.parseLong(req.getParameter("id"));
            User user = userService.getUser(id);
            sendResp(resp, user, 200);
        } else {
            List<User> users = userService.getAllUsers();
            sendResp(resp, users, 200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getFromRequest(req);
        User user1 = userService.createUser(user);
        req.getSession().setAttribute("roles", user.getRoles());
        sendResp(resp, user1, 201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User updatedUser = getFromRequest(req);
        User result = userService.updateUser(updatedUser);
        req.getSession().setAttribute("roles", updatedUser.getRoles());
        sendResp(resp, result, 200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    }

    private void sendResp(HttpServletResponse response, Object o, int code) throws IOException {
        String user1 = gson.toJson(o);
        response.getWriter().write(user1);
        response.setStatus(code);
        response.setContentType("application/json");
    }

    private User getFromRequest(HttpServletRequest request) throws IOException {
        String res = request.getAttribute("body").toString();
        return gson.fromJson(res, User.class);
    }
}
