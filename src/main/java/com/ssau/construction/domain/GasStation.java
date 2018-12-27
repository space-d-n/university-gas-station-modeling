package com.ssau.construction.domain;

import com.ssau.construction.domain.template.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.Serializable;

public class GasStation implements Serializable {

    private FunctionalBlock[][] gasStation;
    private FunctionalBlock[][] serviceArea;
    private transient GraphicsContext graphicsContext;
    private int size;

    private int gasStationEntryI = -1;
    private int gasStationEntryJ = -1;
    private int gasStationDepartureI = -1;
    private int gasStationDepartureJ = -1;
    private int serviceAreaEntryI = -1;
    private int serviceAreaEntryJ = -1;
    private int serviceAreaDepartureI = -1;
    private int serviceAreaDepartureJ = -1;
    private int infoTableI = -1;
    private int infoTableJ = -1;
    private int cashBoxI = -1;
    private int cashBoxJ = -1;

    public int getGasStationEntryI() {
        return gasStationEntryI;
    }

    public int getGasStationEntryJ() {
        return gasStationEntryJ;
    }

    public int getGasStationDepartureI() {
        return gasStationDepartureI;
    }

    public int getGasStationDepartureJ() {
        return gasStationDepartureJ;
    }

    public int getServiceAreaEntryI() {
        return serviceAreaEntryI;
    }

    public int getServiceAreaEntryJ() {
        return serviceAreaEntryJ;
    }

    public int getServiceAreaDepartureI() {
        return serviceAreaDepartureI;
    }

