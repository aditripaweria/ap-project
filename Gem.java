package com.example.demo2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Gem implements GameElement {
    private ImageView gemView;

    public Gem(double x, double y) {
        this.gemView = new ImageView(new Image("/gem.png"));
        this.gemView.setFitWidth(30);
        this.gemView.setFitHeight(30);
        this.gemView.setTranslateX(x);
        this.gemView.setTranslateY(y);
    }

    @Override
    public void interact() {
        // Implementation for gem interaction
        // For example, checking if the character collects the gem
    }

    @Override
    public ImageView getView() {
        return gemView;
    }
}

