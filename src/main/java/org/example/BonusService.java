package org.example;

public class BonusService {

    // Scenario 2 (different method / different class): Alice edits this.
    public void award(Player p) {
        System.out.println("BonusService.award called");
        p.setScore(p.getScore() + 50); // Alice (Left): award bonus points
    }
}
