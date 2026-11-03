package com.goldbyte.arena.models;

/** A question. The correct answer is intentionally NOT stored here, so it can never leak to students. */
public class Mission {
    private int id;
    private String title;
    private String description;
    private String category;
    private String difficulty;
    private String optionA, optionB, optionC, optionD;
    private int xpReward;
    /** 0 = not attempted, 1 = solved, 2 = attempted but wrong */
    private int status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getOptionA() { return optionA; }
    public void setOptionA(String v) { this.optionA = v; }
    public String getOptionB() { return optionB; }
    public void setOptionB(String v) { this.optionB = v; }
    public String getOptionC() { return optionC; }
    public void setOptionC(String v) { this.optionC = v; }
    public String getOptionD() { return optionD; }
    public void setOptionD(String v) { this.optionD = v; }
    public int getXpReward() { return xpReward; }
    public void setXpReward(int xpReward) { this.xpReward = xpReward; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    /** Difficulty multiplier: Easy x1, Medium x1.5, Hard x2 */
    public static double multiplierFor(String difficulty) {
        if ("Hard".equals(difficulty)) return 2.0;
        if ("Medium".equals(difficulty)) return 1.5;
        return 1.0;
    }

    public int getEffectiveXp() { return (int) Math.round(xpReward * multiplierFor(difficulty)); }
}
