package com.goldbyte.arena.servlets;

import com.goldbyte.arena.dao.MissionDAO;
import com.goldbyte.arena.models.Mission;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

@WebServlet("/admin/add-question")
public class AddQuestionServlet extends HttpServlet {

    private final MissionDAO missionDAO = new MissionDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/add-question.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String title = p(req, "title"), desc = p(req, "description");
        String category = p(req, "category"), difficulty = p(req, "difficulty");
        String a = p(req, "optionA"), b = p(req, "optionB"), c = p(req, "optionC"), d = p(req, "optionD");
        String correct = p(req, "correct").toUpperCase();
        int xp = 0;
        try { xp = Integer.parseInt(p(req, "xp")); } catch (NumberFormatException ignore) { }

        String error = null;
        if (title.isEmpty() || desc.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
            error = "Title, description and all four answer choices are required.";
        } else if (title.length() > 150 || a.length() > 255 || b.length() > 255 || c.length() > 255 || d.length() > 255) {
            error = "Title must be at most 150 characters and each choice at most 255.";
        } else if (!Arrays.asList("Java", "SQL", "DSA").contains(category)) {
            error = "Pick a category: Java, SQL or DSA.";
        } else if (!Arrays.asList("Easy", "Medium", "Hard").contains(difficulty)) {
            error = "Pick a difficulty: Easy, Medium or Hard.";
        } else if (!Arrays.asList("A", "B", "C", "D").contains(correct)) {
            error = "Pick which choice (A to D) is correct.";
        } else if (xp < 5 || xp > 100) {
            error = "XP reward must be a number between 5 and 100.";
        }

        if (error == null) {
            Mission m = new Mission();
            m.setTitle(title); m.setDescription(desc); m.setCategory(category); m.setDifficulty(difficulty);
            m.setOptionA(a); m.setOptionB(b); m.setOptionC(c); m.setOptionD(d); m.setXpReward(xp);
            try {
                missionDAO.insert(m, correct);
                resp.sendRedirect(req.getContextPath() + "/admin/view-questions?added=1");
                return;
            } catch (SQLException e) {
                error = "The question could not be saved because of a database problem.";
            }
        }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/add-question.jsp").forward(req, resp);
    }

    private String p(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }
}
