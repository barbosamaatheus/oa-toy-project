package org.example;

public class PenaltyService {

    // Scenario 2 (different method / different class): Bob edits this.
    public void punish(Player p) {
        System.out.println("PenaltyService.punish called");
        p.setScore(0); // Bob (Right): reset score as a penalty
    }
}
