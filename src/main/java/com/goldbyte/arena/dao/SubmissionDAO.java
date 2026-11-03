package com.goldbyte.arena.dao;

import com.goldbyte.arena.config.DBConnection;
import com.goldbyte.arena.models.LeaderboardEntry;
import com.goldbyte.arena.models.Mission;
import com.goldbyte.arena.models.User;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SubmissionDAO {

    /** Everything the result page needs. */
    public static class SubmitResult {
        public String status = "OK";          // OK, DUPLICATE, NOT_FOUND
        public String missionTitle;
        public String selected;
        public String correctOption;
        public boolean correct;
        public int awarded;
        public int newXp;
        public int oldLevel;
        public int newLevel;
        public int streak;
        public boolean isLevelUp() { return newLevel > oldLevel; }
    }

    /**
     * One transaction: check attempt rule, save submission, and (if correct) update XP, level and streak.
     * Either everything is saved or nothing is.
     */
    public SubmitResult submit(int userId, int missionId, String selected) throws SQLException {
        SubmitResult r = new SubmitResult();
        r.selected = selected;
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            int base;
            String difficulty;
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT title, correct_option, xp_reward, difficulty FROM missions WHERE id = ?")) {
                ps.setInt(1, missionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) { c.rollback(); r.status = "NOT_FOUND"; return r; }
                    r.missionTitle = rs.getString("title");
                    r.correctOption = rs.getString("correct_option");
                    base = rs.getInt("xp_reward");
                    difficulty = rs.getString("difficulty");
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT 1 FROM mission_submissions WHERE user_id = ? AND mission_id = ?")) {
                ps.setInt(1, userId);
                ps.setInt(2, missionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) { c.rollback(); r.status = "DUPLICATE"; return r; }
                }
            }

            r.correct = r.correctOption.equalsIgnoreCase(selected);
            r.awarded = r.correct ? (int) Math.round(base * Mission.multiplierFor(difficulty)) : 0;

            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO mission_submissions (user_id, mission_id, selected_option, is_correct, xp_awarded) "
                  + "VALUES (?,?,?,?,?)")) {
                ps.setInt(1, userId);
                ps.setInt(2, missionId);
                ps.setString(3, selected.toUpperCase());
                ps.setBoolean(4, r.correct);
                ps.setInt(5, r.awarded);
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException dup) {
                c.rollback();
                r.status = "DUPLICATE";
                return r;
            }

            // lock the player row, then update stats
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT xp, level, streak, last_active FROM users WHERE id = ? FOR UPDATE")) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    int xp = rs.getInt("xp");
                    int streak = rs.getInt("streak");
                    Date lastActive = rs.getDate("last_active");
                    r.oldLevel = rs.getInt("level");
                    r.newXp = xp + r.awarded;
                    r.newLevel = User.levelFor(r.newXp);
                    r.streak = streak;

                    if (r.correct) {
                        LocalDate today = LocalDate.now();
                        if (lastActive == null) {
                            r.streak = 1;
                        } else {
                            LocalDate last = lastActive.toLocalDate();
                            if (last.equals(today)) r.streak = Math.max(streak, 1);
                            else if (last.equals(today.minusDays(1))) r.streak = streak + 1;
                            else r.streak = 1;
                        }
                        try (PreparedStatement up = c.prepareStatement(
                                "UPDATE users SET xp = ?, level = ?, streak = ?, last_active = ? WHERE id = ?")) {
                            up.setInt(1, r.newXp);
                            up.setInt(2, r.newLevel);
                            up.setInt(3, r.streak);
                            up.setDate(4, Date.valueOf(today));
                            up.setInt(5, userId);
                            up.executeUpdate();
                        }
                    }
                }
            }
            c.commit();
            return r;
        } catch (SQLException e) {
            if (c != null) { try { c.rollback(); } catch (SQLException ignore) { } }
            throw e;
        } finally {
            if (c != null) { try { c.close(); } catch (SQLException ignore) { } }
        }
    }

    /** Ranked by XP, then solved missions, then name. */
    public List<LeaderboardEntry> leaderboard(int limit) throws SQLException {
        String sql = "SELECT u.username, u.level, u.xp, "
                   + "COUNT(CASE WHEN s.is_correct = 1 THEN 1 END) AS solved "
                   + "FROM users u LEFT JOIN mission_submissions s ON s.user_id = u.id "
                   + "GROUP BY u.id, u.username, u.level, u.xp "
                   + "ORDER BY u.xp DESC, solved DESC, u.username ASC LIMIT ?";
        List<LeaderboardEntry> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    LeaderboardEntry e = new LeaderboardEntry();
                    e.setRank(rank++);
                    e.setUsername(rs.getString("username"));
                    e.setLevel(rs.getInt("level"));
                    e.setXp(rs.getInt("xp"));
                    e.setSolved(rs.getInt("solved"));
                    list.add(e);
                }
            }
        }
        return list;
    }
}
