package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class GasolinePump extends FunctionalBlock {

    private int number;

    public GasolinePump (GraphicsContext graphicsContext){super(graphicsContext);}

    public void render(double x, double y, int size) {
        String imagePath = "gasoline_pump.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
