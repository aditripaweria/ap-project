package com.example.demo2;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

abstract class GameObject extends ImageView {
    GameObject(Image image) {
        super(image);
    }

    // Common methods for game entities
    abstract void update();

    abstract void render();
}
