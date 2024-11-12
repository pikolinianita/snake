package pl.lcc.talisman.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import pl.lcc.talisman.TheApp;
import pl.lcc.talisman.engine.Engine;

public class MapController {
    @FXML
    private Label welcomeText;
    @FXML
    private Scene scene;

    @FXML
    TilePane mapPane;
    private TheMap theMap;

    public void initialize(){
        System.out.println("Scene");
        System.out.println(TheApp.mainScene);
        System.out.println(scene);
        System.out.println(mapPane.getScene());

        var btn = new Button();
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void mapClicked(MouseEvent mouseEvent) {
        System.out.println("tile Clicked");
        System.out.println(mapPane.getScene());
    }

    public void lowClick(ActionEvent actionEvent) {
        System.out.println("low");
        theMap = Engine.getInstance().createMap();
        Engine.getInstance().setUpdateCallBack(this::updateMap);
        mapPane.getChildren().addAll(theMap.getTiles());
        mapPane.getScene().setOnKeyPressed((type)-> Engine.getInstance().processKey(type.getCode()));
    }

    public void onChoice(ActionEvent actionEvent) {
        System.out.println("choice");
    }

    void updateMap(){
        System.out.println("update");
       Platform.runLater(()-> {
           mapPane.getChildren().clear();
           mapPane.getChildren().addAll(theMap.getTiles());
        });
    }
}