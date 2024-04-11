package com.example.demo3;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Panier;
import utils.MyDatabase;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Paniercontroller implements Initializable {
    @FXML
    private TableColumn<Panier, String> produitImage;
    @FXML
    private TableColumn<Panier, Integer> panier_id;
    @FXML
    private TableView<Panier> tableView;
    @FXML
    private TableColumn<Panier, String> produitNom;
    @FXML
    private TableColumn<Panier, Integer> produitPrix;

    private Connection conn; // Database connection

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure the cells to display the appropriate data
        panier_id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        produitNom.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProduitNom()));
        produitPrix.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProduitPrix()));

        // Initialize database connection
        conn = MyDatabase.getInstance().getConnection();
        if (conn == null) {
            System.err.println("Failed to obtain database connection.");
            return; // Do not proceed if connection is not established
        }

        // Initialize the image column cell factory
        produitImage.setCellValueFactory(new PropertyValueFactory<>("produitImage"));
        produitImage.setCellFactory(column -> {
            return new TableCell<Panier, String>() {
                private final ImageView imageView = new ImageView();

                {
                    // Set dimensions for the ImageView
                    imageView.setFitWidth(50);
                    imageView.setFitHeight(50);
                    // Set how the image should be resized
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView); // Set the ImageView as the graphic for the cell
                }

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);
                    if (empty || imagePath == null) {
                        imageView.setImage(null); // Clear the image if no path is provided
                    } else {
                        // Load the image directly from the file path
                        Image image = new Image(new File(imagePath).toURI().toString());
                        imageView.setImage(image); // Set the loaded image to the ImageView
                    }
                }
            };
        });


        // Load panier data from the database
        loadPanierDataFromDatabase();
    }

    private void loadPanierDataFromDatabase() {
        try {
            String query = "SELECT p.panier_id, pr.nom AS produit_nom, pr.prix AS produit_prix, pr.image AS produit_image " +
                    "FROM produit_panier p " +
                    "JOIN produit pr ON p.produit_id = pr.id";

            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int panierId = rs.getInt("panier_id");
                    String produitNom = rs.getString("produit_nom");
                    int produitPrix = rs.getInt("produit_prix");
                    String produitImage = rs.getString("produit_image");

                    // Debug output to check the retrieved image path
                    System.out.println("Image path from database: " + produitImage);

                    // Create Panier object and add it to TableView
                    Panier panier = new Panier(panierId, produitNom, produitPrix, produitImage);
                    tableView.getItems().add(panier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
