package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Road extends FunctionalBlock {

    public Road (GraphicsContext graphicsContext){ super(graphicsContext);}

    public void render(double x, double y, int size) {
        String imagePath = "road.jpg";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
