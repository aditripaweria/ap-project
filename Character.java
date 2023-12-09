package com.example.demo2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Character implements GameElement {
    private ImageView characterView;

    public Character(String imagePath, double fitWidth, double fitHeight) {
        this.characterView = new ImageView(new Image(imagePath));
        this.characterView.setFitWidth(fitWidth);
        this.characterView.setFitHeight(fitHeight);
    }

    // Getter for the character view
    public ImageView getView() {
        return characterView;
    }

    // Abstract method for character-specific movement
    public abstract void move(double deltaX);
}
