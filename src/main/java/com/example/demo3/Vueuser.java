package com.example.demo3;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Produit;
import services.ServicePanier;
import services.UserSessionService;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Vueuser extends Application implements Initializable {

    private int loggedInUserId;
    private Produit selectedProduct;

    @FXML
    private Button panier;

    @FXML
    private Button addtocart;

    @FXML
    private GridPane container;

    private Pane selectedPane;

    private Connection connection = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("vueuser.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vueuser");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServicePanier servicePanier = new ServicePanier();

        // Initialize loggedInUserId from UserSessionService
        loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();

        List<Produit> produits = fetchProductsFromDatabase();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50);
        gridPane.setVgap(50);

        for (int i = 0; i < produits.size(); i++) {
            Produit produit = produits.get(i);
            Pane productPane = createProductPane(produit);
            gridPane.add(productPane, i % 3, i / 3);
        }

        container.getChildren().add(gridPane);

        addtocart.setOnAction(event -> {
            if (selectedProduct != null) {
                if (loggedInUserId > 0) {
                    addToCart(selectedProduct);
                    System.out.println("Product added to cart successfully.");
                    // You can navigate to the cart page here if needed
                } else {
                    System.out.println("Cannot add product to cart: User is not logged in");
                    // You might want to show an alert or redirect to the login page
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a product first.");
                alert.showAndWait();
            }
        });
    }

    private Pane createProductPane(Produit produit) {
        Pane productPane = new Pane();
        productPane.setPrefSize(200, 250);
        productPane.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 10px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.0, 0, 2);");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(120);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(true);
        try {
            imageView.setImage(new Image(produit.getImage()));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return productPane;
        }

        double imageX = (productPane.getPrefWidth() - imageView.getFitWidth()) / 2;
        double imageY = 20;
        imageView.setLayoutX(imageX);
        imageView.setLayoutY(imageY);

        Label nameLabel = new Label(produit.getNom());
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(180);
        nameLabel.setLayoutX((productPane.getPrefWidth() - nameLabel.getMaxWidth()) / 2);
        nameLabel.setLayoutY(imageY + imageView.getFitHeight() + 10);
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label priceLabel = new Label("Price: $" + produit.getPrix());
        priceLabel.setLayoutX((productPane.getPrefWidth() - priceLabel.getWidth()) / 2);
        priceLabel.setLayoutY(nameLabel.getLayoutY() + nameLabel.getHeight() + 5);
        priceLabel.setStyle("-fx-font-size: 12px;");

        productPane.getChildren().addAll(imageView, nameLabel, priceLabel);

        productPane.setOnMouseClicked(event -> {
            selectedProduct = produit;
            highlightProductPane(productPane);
        });

        return productPane;
    }

    private void highlightProductPane(Pane pane) {
        if (selectedPane != null) {
            selectedPane.setStyle("-fx-background-color: transparent;");
        }
        pane.setStyle("-fx-background-color: lightblue; -fx-border-color: blue; -fx-border-width: 2px;");
        selectedPane = pane;
    }

    @FXML
    private void redirectToPanier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("panier.fxml"));
            Parent panierRoot = loader.load();

            // Set the scene
            Scene panierScene = new Scene(panierRoot);
            Stage primaryStage = (Stage) panier.getScene().getWindow(); // Get the primary stage
            primaryStage.setScene(panierScene);
            primaryStage.setTitle("Panier");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading panier.fxml: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        }
    }

    private List<Produit> fetchProductsFromDatabase() {
        List<Produit> produits = new ArrayList<>();

        try {
            connection = MyDatabase.getInstance().getConnection();
            pst = connection.prepareStatement("SELECT * FROM produit");
            rs = pst.executeQuery();

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
            System.err.println("Error fetching products from database: " + e.getMessage());
            e.printStackTrace();
        } finally {
           // closeResources();
        }

        return produits;
    }

    private void addToCart(Produit produit) {
        // Check if user is logged in
        int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();
        if (loggedInUserId <= 0) {
            System.err.println("Cannot add product to cart: User is not logged in");
            // Display an error message or redirect to the login page
            return;
        }

        // Perform adding to cart operation
        if (checkProductInCart(loggedInUserId, produit.getId())) {
            System.out.println("Product is already in the cart.");
        } else {
            insertProduitPanier(loggedInUserId, produit.getId());
            System.out.println("Product added to cart successfully.");
        }
    }

    private void insertProduitPanier(int loggedInUserId, int produitId) {
        String selectPanierSQL = "SELECT id FROM panier WHERE owner_id = ?";
        String insertPanierSQL = "INSERT INTO panier (owner_id) VALUES (?)";
        String insertProduitPanierSQL = "INSERT INTO produit_panier (panier_id, produit_id) VALUES (?, ?)";

        try {
            connection = MyDatabase.getInstance().getConnection();

            // Check if the user has a cart (panier) already created
            int panierId = -1;
            pst = connection.prepareStatement(selectPanierSQL);
            pst.setInt(1, loggedInUserId);
            rs = pst.executeQuery();
            if (rs.next()) {
                panierId = rs.getInt("id");
            }

            if (panierId <= 0) {
                // If no cart exists, create one
                pst = connection.prepareStatement(insertPanierSQL, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, loggedInUserId);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = pst.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        panierId = generatedKeys.getInt(1);
                    }
                }
            }

            if (panierId <= 0) {
                System.err.println("Error inserting into panier: Could not create or find cart.");
                return;  // Exit if there's still no cart ID
            }

            // Now insert into produit_panier
            pst = connection.prepareStatement(insertProduitPanierSQL);
            pst.setInt(1, panierId);
            pst.setInt(2, produitId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added to cart successfully.");
            } else {
                System.err.println("Failed to insert into produit_panier.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting into panier/produit_panier: " + e.getMessage());
            e.printStackTrace();
        } finally {
            //closeResources();
        }
    }

    private int getPanierIdForUser(int userId) throws SQLException {
        String sqlSelect = "SELECT id FROM panier WHERE owner_id = ?";
        String sqlInsert = "INSERT INTO panier (owner_id) VALUES (?)";

        int panierId = -1;  // Default value if no cart found

        try {
            // Check if the user already has a cart
            pst = connection.prepareStatement(sqlSelect);
            pst.setInt(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
                panierId = rs.getInt("id");
            } else {
                // User doesn't have a cart, create one
                pst = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, userId);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    ResultSet generatedKeys = pst.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        panierId = generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting/creating panier: " + e.getMessage());
            e.printStackTrace();
        }

        return panierId;
    }

    private boolean checkProductInCart(int loggedInUserId, int productId) {
        String sql = "SELECT COUNT(*) FROM produit_panier WHERE panier_id = ? AND produit_id = ?";
        try {
            connection = MyDatabase.getInstance().getConnection();
            pst = connection.prepareStatement(sql);
            pst.setInt(1, loggedInUserId);
            pst.setInt(2, productId);
            rs = pst.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking product in cart: " + e.getMessage());
            e.printStackTrace();
        } finally {
          //  closeResources();
        }
        return false;
    }

    private void closeResources() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
