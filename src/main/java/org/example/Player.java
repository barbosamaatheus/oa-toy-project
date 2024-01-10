package org.example;

public class Player {
    private int score;
    private int level;
    private String lastAction;

    public Player() {
        this.score = 0;
        this.level = 1;
        this.lastAction = "none";
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    // Scenario 1 (same method): both developers edit levelUp().
    public void levelUp() {
        int gained = 1;
        this.level = this.level + gained;
        System.out.println("Level up to level " + this.level);
        System.out.println("Current score is " + this.score);
        this.lastAction = "levelUp";
    }
}
