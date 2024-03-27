package com.example.demo3;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Produit;
import utils.MyDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Vueuser extends Application {
    @FXML
    private Button addtocart;

    @Override
    public void start(Stage primaryStage) {
        List<Produit> produits = fetchProductsFromDatabase();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

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

        Scene scene = new Scene(gridPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void displayProductsFromDatabase() {
        List<Produit> produits = fetchProductsFromDatabase();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

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

}
