package com.ssau.construction.domain.template;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class InfoTable extends FunctionalBlock {

    public InfoTable(GraphicsContext graphicsContext){ super(graphicsContext);}

    public void render(double x, double y, int size) {
        String imagePath = "pictures/infoTable.png";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
    }

    public void renderInModeling(double x, double y, int size, int count){
        String imagePath = "pictures/road.jpg";
        Image image = new Image(imagePath);
        graphicsContext.drawImage(image, x, y, size, size);
        graphicsContext.setFill(Color.web("#ffffff"));
        graphicsContext.setFont(Font.font("Arial", size/3));

        if (count<10){
            graphicsContext.fillText(Integer.toString(count), x + size / 2 - size / 6.998138688 + 3, y + size / 2 + size / 6 + 2);
        }
        else {
            graphicsContext.fillText(Integer.toString(count), x + size / 2 - size / 3.5 + 3, y + size / 2 + size / 6 + 2);
        }

        graphicsContext.setFill(Color.BLACK);
    }
}
