package com.ssau.construction.domain;

import com.ssau.construction.domain.template.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.io.Serializable;

public class GasStation implements Serializable {

    private FunctionalBlock[][] gasStation;
    private transient GraphicsContext graphicsContext;
    private int size;

    private int entryI = -1;
    private int entryJ = -1;
    private int departureI = -1;
    private int departureJ = -1;
    private int infoTableI = -1;
    private int infoTableJ = -1;
    private int cashBoxI = -1;
    private int cashBoxJ = -1;

    public int getEntryI() {
        return entryI;
    }

    public int getEntryJ() {
        return entryJ;
    }

    public int getDepartureI() {
        return departureI;
    }

    public int getDepartureJ() {
        return departureJ;
    }

    public int getInfoTableI() {
        return infoTableI;
    }

    public int getInfoTableJ() {
        return infoTableJ;
    }

    public int getCashBoxI() {
        return cashBoxI;
    }

    public int getCashBoxJ() {
        return cashBoxJ;
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    private int functionalBlockH;
    private int functionalBlockV;
    private int serviceBlockH;
    private int serviceBlockV;
    private final double HORIZONTAL_MARGIN;
    private final double VERTICAL_MARGIN;
    private final double HIGHWAY_SIZE;

    public FunctionalBlock[][] getGasStation() {
        return gasStation;
    }

    public int getFunctionalBlockH() {
        return functionalBlockH;
    }

    public int getFunctionalBlockV() {
        return functionalBlockV;
    }

    public GasStation(int functionalBlockH, int functionalBlockV, int serviceBlockH, int serviceBlockV, GraphicsContext graphicsContext, int size) {
        gasStation = new FunctionalBlock[functionalBlockH + serviceBlockH][functionalBlockV];
        this.graphicsContext = graphicsContext;
        this.size = size;
        this.functionalBlockH = functionalBlockH;
        this.functionalBlockV = functionalBlockV;
        this.serviceBlockH = serviceBlockH;
        this.serviceBlockV = serviceBlockV;
        HIGHWAY_SIZE = 2 * size;
        HORIZONTAL_MARGIN = graphicsContext.getCanvas().getWidth() / 2 - functionalBlockH * size / 2;
        VERTICAL_MARGIN = graphicsContext.getCanvas().getHeight() / 2 - functionalBlockV * size / 2 - HIGHWAY_SIZE / 2;
        for (int i = 0; i < functionalBlockH + serviceBlockH; i++)
            for (int j = 0; j < functionalBlockV; j++) {
                gasStation[i][j] = new Road(graphicsContext);
            }
    }

    public GasStation(int functionalBlockH, int functionalBlockV, int serviceBlockN, int serviceBlockV, GraphicsContext graphicsContext, int size, GasStation oldGasStation) {
        this(functionalBlockH, functionalBlockV, serviceBlockN, serviceBlockN, graphicsContext, size);
        int minH = (functionalBlockH > oldGasStation.functionalBlockH) ? oldGasStation.functionalBlockH : functionalBlockH;
        int minV = (functionalBlockV > oldGasStation.functionalBlockV) ? oldGasStation.functionalBlockV : functionalBlockV;
        for (int i = 0; i < minH; i++)
            for (int j = 0; j < minV; j++) {
                try {
                    gasStation[i][j] = (FunctionalBlock) oldGasStation.gasStation[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                gasStation[i][j].setGraphicsContext(graphicsContext);
            }
        for (int i = minH; i < functionalBlockH; i++)
            for (int j = minV; j < functionalBlockV; j++)
                gasStation[i][j] = new Road(graphicsContext);

        if (oldGasStation.entryI < functionalBlockH && oldGasStation.entryJ < functionalBlockV) {
            entryI = oldGasStation.entryI;
            entryJ = oldGasStation.entryJ;
        }
        if (oldGasStation.departureI < functionalBlockH && oldGasStation.departureJ < functionalBlockV) {
            departureI = oldGasStation.departureI;
            departureJ = oldGasStation.departureJ;
        }
        if (oldGasStation.infoTableI < functionalBlockH && oldGasStation.infoTableJ < functionalBlockV) {
            infoTableI = oldGasStation.infoTableI;
            infoTableJ = oldGasStation.infoTableJ;
        }
        if (oldGasStation.cashBoxI < functionalBlockH && oldGasStation.cashBoxJ < functionalBlockV) {
            cashBoxI = oldGasStation.cashBoxI;
            cashBoxJ = oldGasStation.cashBoxJ;
        }
    }

    public FunctionalBlock getFunctionalBlock(int i, int j) {
        return gasStation[i][j];
    }

    //Отрисовка разметки
    public void drawMarkup() {
        for (int i = 0; i <= functionalBlockH + serviceBlockH; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN + i * size, VERTICAL_MARGIN, HORIZONTAL_MARGIN + i * size,
                    VERTICAL_MARGIN + functionalBlockV * size);
        }
        for (int i = 0; i <= functionalBlockV; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN, VERTICAL_MARGIN + i * size,
                    HORIZONTAL_MARGIN + functionalBlockH * size,
                    VERTICAL_MARGIN + i * size);
        }
    }

    //Отрисовка функционального блока
    public void drawFunctionalBlock(double x, double y) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - VERTICAL_MARGIN)) / size;
        graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 2, size - 2);
        gasStation[i][j].render(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 1);
    }


    //Создание функционального блока
    public void createFunctionalBlock(double x, double y, Template template) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - VERTICAL_MARGIN)) / size;
        if (i == entryI && j == entryJ) {
            entryI = -1;
            entryJ = -1;
        }
        if (i == departureI && j == departureJ) {
            departureI = -1;
            departureJ = -1;
        }
        if (i == infoTableI && j == infoTableJ) {
            infoTableI = -1;
            infoTableJ = -1;
        }
        if (i == cashBoxI && j == cashBoxJ) {
            cashBoxI = -1;
            cashBoxJ = -1;
        }
        switch (template) {
            case CashBox:
                gasStation[i][j] = new CashBox(graphicsContext);
                if (cashBoxI != -1 && cashBoxJ != -1) {
                    gasStation[cashBoxI][cashBoxJ] = new Road(graphicsContext);
                    drawFunctionalBlock(cashBoxI * size + HORIZONTAL_MARGIN, cashBoxJ * size + VERTICAL_MARGIN);
                }
                cashBoxI = i;
                cashBoxJ = j;
                break;
            case InfoTable:
                gasStation[i][j] = new InfoTable(graphicsContext);
                if (infoTableI != -1 && infoTableJ != -1) {
                    gasStation[infoTableI][infoTableJ] = new Road(graphicsContext);
                    drawFunctionalBlock(infoTableI * size + HORIZONTAL_MARGIN, infoTableJ * size + VERTICAL_MARGIN);
                }
                infoTableI = i;
                infoTableJ = j;
                break;
            case Road:
                gasStation[i][j] = new Road(graphicsContext);
                break;
            case Lawn:
                gasStation[i][j] = new Lawn(graphicsContext);
                break;
            case Entry:
                gasStation[i][j] = new Entry(graphicsContext);
                if (entryI != -1 && entryJ != -1) {
                    gasStation[entryI][entryJ] = new Road(graphicsContext);
                    drawFunctionalBlock(entryI * size + HORIZONTAL_MARGIN, entryJ * size + VERTICAL_MARGIN);
                }
                entryI = i;
                entryJ = j;
                break;
            case Departure:
                gasStation[i][j] = new Departure(graphicsContext);
                if (departureI != -1 && departureJ != -1) {
                    gasStation[departureI][departureJ] = new Road(graphicsContext);
                    drawFunctionalBlock(departureI * size + HORIZONTAL_MARGIN, departureJ * size + VERTICAL_MARGIN);
                }
                departureI = i;
                departureJ = j;
                break;
            case GasolinePump:
                gasStation[i][j] = new GasolinePump(graphicsContext);
                break;
            case GasolineTank:
                gasStation[i][j] = new GasolineTank(graphicsContext);
                break;
        }
    }

    public void drawFunctionalBlocks() {
        for (int i = 0; i < functionalBlockH + serviceBlockH; i++)
            for (int j = 0; j < functionalBlockV; j++)
                if (gasStation[i][j] != null) {
                    graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 2, size - 2);
                    gasStation[i][j].render(HORIZONTAL_MARGIN + i * size + 1, VERTICAL_MARGIN + j * size + 1, size - 1);
                }
    }

    public void drawFunctionalBlocksInModeling() {
        for (int i = 0; i < functionalBlockH + serviceBlockH; i++)
            for (int j = 0; j < functionalBlockV + serviceBlockV; j++)
                if (gasStation[i][j] != null) {
                    gasStation[i][j].render(HORIZONTAL_MARGIN + i * size, VERTICAL_MARGIN + j * size, size);
                }
    }

    //Отрисовка шоссе
    public void drawHighway() {
        String imagePath = "pictures/highway_road.jpg";
        Image image = new Image(imagePath);
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / HIGHWAY_SIZE; i++) {
            graphicsContext.drawImage(image, 2 * i * size, VERTICAL_MARGIN + functionalBlockV * size + 1, HIGHWAY_SIZE, HIGHWAY_SIZE);
        }
    }

    public void drawHighwayInModeling() {
        String imagePath = "pictures/highway_road.jpg";
        Image image = new Image(imagePath);
        for (int i = 0; i < entryI + HORIZONTAL_MARGIN / size; i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        for (int i = entryI + 1 + (int) (Math.floor(HORIZONTAL_MARGIN / size)); i < departureI + (int) (Math.ceil(HORIZONTAL_MARGIN / size)); i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        for (int i = departureI + 1 + (int) HORIZONTAL_MARGIN / size; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        imagePath = "highway_road_ed.jpg";
        image = new Image(imagePath);
        graphicsContext.drawImage(image, entryI * size + HORIZONTAL_MARGIN, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
        graphicsContext.drawImage(image, departureI * size + HORIZONTAL_MARGIN, VERTICAL_MARGIN + functionalBlockV * size, size, HIGHWAY_SIZE);
    }

    public boolean isInGasStation(int x, int y) {
        return x > HORIZONTAL_MARGIN && x < HORIZONTAL_MARGIN + functionalBlockH * size
                && y > VERTICAL_MARGIN && y < VERTICAL_MARGIN + functionalBlockV * size;
    }

    public void drawInfoTableInModeling(int count) {
        ((InfoTable) gasStation[infoTableI][infoTableJ]).renderInModeling(HORIZONTAL_MARGIN + infoTableI * size, VERTICAL_MARGIN + infoTableJ * size, size, count);
    }

    public void drawBackground() {
        String imagePath = "pictures/tree.png";
        Image image = new Image(imagePath);
        imagePath = "pictures/lawn2.png";
        Image image2 = new Image(imagePath);
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            for (int j = 0; j < graphicsContext.getCanvas().getHeight() / size; j++) {
                graphicsContext.drawImage(image2, i * size, j * size, size, size);
                //graphicsContext.drawImage(image,i*size, j*size,size,size);
            }
        }
        for (int i = -1; i < 1 + functionalBlockH + serviceBlockH; i++) {
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN + i * size, VERTICAL_MARGIN - size, size, size);
        }
        for (int i = 0; i < functionalBlockV; i++) {
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN - size, VERTICAL_MARGIN + size * i, size, size);
        }
        for (int i = 0; i < functionalBlockV; i++) {
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN + functionalBlockH * size, VERTICAL_MARGIN + size * i, size, size);
        }
        for (int i = 1; i <= HORIZONTAL_MARGIN / size + 1; i++) {
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN - i * size, VERTICAL_MARGIN + functionalBlockV * size - size, size, size);
        }
        for (int i = 0; i < HORIZONTAL_MARGIN / size; i++) {
            graphicsContext.drawImage(image, HORIZONTAL_MARGIN + functionalBlockH * size + i * size, VERTICAL_MARGIN + functionalBlockV * size - size, size, size);
        }
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            graphicsContext.drawImage(image, i * size, VERTICAL_MARGIN + functionalBlockV * size + 2 * size, size, size);
        }
    }

    //Отрисовка номеров бензоколонок
    public void drawGasolinePumpNumbers() {
        for (int j = 0, number = 1; j < functionalBlockV; j++)
            for (int i = 0; i < functionalBlockH; i++) {
                if (gasStation[i][j] instanceof GasolinePump) {
                    graphicsContext.setFill(Color.web("#e59711"));
                    graphicsContext.setFont(Font.font("Arial", size / 2));
                    ((GasolinePump) gasStation[i][j]).setNumber(number);
                    if (number < 10) {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + size / 2 - size / 6.998138688, j * size + VERTICAL_MARGIN + size / 2 + size / 6);
                    } else {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + size / 2 - size / 3.5, j * size + VERTICAL_MARGIN + size / 2 + size / 6);
                    }

                    graphicsContext.setFill(Color.BLACK);
                    number++;
                }
            }
    }

    //Отрисовка номеров бензоколонок
    public void drawGasolineTankNumbers() {
        for (int j = 0, number = 1; j < functionalBlockV; j++)
            for (int i = 0; i < functionalBlockH; i++) {
                if (gasStation[i][j] instanceof GasolineTank) {
                    graphicsContext.setFill(Color.web("#e59711"));
                    graphicsContext.setFont(Font.font("Arial", size / 2));
                    ((GasolineTank) gasStation[i][j]).setNumber(number);
                    if (number < 10) {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + size / 2 - size / 6.998138688, j * size + VERTICAL_MARGIN + size / 2 + size / 6);
                    } else {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + size / 2 - size / 3.5, j * size + VERTICAL_MARGIN + size / 2 + size / 6);
                    }

                    graphicsContext.setFill(Color.BLACK);
                    number++;
                }
            }
    }

    public double getHORIZONTAL_MARGIN() {
        return HORIZONTAL_MARGIN;
    }

    public double getVERTICAL_MARGIN() {
        return VERTICAL_MARGIN;
    }
}
