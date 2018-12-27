package com.ssau.construction.controller;

import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class AboutProjectController {
    @FXML
    WebView webViewProject;

    @FXML
    public void initialize() {
        try {
            webViewProject.getEngine().load(getClass().getResource("/forms/AboutSystem.html").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
