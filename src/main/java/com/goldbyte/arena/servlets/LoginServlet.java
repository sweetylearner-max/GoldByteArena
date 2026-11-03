package com.goldbyte.arena.servlets;

import com.goldbyte.arena.config.DBConnection;
import com.goldbyte.arena.dao.UserDAO;
import com.goldbyte.arena.models.User;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String identifier = req.getParameter("identifier") == null ? "" : req.getParameter("identifier").trim();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");

        String error = null;
        if (identifier.isEmpty() || password.isEmpty()) {
            error = "Enter your username or email, and your password.";
        } else {
            try {
                User user = userDAO.authenticate(identifier, DBConnection.hash(password));
                if (user == null) {
                    error = "Wrong username/email or password.";
                } else {
                    HttpSession session = req.getSession(true);
                    req.changeSessionId();                       // protects against session fixation
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("username", user.getUsername());

                    // Cookie: remember the username so the login form is pre-filled next time
                    Cookie remember = new Cookie("gba_user", user.getUsername());
                    remember.setMaxAge(7 * 24 * 60 * 60);
                    remember.setPath("/");
                    remember.setHttpOnly(true);
                    resp.addCookie(remember);

                    // ServletContext: arena-wide login counter shown on the admin dashboard
                    ServletContext ctx = getServletContext();
                    synchronized (ctx) {
                        AtomicInteger counter = (AtomicInteger) ctx.getAttribute("totalLogins");
                        if (counter == null) { counter = new AtomicInteger(); ctx.setAttribute("totalLogins", counter); }
                        counter.incrementAndGet();
                    }
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }
            } catch (SQLException e) {
                error = "Cannot reach the database right now. Please try again shortly.";
            }
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
