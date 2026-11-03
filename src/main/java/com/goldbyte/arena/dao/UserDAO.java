package com.goldbyte.arena.dao;

import com.goldbyte.arena.config.DBConnection;
import com.goldbyte.arena.models.User;
import java.sql.*;

public class UserDAO {

    public boolean usernameExists(String username) throws SQLException {
        return exists("SELECT 1 FROM users WHERE username = ?", username);
    }

    public boolean emailExists(String email) throws SQLException {
        return exists("SELECT 1 FROM users WHERE email = ?", email);
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    /** New players start at 0 XP, Level 1. */
    public void register(String username, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (username, email, password_hash, xp, level) VALUES (?, ?, ?, 0, 1)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.executeUpdate();
        }
    }

    /** Login with username OR email. Returns null when credentials are wrong. */
    public User authenticate(String identifier, String passwordHash) throws SQLException {
        String sql = "SELECT id, username, email, xp, level, streak FROM users "
                   + "WHERE (username = ? OR email = ?) AND password_hash = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, identifier);
            ps.setString(2, identifier);
            ps.setString(3, passwordHash);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT id, username, email, xp, level, streak FROM users WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? map(rs) : null; }
        }
    }

    public int solvedCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM mission_submissions WHERE user_id = ? AND is_correct = 1";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }

    public int rankOf(int xp) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) + 1 FROM users WHERE xp > ?")) {
            ps.setInt(1, xp);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setEmail(rs.getString("email"));
        u.setXp(rs.getInt("xp"));
        u.setLevel(rs.getInt("level"));
        u.setStreak(rs.getInt("streak"));
        return u;
    }
}
