package pl.lcc.talisman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TheApp extends Application {

    public static Scene mainScene;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TheApp.class.getResource("map-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 800);
        mainScene = scene;
        stage.setTitle("Snake!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}