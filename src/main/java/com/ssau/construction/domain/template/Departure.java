package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Departure extends FunctionalBlock {

    public Departure(GraphicsContext graphicsContext){
        super(graphicsContext);
    }

    public void render(double x, double y, int size) {
        String imagePath = "pictures/departure.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
