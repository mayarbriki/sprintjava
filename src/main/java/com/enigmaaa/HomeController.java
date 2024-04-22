package com.enigmaaa;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class HomeController {

    @FXML
    private Button BlogClient;

    @FXML
    private Button Stock;

    @FXML
    private Button blogAdmin;

    @FXML
    void initialize() {
        BlogClient.setOnAction(event -> loadScene("ClientBlog.fxml"));
        Stock.setOnAction(event -> loadScene("StockList.fxml"));
        blogAdmin.setOnAction(event -> loadScene("BlogList.fxml"));
    }

    private void loadScene(String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            Stage stage = (Stage) BlogClient.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
