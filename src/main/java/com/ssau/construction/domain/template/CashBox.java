package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CashBox extends FunctionalBlock {

    public CashBox(GraphicsContext graphicsContext) {
        super(graphicsContext);
    }

    public void render(double x, double y, int size) {
        String imagePath = "pictures/cashBox.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }

    public void renderInModeling(double x, double y, int size, int amount) {
        String imagePath = "pictures/road.jpg";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
        graphicsContext.setFill(Color.web("#ffffff"));
        graphicsContext.setFont(Font.font("Arial", size / 3));

        if (amount < 10) {
            graphicsContext.fillText(Integer.toString(amount) + " у.е.", x + size / 2 - size / 6.998138688 + 3, y + size / 2 + size / 6 + 2);
        } else {
            graphicsContext.fillText(Integer.toString(amount) + " у.е.", x + size / 2 - size / 3.5 + 3, y + size / 2 + size / 6 + 2);
        }

        graphicsContext.setFill(Color.BLACK);
    }
}
