package com.example.demo2;

import java.util.List;
import javafx.scene.layout.Pane;

public class GameView {
    private StickHero stickHero;
    private List<Platform> platforms;

    public GameView(StickHero stickHero, List<Platform> platforms) {
        this.stickHero = stickHero;
        this.platforms = platforms;
        Pane gamePane = new Pane();
        // Set up the gamePane and other initialization
    }

    //public Pane getGamePane() {
        //return gamePane;
    //}

    public void render() {
        // Implement rendering logic (update visual elements based on game state)
    }

    public StickHero getStickHero() {
        return stickHero;
    }

    public void setStickHero(StickHero stickHero) {
        this.stickHero = stickHero;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<Platform> platforms) {
        this.platforms = platforms;
    }
    // Constructor, methods for rendering the game, handling animations, and playing sounds
}
