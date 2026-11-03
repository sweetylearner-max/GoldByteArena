package com.goldbyte.arena.dao;

import com.goldbyte.arena.config.DBConnection;
import java.sql.*;

public class AdminDAO {

    /** Returns the admin id, or -1 when credentials are wrong. */
    public int authenticate(String username, String passwordHash) throws SQLException {
        String sql = "SELECT id FROM admins WHERE username = ? AND password_hash = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getInt("id") : -1; }
        }
    }
}
