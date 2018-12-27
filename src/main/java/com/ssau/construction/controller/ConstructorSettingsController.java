package com.ssau.construction.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;

public class ConstructorSettingsController {
    @FXML
    private Spinner<Integer> gasStationWidth;
    @FXML
    private Spinner<Integer> gasStationHeight;
    @FXML
    private Spinner<Integer> serviceHeight;
    @FXML
    private Spinner<Integer> serviceWidth;

    private Stage dialogStage;
    private boolean isSubmitClicked = false;


    public int getGasStationWidth() {
        return gasStationWidth.getValue();
    }

    public int getGasStationHeight() {
        return gasStationHeight.getValue();
    }

    public int getServiceHeight() {
        return serviceHeight.getValue();
    }

    public int getServiceWidth() {
        return serviceWidth.getValue();
    }

    public void setInitialParkingWidth(int width) {
        gasStationWidth.getValueFactory().setValue(width);
    }

    public void setInitialParkingHeight(int height) {
        gasStationHeight.getValueFactory().setValue(height);
    }

    public void setInitialServiceWidth(int width) {
        serviceWidth.getValueFactory().setValue(width);
    }

    public void setInitialServiceHeight(int height) {
        serviceHeight.getValueFactory().setValue(height);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSubmitClicked() {
        return isSubmitClicked;
    }

    @FXML
    public void onSubmit() {
        isSubmitClicked = true;
        dialogStage.close();
    }
}

