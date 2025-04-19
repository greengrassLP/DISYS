package com.energy.api.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class EnergyController {

    @FXML
    private Label labelCommunityPool;

    @FXML
    private Label labelGridPortion;

    @FXML
    private Button btnRefresh;

    @FXML
    private Label labelChooseStart;

    @FXML
    private Label labelChooseEnd;

    @FXML
    private DatePicker dpStart;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private TextField tfStart;

    @FXML
    private TextField tfEnd;

    @FXML
    private Button btnShowData;

    @FXML
    private Label CommunityProduced;

    @FXML
    private Label labelCommunityUsed;

    @FXML
    private Label labelGridUsed;

}
