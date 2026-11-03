package com.goldbyte.arena.servlets;

import com.goldbyte.arena.dao.MissionDAO;
import com.goldbyte.arena.models.Mission;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet("/missions")
public class MissionServlet extends HttpServlet {

    private final MissionDAO missionDAO = new MissionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        String category = whitelist(req.getParameter("category"), "Java", "SQL", "DSA");
        String difficulty = whitelist(req.getParameter("difficulty"), "Easy", "Medium", "Hard");

        List<Mission> missions = new ArrayList<>();
        try {
            missions = missionDAO.listForStudent(userId, category, difficulty);
        } catch (SQLException e) {
            req.setAttribute("error", "Missions could not be loaded from the database. Please refresh.");
        }
        req.setAttribute("missions", missions);
        req.setAttribute("selCategory", category == null ? "" : category);
        req.setAttribute("selDifficulty", difficulty == null ? "" : difficulty);
        req.getRequestDispatcher("/missions.jsp").forward(req, resp);
    }

    /** Lets SubmitAnswerServlet forward here to show an error on the missions page. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    private String whitelist(String value, String... allowed) {
        return (value != null && Arrays.asList(allowed).contains(value)) ? value : null;
    }
}
