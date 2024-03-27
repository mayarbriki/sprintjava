package com.example.demo3;

import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Produit;
import utils.MyDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Vueuser extends Application implements Initializable {
    @FXML
    private Button addtocart;
    @FXML
    private GridPane container;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vueuser.fxml"));
        Parent root = loader.load();

        // Set the scene
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vueuser");
        primaryStage.show();
    }


    public void displayProductsFromDatabase() {
        List<Produit> produits = fetchProductsFromDatabase();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(1);
        gridPane.setVgap(5);

        for (int i = 0; i < produits.size(); i++) {
            Produit produit = produits.get(i);
            ImageView imageView = new ImageView();
            Label nameLabel = new Label();
            Label descriptionLabel = new Label();
            Label priceLabel = new Label();

            try {
                // Load image from resource
                imageView.setImage(new Image(produit.getImage()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue; // Skip this product if image loading fails
            }

            nameLabel.setText(produit.getNom());
            descriptionLabel.setText(produit.getDescription());
            priceLabel.setText("Price: $" + produit.getPrix());

            gridPane.add(imageView, 0, i);
            gridPane.add(nameLabel, 1, i);
            gridPane.add(descriptionLabel, 2, i);
            gridPane.add(priceLabel, 3, i);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private List<Produit> fetchProductsFromDatabase() {
        List<Produit> produits = new ArrayList<>();

        try (Connection connection = MyDatabase.getInstance().getConnection()) {
            String sql = "SELECT * FROM produit";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Produit produit = new Produit(
                            rs.getInt("id"),
                            rs.getInt("quantite"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            "file:" + rs.getString("image"), // Prefix "file:" to create a file URL
                            rs.getString("prix")
                    );
                    produits.add(produit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
        }

        return produits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Produit> produits = fetchProductsFromDatabase();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Set column constraints to ensure labels have enough space
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(5); // Adjust as needed
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(5); // Adjust as needed
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(5); // Adjust as needed
        gridPane.getColumnConstraints().addAll(col1, col2, col3);

        for (int i = 0; i < produits.size(); i++) {
            Produit produit = produits.get(i);
            ImageView imageView = new ImageView();
            Label nameLabel = new Label();
            Label descriptionLabel = new Label();
            Label priceLabel = new Label();

            // Set preferred width and height for labels
            nameLabel.setPrefWidth(100); // Adjust as needed
            nameLabel.setPrefHeight(30); // Adjust as needed
            descriptionLabel.setPrefWidth(200); // Adjust as needed
            descriptionLabel.setPrefHeight(30); // Adjust as needed
            priceLabel.setPrefWidth(100); // Adjust as needed
            priceLabel.setPrefHeight(30); // Adjust as needed

            try {
                // Load image from resource
                imageView.setImage(new javafx.scene.image.Image(produit.getImage()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue; // Skip this product if image loading fails
            }

            nameLabel.setText(produit.getNom());
            descriptionLabel.setText(produit.getDescription());
            priceLabel.setText("Price: $" + produit.getPrix());

            gridPane.add(imageView, 0, i);
            gridPane.add(nameLabel, 1, i);
            gridPane.add(descriptionLabel, 2, i);
            gridPane.add(priceLabel, 3, i);
        }

        // Assuming you have a container in your FXML file with fx:id "container"
        container.getChildren().add(gridPane);
    }


}
