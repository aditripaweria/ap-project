package com.example.demo2;

public class StickHero {
    private int score;
    private int cherries;
    private boolean isRevived;

    public StickHero() {
        // Initialize StickHero properties
    }

    public void extendStick(double length) {
        // Implement stick extension logic
    }

    public void move(double distance) {
        // Implement character movement logic
    }

    public void flipUpsideDown() {
        // Implement flipping logic to collect cherries
    }

    public void revive() {
        // Implement revival logic
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCherries() {
        return cherries;
    }

    public void setCherries(int cherries) {
        this.cherries = cherries;
    }

    public boolean isRevived() {
        return isRevived;
    }

    public void setRevived(boolean revived) {
        isRevived = revived;
    }
}

