package com.goldbyte.arena.models;

public class LeaderboardEntry {
    private int rank;
    private String username;
    private int level;
    private int solved;
    private int xp;

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getSolved() { return solved; }
    public void setSolved(int solved) { this.solved = solved; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
}
