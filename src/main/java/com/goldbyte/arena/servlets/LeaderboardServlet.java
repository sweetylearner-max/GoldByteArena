package com.goldbyte.arena.servlets;

import com.goldbyte.arena.dao.SubmissionDAO;
import com.goldbyte.arena.models.LeaderboardEntry;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/leaderboard")
public class LeaderboardServlet extends HttpServlet {

    private final SubmissionDAO submissionDAO = new SubmissionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<LeaderboardEntry> entries = new ArrayList<>();
        try {
            entries = submissionDAO.leaderboard(50);
        } catch (SQLException e) {
            req.setAttribute("error", "The rankings could not be loaded from the database. Refresh to try again.");
        }
        req.setAttribute("entries", entries);
        req.getRequestDispatcher("/leaderboard.jsp").forward(req, resp);
    }
}
