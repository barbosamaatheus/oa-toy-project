package org.example;

public class Main {
    public static void main(String[] args) {
        Player p = new Player();
        BonusService bonus = new BonusService();
        PenaltyService penalty = new PenaltyService();

        // Scenario 1: writes to Player.score happen inside levelUp().
        p.levelUp();

        // Scenario 2: award() and punish() both write to the same Player.score.
        bonus.award(p);
        penalty.punish(p);

        System.out.println("Final score: " + p.getScore());
    }
}
