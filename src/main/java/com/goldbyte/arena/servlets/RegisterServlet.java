package com.goldbyte.arena.servlets;

import com.goldbyte.arena.config.DBConnection;
import com.goldbyte.arena.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/register.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = trim(req.getParameter("username"));
        String email = trim(req.getParameter("email"));
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        String error = null;
        if (username.isEmpty() || email.isEmpty() || password == null || password.isEmpty()
                || confirm == null || confirm.isEmpty()) {
            error = "All fields are required.";
        } else if (!username.matches("^[A-Za-z0-9_]{3,30}$")) {
            error = "Username must be 3 to 30 characters: letters, numbers or underscore.";
        } else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || email.length() > 100) {
            error = "Enter a valid email address.";
        } else if (password.length() < 6) {
            error = "Password must be at least 6 characters.";
        } else if (!password.equals(confirm)) {
            error = "Passwords do not match.";
        }

        try {
            if (error == null && userDAO.usernameExists(username)) error = "That username is already taken.";
            if (error == null && userDAO.emailExists(email)) error = "That email is already registered.";
            if (error == null) {
                userDAO.register(username, email, DBConnection.hash(password));
                resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Account+created.+Log+in+to+enter+the+arena");
                return;
            }
        } catch (SQLException e) {
            error = "Database problem while creating your account. Please try again in a moment.";
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
