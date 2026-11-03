package com.goldbyte.arena.servlets;

import com.goldbyte.arena.dao.UserDAO;
import com.goldbyte.arena.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        try {
            User user = userDAO.findById(userId);
            if (user == null) {
                req.getSession().invalidate();
                resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=Account+not+found");
                return;
            }
            req.setAttribute("player", user);
            req.setAttribute("solved", userDAO.solvedCount(userId));
            req.setAttribute("rank", userDAO.rankOf(user.getXp()));
        } catch (SQLException e) {
            req.setAttribute("error", "Could not load your stats from the database. Refresh to try again.");
        }
        req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
    }
}
