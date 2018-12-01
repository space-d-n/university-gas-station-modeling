package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InfoTable extends FunctionalBlock {

    public InfoTable(GraphicsContext graphicsContext){ super(graphicsContext);}

    public void render(double x, double y, int size) {
        String imagePath = "infoTable.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }

    public void renderInModeling(double x, double y, int size, int count){
        String imagePath = "road.jpg";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
        graphicsContext.setFill(Color.web("#e59711"));
        graphicsContext.setFont(Font.font("Arial", size/2));

        if (count<10){
            graphicsContext.fillText(Integer.toString(count), x + size / 2 - size / 6.998138688, y + size / 2 + size / 6);
        }
        else {
            graphicsContext.fillText(Integer.toString(count), x + size / 2 - size / 3.5, y + size / 2 + size / 6);
        }

        graphicsContext.setFill(Color.BLACK);
    }
}
