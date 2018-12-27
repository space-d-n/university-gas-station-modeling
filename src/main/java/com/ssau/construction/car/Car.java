package com.ssau.construction.car;

import com.ssau.construction.util.GasStationGraph;
import com.ssau.construction.util.Node;
import com.ssau.construction.util.ServiceAreaGraph;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public abstract class Car extends ImageView {

    private String arrivalTime;
    private String departureTime;

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public GasStationGraph.GasolinePumpNode getGasolinePumpNode() {
        return gasolinePumpNode;
    }

    public ServiceAreaGraph.GasolineTankNode getGasolineTankNode() {
        return gasolineTankNode;
    }

    private GasStationGraph.GasolinePumpNode gasolinePumpNode;
    private ServiceAreaGraph.GasolineTankNode gasolineTankNode;

    public Car(double x, double y, String path, int width, int height) {
        super(path);
        setFitWidth(width);
        setFitHeight(height);
        setX(x);
        setY(y);
    }

    public abstract double getRate();

    public abstract String getType();

    public ArrayList<Node> getPathToGasStationEntry() {
        return gasolinePumpNode.getPathToEntry();
    }

    public ArrayList<Node> getPathToGasStationDeparture() {
        return gasolinePumpNode.getPathToDeparture();
    }

    public void setGasolinePumpNode(GasStationGraph.GasolinePumpNode gasolinePumpNode) {
        this.gasolinePumpNode = gasolinePumpNode;
    }

    public ArrayList<Node> getPathToServiceAreaEntry() {
        return gasolineTankNode.getPathToEntry();
    }

    public ArrayList<Node> getPathToServiceAreaDeparture() {
        return gasolineTankNode.getPathToDeparture();
    }

    public void setGasolineTankNode(ServiceAreaGraph.GasolineTankNode gasolineTankNode) {
        this.gasolineTankNode = gasolineTankNode;
    }
}
