package com.goldbyte.arena.servlets;

import com.goldbyte.arena.dao.SubmissionDAO;
import com.goldbyte.arena.dao.SubmissionDAO.SubmitResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/submit-answer")
public class SubmitAnswerServlet extends HttpServlet {

    private final SubmissionDAO submissionDAO = new SubmissionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(req.getContextPath() + "/missions");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int userId = (Integer) req.getSession().getAttribute("userId");
        String answer = req.getParameter("answer");
        int missionId;
        try {
            missionId = Integer.parseInt(req.getParameter("missionId"));
        } catch (NumberFormatException e) {
            fail(req, resp, "Invalid mission.");
            return;
        }
        if (answer == null || !answer.matches("^[A-Da-d]$")) {
            fail(req, resp, "Pick one of the four answers before submitting.");
            return;
        }
        try {
            SubmitResult result = submissionDAO.submit(userId, missionId, answer.toUpperCase());
            if ("NOT_FOUND".equals(result.status)) { fail(req, resp, "That mission no longer exists."); return; }
            if ("DUPLICATE".equals(result.status)) { fail(req, resp, "You already used your one attempt on that mission."); return; }
            req.setAttribute("result", result);
            req.getRequestDispatcher("/mission-result.jsp").forward(req, resp);
        } catch (SQLException e) {
            fail(req, resp, "Your answer could not be saved because of a database problem. Nothing was changed; try again.");
        }
    }

    private void fail(HttpServletRequest req, HttpServletResponse resp, String msg) throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/missions").forward(req, resp);
    }
}
