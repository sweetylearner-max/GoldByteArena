package com.goldbyte.arena.dao;

import com.goldbyte.arena.config.DBConnection;
import com.goldbyte.arena.models.Mission;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionDAO {

    private static final String ORDER =
        " ORDER BY FIELD(m.category,'Java','SQL','DSA'), FIELD(m.difficulty,'Easy','Medium','Hard'), m.id";

    /** Missions for a student with their own attempt status. correct_option is never selected. */
    public List<Mission> listForStudent(int userId, String category, String difficulty) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT m.id, m.title, m.description, m.category, m.difficulty, "
          + "m.option_a, m.option_b, m.option_c, m.option_d, m.xp_reward, "
          + "s.id AS sid, s.is_correct FROM missions m "
          + "LEFT JOIN mission_submissions s ON s.mission_id = m.id AND s.user_id = ? WHERE 1=1");
        List<String> params = new ArrayList<>();
        if (category != null && !category.isEmpty()) { sql.append(" AND m.category = ?"); params.add(category); }
        if (difficulty != null && !difficulty.isEmpty()) { sql.append(" AND m.difficulty = ?"); params.add(difficulty); }
        sql.append(ORDER);

        List<Mission> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            ps.setInt(1, userId);
            for (int i = 0; i < params.size(); i++) ps.setString(i + 2, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Mission m = map(rs);
                    if (rs.getObject("sid") == null) m.setStatus(0);
                    else m.setStatus(rs.getBoolean("is_correct") ? 1 : 2);
                    list.add(m);
                }
            }
        }
        return list;
    }

    /** All missions for the admin catalog. */
    public List<Mission> listAll() throws SQLException {
        String sql = "SELECT m.id, m.title, m.description, m.category, m.difficulty, "
                   + "m.option_a, m.option_b, m.option_c, m.option_d, m.xp_reward "
                   + "FROM missions m" + ORDER;
        List<Mission> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void insert(Mission m, String correctOption) throws SQLException {
        String sql = "INSERT INTO missions (title, description, category, difficulty, "
                   + "option_a, option_b, option_c, option_d, correct_option, xp_reward) "
                   + "VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, m.getTitle());
            ps.setString(2, m.getDescription());
            ps.setString(3, m.getCategory());
            ps.setString(4, m.getDifficulty());
            ps.setString(5, m.getOptionA());
            ps.setString(6, m.getOptionB());
            ps.setString(7, m.getOptionC());
            ps.setString(8, m.getOptionD());
            ps.setString(9, correctOption);
            ps.setInt(10, m.getXpReward());
            ps.executeUpdate();
        }
    }

    private Mission map(ResultSet rs) throws SQLException {
        Mission m = new Mission();
        m.setId(rs.getInt("id"));
        m.setTitle(rs.getString("title"));
        m.setDescription(rs.getString("description"));
        m.setCategory(rs.getString("category"));
        m.setDifficulty(rs.getString("difficulty"));
        m.setOptionA(rs.getString("option_a"));
        m.setOptionB(rs.getString("option_b"));
        m.setOptionC(rs.getString("option_c"));
        m.setOptionD(rs.getString("option_d"));
        m.setXpReward(rs.getInt("xp_reward"));
        return m;
    }
}