    public int getServiceAreaDepartureJ() {
        return serviceAreaDepartureJ;
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
    private final double GAS_STATION_VERTICAL_MARGIN_TOP;
    private final double GAS_STATION_VERTICAL_MARGIN_BOTTOM;
    private final double SERVICE_AREA_VERTICAL_MARGIN_TOP;
    private final double SERVICE_AREA_VERTICAL_MARGIN_BOTTOM;
    private final double HIGHWAY_SIZE;
    public static final int DISTANCE_BETWEEN = 50;

    public FunctionalBlock[][] getGasStation() {
        return gasStation;
    }

    public FunctionalBlock[][] getServiceArea() {
        return serviceArea;
    }

    public int getFunctionalBlockH() {
        return functionalBlockH;
    }

    public int getFunctionalBlockV() {
        return functionalBlockV;
    }

    public int getServiceBlockH() {
        return serviceBlockH;
    }

    public int getServiceBlockV() {
        return serviceBlockV;
    }

    public GasStation(int functionalBlockH, int functionalBlockV, int serviceBlockH, int serviceBlockV, GraphicsContext graphicsContext, int size) {
        gasStation = new FunctionalBlock[functionalBlockH][functionalBlockV];
        serviceArea = new FunctionalBlock[serviceBlockH][serviceBlockV];
        this.graphicsContext = graphicsContext;
        this.size = size;
        this.functionalBlockH = functionalBlockH;
        this.functionalBlockV = functionalBlockV;
        this.serviceBlockH = serviceBlockH;
        this.serviceBlockV = serviceBlockV;
        HIGHWAY_SIZE = 2 * size;
        HORIZONTAL_MARGIN = graphicsContext.getCanvas().getWidth() / 2 - functionalBlockH * size / 2 - serviceBlockH * size / 2 - DISTANCE_BETWEEN / 2;
        if (functionalBlockV > serviceBlockV) {
            GAS_STATION_VERTICAL_MARGIN_TOP = (graphicsContext.getCanvas().getHeight() - functionalBlockV * size - HIGHWAY_SIZE) / 2;
            SERVICE_AREA_VERTICAL_MARGIN_TOP = (graphicsContext.getCanvas().getHeight() - serviceBlockV * size - HIGHWAY_SIZE) / 2 + (Math.max(serviceBlockV, functionalBlockV) - Math.min(serviceBlockV, functionalBlockV)) * size / 2;
        } else {
            GAS_STATION_VERTICAL_MARGIN_TOP = (graphicsContext.getCanvas().getHeight() - functionalBlockV * size - HIGHWAY_SIZE) / 2 + (Math.max(serviceBlockV, functionalBlockV) - Math.min(serviceBlockV, functionalBlockV)) * size / 2;
            SERVICE_AREA_VERTICAL_MARGIN_TOP = (graphicsContext.getCanvas().getHeight() - serviceBlockV * size - HIGHWAY_SIZE) / 2;
        }
        GAS_STATION_VERTICAL_MARGIN_BOTTOM = graphicsContext.getCanvas().getHeight() - (functionalBlockV * size - HIGHWAY_SIZE) / 2 - GAS_STATION_VERTICAL_MARGIN_TOP;
        SERVICE_AREA_VERTICAL_MARGIN_BOTTOM = graphicsContext.getCanvas().getHeight() - (serviceBlockV * size - HIGHWAY_SIZE) / 2 - SERVICE_AREA_VERTICAL_MARGIN_TOP;
        for (int i = 0; i < functionalBlockH; i++) {
            for (int j = 0; j < functionalBlockV; j++) {
                gasStation[i][j] = new Road(graphicsContext);
            }
        }
        for (int i = 0; i < serviceBlockH; i++) {
            for (int j = 0; j < serviceBlockV; j++) {
                serviceArea[i][j] = new Road(graphicsContext);
            }
        }

    }

    public GasStation(int functionalBlockH, int functionalBlockV, int serviceBlockH, int serviceBlockV, GraphicsContext graphicsContext, int size, GasStation oldGasStation) {
        this(functionalBlockH, functionalBlockV, serviceBlockH, serviceBlockV, graphicsContext, size);
        int minGasStationH = (functionalBlockH > oldGasStation.functionalBlockH) ? oldGasStation.functionalBlockH : functionalBlockH;
        int minGasStationV = (functionalBlockV > oldGasStation.functionalBlockV) ? oldGasStation.functionalBlockV : functionalBlockV;
        int minServiceAreaH = (serviceBlockH > oldGasStation.serviceBlockH) ? oldGasStation.serviceBlockH : serviceBlockH;
        int minServiceAreaV = (serviceBlockV > oldGasStation.serviceBlockV) ? oldGasStation.serviceBlockV : serviceBlockV;
        for (int i = 0; i < minGasStationH; i++) {
            for (int j = 0; j < minGasStationV; j++) {
                try {
                    gasStation[i][j] = (FunctionalBlock) oldGasStation.gasStation[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                gasStation[i][j].setGraphicsContext(graphicsContext);
            }
        }
        for (int i = 0; i < minServiceAreaH; i++) {
            for (int j = 0; j < minServiceAreaV; j++) {
                try {
                    serviceArea[i][j] = (FunctionalBlock) oldGasStation.serviceArea[i][j].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                serviceArea[i][j].setGraphicsContext(graphicsContext);
            }
        }
        for (int i = minGasStationH; i < functionalBlockH; i++)
            for (int j = minGasStationV; j < functionalBlockV; j++)
                gasStation[i][j] = new Road(graphicsContext);
        for (int i = minServiceAreaH; i < serviceBlockH; i++)
            for (int j = minServiceAreaV; j < serviceBlockV; j++)
                serviceArea[i][j] = new Road(graphicsContext);

        if (oldGasStation.gasStationEntryI < functionalBlockH && oldGasStation.gasStationEntryJ < functionalBlockV) {
            gasStationEntryI = oldGasStation.gasStationEntryI;
            gasStationEntryJ = oldGasStation.gasStationEntryJ;
        }
        if (oldGasStation.gasStationDepartureI < functionalBlockH && oldGasStation.gasStationDepartureJ < functionalBlockV) {
            gasStationDepartureI = oldGasStation.gasStationDepartureI;
            gasStationDepartureJ = oldGasStation.gasStationDepartureJ;
        }
        if (oldGasStation.infoTableI < functionalBlockH && oldGasStation.infoTableJ < functionalBlockV) {
            infoTableI = oldGasStation.infoTableI;
            infoTableJ = oldGasStation.infoTableJ;
        }
        if (oldGasStation.cashBoxI < functionalBlockH && oldGasStation.cashBoxJ < functionalBlockV) {
            cashBoxI = oldGasStation.cashBoxI;
            cashBoxJ = oldGasStation.cashBoxJ;
        }
        if (oldGasStation.serviceAreaEntryI < serviceBlockH && oldGasStation.serviceAreaEntryJ < serviceBlockV) {
            serviceAreaEntryI = oldGasStation.serviceAreaEntryI;
            serviceAreaEntryJ = oldGasStation.serviceAreaEntryJ;
        }
        if (oldGasStation.serviceAreaDepartureI < serviceBlockH && oldGasStation.serviceAreaDepartureJ < serviceBlockV) {
            serviceAreaDepartureI = oldGasStation.serviceAreaDepartureI;
            serviceAreaDepartureJ = oldGasStation.serviceAreaDepartureJ;
        }
    }

    public FunctionalBlock getGasStationFunctionalBlock(int i, int j) {
        return gasStation[i][j];
    }

    public FunctionalBlock getServiceAreaFunctionalBlock(int i, int j) {
        return serviceArea[i][j];
    }

    //Отрисовка разметки
    public void drawMarkup() {
        // gas station markup
        for (int i = 0; i <= functionalBlockH; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN + i * size, GAS_STATION_VERTICAL_MARGIN_TOP,
                    HORIZONTAL_MARGIN + i * size, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size);
        }
        for (int i = 0; i <= functionalBlockV; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN, GAS_STATION_VERTICAL_MARGIN_TOP + i * size,
                    HORIZONTAL_MARGIN + functionalBlockH * size, GAS_STATION_VERTICAL_MARGIN_TOP + i * size);
        }
        // service area markup
        for (int i = 0; i <= serviceBlockH; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN + i * size + (functionalBlockH * size) + DISTANCE_BETWEEN, SERVICE_AREA_VERTICAL_MARGIN_TOP,
                    HORIZONTAL_MARGIN + i * size + (functionalBlockH * size) + DISTANCE_BETWEEN, SERVICE_AREA_VERTICAL_MARGIN_TOP + serviceBlockV * size);
        }
        for (int i = 0; i <= serviceBlockV; i++) {
            graphicsContext.strokeLine(
                    HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN, SERVICE_AREA_VERTICAL_MARGIN_TOP + i * size,
                    HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN + (serviceBlockH * size), SERVICE_AREA_VERTICAL_MARGIN_TOP + i * size);
        }
    }

    //Отрисовка функционального блока на заправке
    public void drawGasStationFunctionalBlock(double x, double y) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - GAS_STATION_VERTICAL_MARGIN_TOP)) / size;
        graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1, GAS_STATION_VERTICAL_MARGIN_TOP + j * size + 1, size - 2, size - 2);
        gasStation[i][j].render(HORIZONTAL_MARGIN + i * size + 1, GAS_STATION_VERTICAL_MARGIN_TOP + j * size + 1, size - 1);
    }

    //Отрисовка функционального блока в сервисной зоне
    public void drawServiceAreaFunctionalBlock(double x, double y) {
        int i = ((int) (x - HORIZONTAL_MARGIN - functionalBlockH * size - DISTANCE_BETWEEN)) / size;
        int j = ((int) (y - SERVICE_AREA_VERTICAL_MARGIN_TOP)) / size;
        graphicsContext.clearRect(HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN + i * size + 1, SERVICE_AREA_VERTICAL_MARGIN_TOP + j * size + 1, size - 2, size - 2);
        serviceArea[i][j].render(HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN + i * size + 1, SERVICE_AREA_VERTICAL_MARGIN_TOP + j * size + 1, size - 1);
    }


    //Создание функционального блока на заправке
    public void createGasStationFunctionalBlock(double x, double y, Template template) {
        int i = ((int) (x - HORIZONTAL_MARGIN)) / size;
        int j = ((int) (y - GAS_STATION_VERTICAL_MARGIN_TOP)) / size;
        if (i == gasStationEntryI && j == gasStationEntryJ) {
            gasStationEntryI = -1;
            gasStationEntryJ = -1;
        }
        if (i == gasStationDepartureI && j == gasStationDepartureJ) {
            gasStationDepartureI = -1;
            gasStationDepartureJ = -1;
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
                    drawGasStationFunctionalBlock(cashBoxI * size + HORIZONTAL_MARGIN, cashBoxJ * size + GAS_STATION_VERTICAL_MARGIN_TOP);
                }
                cashBoxI = i;
                cashBoxJ = j;
                break;
            case InfoTable:
                gasStation[i][j] = new InfoTable(graphicsContext);
                if (infoTableI != -1 && infoTableJ != -1) {
                    gasStation[infoTableI][infoTableJ] = new Road(graphicsContext);
                    drawGasStationFunctionalBlock(infoTableI * size + HORIZONTAL_MARGIN, infoTableJ * size + GAS_STATION_VERTICAL_MARGIN_TOP);
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
                if (gasStationEntryI != -1 && gasStationEntryJ != -1) {
                    gasStation[gasStationEntryI][gasStationEntryJ] = new Road(graphicsContext);
                    drawGasStationFunctionalBlock(gasStationEntryI * size + HORIZONTAL_MARGIN, gasStationEntryJ * size + GAS_STATION_VERTICAL_MARGIN_TOP);
                }
                gasStationEntryI = i;
                gasStationEntryJ = j;
                break;
            case Departure:
                gasStation[i][j] = new Departure(graphicsContext);
                if (gasStationDepartureI != -1 && gasStationDepartureJ != -1) {
                    gasStation[gasStationDepartureI][gasStationDepartureJ] = new Road(graphicsContext);
                    drawGasStationFunctionalBlock(gasStationDepartureI * size + HORIZONTAL_MARGIN, gasStationDepartureJ * size + GAS_STATION_VERTICAL_MARGIN_TOP);
                }
                gasStationDepartureI = i;
                gasStationDepartureJ = j;
                break;
            case GasolinePump:
                gasStation[i][j] = new GasolinePump(graphicsContext);
                break;
            case GasolineTank:
//                gasStation[i][j] = new GasolineTank(graphicsContext);
                break;
        }
    }

    //Создание функционального блока в сервисной зоне
    public void createServiceAreaFunctionalBlock(double x, double y, Template template) {
        int i = ((int) (x - HORIZONTAL_MARGIN - functionalBlockH * size - DISTANCE_BETWEEN)) / size;
        int j = ((int) (y - SERVICE_AREA_VERTICAL_MARGIN_TOP)) / size;
        if (i == serviceAreaEntryI && j == serviceAreaEntryJ) {
            serviceAreaEntryI = -1;
            serviceAreaEntryJ = -1;
        }
        if (i == serviceAreaDepartureI && j == serviceAreaDepartureJ) {
            serviceAreaDepartureI = -1;
            serviceAreaDepartureJ = -1;
        }
        switch (template) {
            case CashBox:
                break;
            case InfoTable:
                break;
            case Road:
                serviceArea[i][j] = new Road(graphicsContext);
                break;
            case Lawn:
                serviceArea[i][j] = new Lawn(graphicsContext);
                break;
            case Entry:
                serviceArea[i][j] = new Entry(graphicsContext);
                if (serviceAreaEntryI != -1 && serviceAreaEntryJ != -1) {
                    serviceArea[serviceAreaEntryI][serviceAreaEntryJ] = new Road(graphicsContext);
                    drawServiceAreaFunctionalBlock(serviceAreaEntryI * size + HORIZONTAL_MARGIN + functionalBlockH * size + DISTANCE_BETWEEN, serviceAreaEntryJ * size + SERVICE_AREA_VERTICAL_MARGIN_TOP);
                }
                serviceAreaEntryI = i;
                serviceAreaEntryJ = j;
                break;
            case Departure:
                serviceArea[i][j] = new Departure(graphicsContext);
                if (serviceAreaDepartureI != -1 && serviceAreaDepartureJ != -1) {
                    serviceArea[serviceAreaDepartureI][serviceAreaDepartureJ] = new Road(graphicsContext);
                    drawServiceAreaFunctionalBlock(serviceAreaDepartureI * size + HORIZONTAL_MARGIN + functionalBlockH * size + DISTANCE_BETWEEN, serviceAreaDepartureJ * size + SERVICE_AREA_VERTICAL_MARGIN_TOP);
                }
                serviceAreaDepartureI = i;
                serviceAreaDepartureJ = j;
                break;
            case GasolinePump:
                break;
            case GasolineTank:
                serviceArea[i][j] = new GasolineTank(graphicsContext);
                break;
        }
    }

    public void drawFunctionalBlocks() {
        //gas station
        for (int i = 0; i < functionalBlockH; i++) {
            for (int j = 0; j < functionalBlockV; j++) {
                if (gasStation[i][j] != null) {
                    graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1, GAS_STATION_VERTICAL_MARGIN_TOP + j * size + 1, size - 2, size - 2);
                    gasStation[i][j].render(HORIZONTAL_MARGIN + i * size + 1, GAS_STATION_VERTICAL_MARGIN_TOP + j * size + 1, size - 1);
                }
            }
        }

        //service area
        for (int i = 0; i < serviceBlockH; i++) {
            for (int j = 0; j < serviceBlockV; j++) {
                if (serviceArea[i][j] != null) {
                    graphicsContext.clearRect(HORIZONTAL_MARGIN + i * size + 1 + (functionalBlockH * size) + DISTANCE_BETWEEN, SERVICE_AREA_VERTICAL_MARGIN_TOP + j * size + 1, size - 2, size - 2);
                    serviceArea[i][j].render(HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN + i * size + 1, SERVICE_AREA_VERTICAL_MARGIN_TOP + j * size + 1, size - 1);
                }
            }
        }
    }

    public void drawFunctionalBlocksInModeling() {

        //gas station
        for (int i = 0; i < functionalBlockH; i++) {
            for (int j = 0; j < functionalBlockV; j++) {
                if (gasStation[i][j] != null) {
                    gasStation[i][j].render(HORIZONTAL_MARGIN + i * size + 1, GAS_STATION_VERTICAL_MARGIN_TOP + j * size, size);
                }
            }
        }

        //service area
        for (int i = 0; i < serviceBlockH; i++) {
            for (int j = 0; j < serviceBlockV; j++) {
                if (serviceArea[i][j] != null) {
                    serviceArea[i][j].render(HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN + i * size + 1, SERVICE_AREA_VERTICAL_MARGIN_TOP + j * size, size);
                }
            }
        }
    }

    //Отрисовка шоссе
    public void drawHighway() {
        String highwayImagePath = "pictures/highway_road.jpg";
        Image highwayImage = new Image(highwayImagePath);
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / HIGHWAY_SIZE; i++) {
            graphicsContext.drawImage(highwayImage, 2 * i * size, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size + 1, HIGHWAY_SIZE, HIGHWAY_SIZE);
        }
    }

    public void drawHighwayInModeling() {
        String imagePath = "pictures/highway_road.jpg";
        Image image = new Image(imagePath);
        for (int i = 0; i < gasStationEntryI + HORIZONTAL_MARGIN / size; i++) {
            graphicsContext.drawImage(image, i * size, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        for (int i = gasStationEntryI + 1 + (int) (Math.floor(HORIZONTAL_MARGIN / size)); i < gasStationDepartureI + (int) (Math.ceil(HORIZONTAL_MARGIN / size)); i++) {
            graphicsContext.drawImage(image, i * size, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        for (int i = gasStationDepartureI + 1 + (int) HORIZONTAL_MARGIN / size; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            graphicsContext.drawImage(image, i * size, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size, size, HIGHWAY_SIZE);
        }
        imagePath = "pictures/highway_road_ed.jpg";
        image = new Image(imagePath);
        graphicsContext.drawImage(image, gasStationEntryI * size + HORIZONTAL_MARGIN, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size, size, HIGHWAY_SIZE);
        graphicsContext.drawImage(image, gasStationDepartureI * size + HORIZONTAL_MARGIN, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size, size, HIGHWAY_SIZE);

//        graphicsContext.clearRect(functionalBlockH * size + HORIZONTAL_MARGIN + DISTANCE_BETWEEN + serviceAreaEntryI * size + 1,
//                SERVICE_AREA_VERTICAL_MARGIN_TOP + serviceBlockV * size + 0.5, size - 1, HIGHWAY_SIZE);

        graphicsContext.drawImage(image, functionalBlockH * size + HORIZONTAL_MARGIN + DISTANCE_BETWEEN + serviceAreaEntryI * size,
                SERVICE_AREA_VERTICAL_MARGIN_TOP + serviceBlockV * size + 0.5, size + 1, HIGHWAY_SIZE);


//        graphicsContext.clearRect(functionalBlockH * size + HORIZONTAL_MARGIN + DISTANCE_BETWEEN + serviceAreaDepartureI * size + 1,
//                SERVICE_AREA_VERTICAL_MARGIN_TOP + serviceBlockV * size + 0.5, size - 1, HIGHWAY_SIZE);

        graphicsContext.drawImage(image, functionalBlockH * size + HORIZONTAL_MARGIN + DISTANCE_BETWEEN + serviceAreaDepartureI * size,
                SERVICE_AREA_VERTICAL_MARGIN_TOP + serviceBlockV * size + 0.5, size + 1, HIGHWAY_SIZE);
    }

    public boolean isInGasStation(int x, int y) {
        return x > HORIZONTAL_MARGIN && x < HORIZONTAL_MARGIN + functionalBlockH * size
                && y > GAS_STATION_VERTICAL_MARGIN_TOP && y < GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size;
    }

    public boolean isInServiceArea(int x, int y) {
        return x > HORIZONTAL_MARGIN + functionalBlockH * size + DISTANCE_BETWEEN && x < HORIZONTAL_MARGIN + functionalBlockH * size + DISTANCE_BETWEEN + serviceBlockH * size
                && y > GAS_STATION_VERTICAL_MARGIN_TOP && y < GAS_STATION_VERTICAL_MARGIN_TOP + serviceBlockV * size;
    }

    public void drawInfoTableInModeling(int count) {
        ((InfoTable) gasStation[infoTableI][infoTableJ]).renderInModeling(HORIZONTAL_MARGIN + infoTableI * size, GAS_STATION_VERTICAL_MARGIN_TOP + infoTableJ * size, size, count);
    }

    public void drawBackground() {
        String imagePath = "pictures/tree.png";
        Image treeImage = new Image(imagePath);
        imagePath = "pictures/lawn2.png";
        Image lawnImage = new Image(imagePath);
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            for (int j = 0; j < graphicsContext.getCanvas().getHeight() / size; j++) {
                graphicsContext.drawImage(lawnImage, i * size, j * size, size, size);
                //graphicsContext.drawImage(image,i*size, j*size,size,size);
            }
        }
        for (int i = -1; i < 1 + functionalBlockH; i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN + i * size, GAS_STATION_VERTICAL_MARGIN_TOP - size, size, size);
        }
        for (int i = -1; i < 1 + serviceBlockH; i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN + (functionalBlockH * size) + DISTANCE_BETWEEN + i * size, SERVICE_AREA_VERTICAL_MARGIN_TOP - size, size, size);
        }
        for (int i = 0; i < functionalBlockV; i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN - size, GAS_STATION_VERTICAL_MARGIN_TOP + size * i, size, size);
        }
        for (int i = 0; i < Math.max(functionalBlockV, serviceBlockV); i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN + functionalBlockH * size, Math.min(GAS_STATION_VERTICAL_MARGIN_TOP, SERVICE_AREA_VERTICAL_MARGIN_TOP) + size * i, size, size);
        }
        for (int i = 0; i < serviceBlockV; i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN + (functionalBlockH + serviceBlockH) * size + DISTANCE_BETWEEN, SERVICE_AREA_VERTICAL_MARGIN_TOP + size * i, size, size);
        }
        for (int i = 1; i <= HORIZONTAL_MARGIN / size + 1; i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN - i * size, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size - size, size, size);
        }
        for (int i = 0; i < HORIZONTAL_MARGIN / size; i++) {
            graphicsContext.drawImage(treeImage, HORIZONTAL_MARGIN + (functionalBlockH + serviceBlockH) * size + i * size + DISTANCE_BETWEEN, GAS_STATION_VERTICAL_MARGIN_TOP + functionalBlockV * size - size, size, size);
        }
        for (int i = 0; i < graphicsContext.getCanvas().getWidth() / size; i++) {
            graphicsContext.drawImage(treeImage, i * size, GAS_STATION_VERTICAL_MARGIN_TOP + (functionalBlockV + serviceBlockH) * size + 2 * size + DISTANCE_BETWEEN, size, size);
        }
    }

    //Отрисовка номеров бензоколонок
    public void drawGasolinePumpNumbers() {
        for (int j = 0, number = 1; j < functionalBlockV; j++)
            for (int i = 0; i < functionalBlockH; i++) {
                if (gasStation[i][j] instanceof GasolinePump) {
                    graphicsContext.setFill(Color.web("#000000"));
                    graphicsContext.setFont(Font.font("Arial", size / 3));
                    ((GasolinePump) gasStation[i][j]).setNumber(number);
                    if (number < 10) {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + size / 2 - size / 6.998138688 + 3, j * size + GAS_STATION_VERTICAL_MARGIN_TOP + size / 2 + size / 6 + 2);
                    } else {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + size / 2 - size / 3.5 + 3, j * size + GAS_STATION_VERTICAL_MARGIN_TOP + size / 2 + size / 6 + 2);
                    }

                    graphicsContext.setFill(Color.BLACK);
                    number++;
                }
            }
    }

    //Отрисовка номеров бензоколонок
    public void drawGasolineTankNumbers() {
        for (int j = 0, number = 1; j < serviceBlockH; j++)
            for (int i = 0; i < serviceBlockV; i++) {
                if (serviceArea[i][j] instanceof GasolineTank) {
                    graphicsContext.setFill(Color.web("#000000"));
                    graphicsContext.setFont(Font.font("Arial", size / 3));
                    ((GasolineTank) serviceArea[i][j]).setNumber(number);
                    if (number < 10) {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + DISTANCE_BETWEEN + functionalBlockH * size + size / 2 - size / 6.998138688 + 3, j * size + SERVICE_AREA_VERTICAL_MARGIN_TOP + size / 2 + size / 6);
                    } else {
                        graphicsContext.fillText(Integer.toString(number), i * size + HORIZONTAL_MARGIN + DISTANCE_BETWEEN + functionalBlockH * size + size / 2 - size / 3.5 + 3, j * size + SERVICE_AREA_VERTICAL_MARGIN_TOP + size / 2 + size / 6);
                    }

                    graphicsContext.setFill(Color.BLACK);
                    number++;
                }
            }
    }

    public double getHORIZONTAL_MARGIN() {
        return HORIZONTAL_MARGIN;
    }

    public double getGAS_STATION_VERTICAL_MARGIN_TOP() {
        return GAS_STATION_VERTICAL_MARGIN_TOP;
    }
}
