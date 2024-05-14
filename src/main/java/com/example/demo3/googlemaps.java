package com.example.demo3;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.util.Objects;

public class googlemaps extends Application {

    @Override
    public void start(Stage primaryStage) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        webEngine.load(Objects.requireNonNull(getClass().getResource("/com/example/demo3/googlemaps.html")).toExternalForm());

        StackPane root = new StackPane();
        root.getChildren().add(webView);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Delivery Tracking Map");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

