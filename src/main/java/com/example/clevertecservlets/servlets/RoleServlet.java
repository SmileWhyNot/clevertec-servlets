package com.example.clevertecservlets.servlets;

import com.example.clevertecservlets.entity.Role;
import com.example.clevertecservlets.entity.User;
import com.example.clevertecservlets.service.RoleService;
import com.example.clevertecservlets.utils.Validator;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@WebServlet(value = "/role")
public class RoleServlet extends HttpServlet {

    private RoleService roleService;
    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.roleService = new RoleService();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("id") != null) {
            Long id = Long.parseLong(req.getParameter("id"));
            Role role = roleService.getRole(id);
            sendResp(resp, role, 200);
        } else {
            List<Role> roles = roleService.getAllRoles();
            sendResp(resp, roles, 200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Role role = getFromRequest(req);
        Role roleResp = roleService.createRole(role);
        sendResp(resp, roleResp, 201);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Role role = getFromRequest(req);
        Set<Role> sessionRoles = (Set<Role>) req.getSession().getAttribute("roles");
        if (sessionRoles != null && sessionRoles.stream().anyMatch(r -> role.getRoleName().equals(r.getRoleName()))) {
            // Найдена соответствующая роль в сессии, обновляем ее значение
            sessionRoles = sessionRoles.stream()
                    .map(role1 -> role.getRoleName().equals(role1.getRoleName()) ? role : role1)
                    .collect(Collectors.toSet());

            req.getSession().setAttribute("roles", sessionRoles);
        }

        Role roleResp = roleService.updateRole(role);
        sendResp(resp, roleResp, 200);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long roleId = Long.parseLong(req.getParameter("id"));

        Set<Role> sessionRoles = (Set<Role>) req.getSession().getAttribute("roles");
        // Проверяем, есть ли роль в сессии
        Optional<Role> roleToRemove = sessionRoles.stream()
                .filter(role -> roleId.equals(role.getId()))
                .findFirst();

        // Если роль найдена, удаляем ее из сессии
        if (roleToRemove.isPresent()) {
            sessionRoles.remove(roleToRemove.get());
            req.getSession().setAttribute("roles", sessionRoles);
        }

        roleService.deleteRole(roleId);
        sendResp(resp, "Role deleted successfully", 200);
    }

    private void sendResp(HttpServletResponse response, Object o, int code) throws IOException {
        String role = gson.toJson(o);
        response.getWriter().write(role);
        response.setStatus(code);
        response.setContentType("application/json");
    }

    private Role getFromRequest(HttpServletRequest request) throws IOException {
        String res = request.getAttribute("body").toString();
        return gson.fromJson(res, Role.class);
    }
}

