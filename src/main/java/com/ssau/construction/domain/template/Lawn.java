package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Lawn extends FunctionalBlock {

    public Lawn (GraphicsContext graphicsContext) {super(graphicsContext);}

    public void render(double x, double y, int size) {
        String imagePath = "lawn.jpg";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
