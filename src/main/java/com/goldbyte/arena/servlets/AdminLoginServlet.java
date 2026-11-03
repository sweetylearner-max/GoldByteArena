package com.goldbyte.arena.servlets;

import com.goldbyte.arena.config.DBConnection;
import com.goldbyte.arena.dao.AdminDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

/** Mapped in web.xml so it can read a ServletConfig init-param (panelName). */
public class AdminLoginServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();
    private String panelName;

    @Override
    public void init() {
        // ServletConfig: value comes from <init-param> in web.xml
        panelName = getServletConfig().getInitParameter("panelName");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/admin-login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username") == null ? "" : req.getParameter("username").trim();
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String error = null;

        if (username.isEmpty() || password.isEmpty()) {
            error = "Enter the admin username and password.";
        } else {
            try {
                int adminId = adminDAO.authenticate(username, DBConnection.hash(password));
                if (adminId < 0) {
                    error = "Wrong admin credentials.";
                } else {
                    HttpSession session = req.getSession(true);
                    req.changeSessionId();
                    session.setAttribute("adminId", adminId);
                    session.setAttribute("adminName", username);
                    session.setAttribute("panelName", panelName);
                    // ServletContext: shared, arena-wide value from <context-param> in web.xml
                    session.setAttribute("arenaName", getServletContext().getInitParameter("arenaName"));
                    resp.sendRedirect(req.getContextPath() + "/admin-dashboard.jsp");
                    return;
                }
            } catch (SQLException e) {
                error = "Cannot reach the database right now. Please try again shortly.";
            }
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/admin-login.jsp").forward(req, resp);
    }
}
