package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CashBox extends FunctionalBlock {

    public CashBox(GraphicsContext graphicsContext) {super(graphicsContext);}

    public void render(double x, double y, int size) {
        String imagePath = "pictures/cashBox.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }
}
