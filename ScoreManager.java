package com.example.demo2;

public class ScoreManager {
    private int highestScore;
    private int lastScore;
    private int totalCherries;

    public ScoreManager() {
        // Initialize score properties
    }

    public void updateScores(int score, int cherries) {
        // Implement score update logic
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getLastScore() {
        return lastScore;
    }

    public int getTotalCherries() {
        return totalCherries;
    }

    public void setTotalCherries(int totalCherries) {
        this.totalCherries = totalCherries;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }
    // Constructor, getters, setters, and methods for updating scores
}

