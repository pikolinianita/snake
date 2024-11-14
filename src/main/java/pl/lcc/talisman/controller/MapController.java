package pl.lcc.talisman.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import pl.lcc.talisman.engine.Engine;

public class MapController {

    @FXML
    TilePane mapPane;

    private TheMap theMap;

    public void initialize(){

    }

    @FXML
    public void lowClick() {
        theMap = Engine.getInstance().initializeAndCreateMap(13,11, 50, 30, this::updateMap);
        mapPane.getChildren().addAll(theMap.getTiles());
        mapPane.getScene().setOnKeyPressed((type)-> Engine.getInstance().processKey(type.getCode()));
    }

    void updateMap(){
       Platform.runLater(()-> {
           mapPane.getChildren().clear();
           mapPane.getChildren().addAll(theMap.getTiles());
        });
    }
}