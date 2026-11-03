package com.goldbyte.arena.models;

public class User {
    private int id;
    private String username;
    private String email;
    private int xp;
    private int level;
    private int streak;

    public User() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    /** Level = 1 + floor(XP / 100) */
    public static int levelFor(int xp) { return 1 + xp / 100; }

    public int getXpIntoLevel() { return xp % 100; }
    public int getXpToNext() { return 100 - (xp % 100); }

    public static String titleFor(int level) {
        if (level <= 1) return "Bronze Initiate";
        if (level <= 3) return "Silver Scripter";
        if (level <= 5) return "Gold Operator";
        if (level <= 7) return "Amethyst Architect";
        return "Golden Overlord";
    }

    public String getTitle() { return titleFor(level); }
}
