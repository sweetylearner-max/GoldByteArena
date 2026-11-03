package com.goldbyte.arena.servlets;

import com.goldbyte.arena.dao.MissionDAO;
import com.goldbyte.arena.models.Mission;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/view-questions")
public class ViewQuestionsServlet extends HttpServlet {

    private final MissionDAO missionDAO = new MissionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Mission> missions = new ArrayList<>();
        try {
            missions = missionDAO.listAll();
        } catch (SQLException e) {
            req.setAttribute("error", "Questions could not be loaded from the database.");
        }
        req.setAttribute("missions", missions);
        req.getRequestDispatcher("/view-questions.jsp").forward(req, resp);
    }
}
