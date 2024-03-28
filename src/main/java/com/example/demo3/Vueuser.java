package com.example.demo3;
import services.UserSessionService;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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
import models.Panier;
import services.ServicePanier;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import  java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import services.panierService;

public class Vueuser extends Application implements Initializable {

    @FXML
    private Button addtocart;

    @FXML
    private GridPane container;

    // Track the currently selected product
    private Produit selectedProduct;
    private ServicePanier servicePanier;

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

    public static void main(String[] args) {
        launch(args);
    }

    private Pane selectedPane; // Track the currently selected pane

    // Method to highlight the selected product pane
    private void highlightProductPane(Pane pane) {
        // Remove highlight from previously selected pane
        if (selectedPane != null) {
            selectedPane.setStyle("-fx-background-color: transparent;"); // Remove highlight
        }

        // Add highlight to the newly selected pane
        pane.setStyle("-fx-background-color: lightblue; -fx-border-color: blue; -fx-border-width: 2px;"); // Add highlight
        selectedPane = pane;
    }


    private List<Produit> fetchProductsFromDatabase() {
        List<Produit> produits = new ArrayList<>();
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Get the connection
            connection = MyDatabase.getInstance().getConnection();

            // Create and execute the SQL statement
            pst = connection.prepareStatement("SELECT * FROM produit");
            rs = pst.executeQuery();

            // Process the ResultSet
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getInt("quantite"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        "file:" + rs.getString("image"),
                        rs.getString("prix")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
        }


        return produits;
    }


    public void addToCart(Produit produit) {
        int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId(); // Retrieve logged-in user ID

        if (loggedInUserId <= 0) {
            // Handle the case where the user is not logged in
            System.err.println("Cannot add product to cart: User is not logged in");
            return;
        }

        try (Connection cnx = MyDatabase.getInstance().getConnection()) {
            try (PreparedStatement panierStmt = cnx.prepareStatement("INSERT INTO panier (owner_id) VALUES (?)")) {
                panierStmt.setInt(1, loggedInUserId); // Set owner_id to logged-in user ID
                panierStmt.executeUpdate();
            }

            // Insert the product into the cart (assuming you have a separate table for the cart items)
            // For demonstration purposes, I'm skipping the code to insert the product into the cart
            // Replace this with your actual code to insert the product into the cart

            System.out.println("Product added to cart successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add product to cart: " + e.getMessage());
        }
    }

    private Panier getCurrentUserCart(int userId) {
        // Implement this method to retrieve the cart of the current user from the database
        // For demonstration purposes, you can create a new Panier instance
        return new Panier(); // Replace this with actual logic to fetch the user's cart from the database
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //  Connection connection = MyDatabase.getInstance().getConnection();
        servicePanier = new ServicePanier();

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
            Pane productPane = new Pane();
            productPane.setPrefSize(700, 200); // Increase height and width
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
                imageView.setImage(new Image(produit.getImage()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue; // Skip this product if image loading fails
            }

            nameLabel.setText(produit.getNom());
            descriptionLabel.setText(produit.getDescription());
            priceLabel.setText("Price: $" + produit.getPrix());

            // Add event handler to select the item
            int finalI = i;
            imageView.setOnMouseClicked(event -> {
                selectedProduct = produits.get(finalI); // Store the selected product
                System.out.println("Selected: " + selectedProduct.getNom());
                System.out.println("Selected: " + selectedProduct.getDescription());
                highlightProductPane(productPane); // Highlight the selected product pane
            });

            gridPane.add(productPane, 0, i);
            gridPane.add(imageView, 0, i);
            gridPane.add(nameLabel, 1, i);
            gridPane.add(descriptionLabel, 2, i);
            gridPane.add(priceLabel, 3, i);
        }

        // Assuming you have a container in your FXML file with fx:id "container"
        container.getChildren().add(gridPane);

        // Add event handler for the Add to Cart button
        addtocart.setOnAction(event -> {
            int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId(); // Retrieve logged-in user ID

          if (selectedProduct != null) {
              Panier currentUserCart = getCurrentUserCart(loggedInUserId);
              addToCart(selectedProduct); // Pass user ID and selected product         System.out.println("Product added to cart successfully.");
     } else {
           System.out.println("Please select a product first.");
           }
        });
    }


}



