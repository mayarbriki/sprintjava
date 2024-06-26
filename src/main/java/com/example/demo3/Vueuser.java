package com.example.demo3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Commande;
import models.Produit;
import services.ServicePanier;
import services.UserSessionService;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import org.controlsfx.control.Rating;  // Import Rating from ControlsFX

public class Vueuser extends Application implements Initializable {
    @FXML
    private Button chat;
    @FXML
    private Button rechbutton;
    @FXML
    private Button tricroissant;

    @FXML
    private Button tridecroissant;
    @FXML
    private TextField rechercher;
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
        rechbutton.setOnAction(event -> {
            String searchText = rechercher.getText().trim(); // Get the text from the TextField
            if (!searchText.isEmpty()) {
                searchProduct(searchText);
            } else {
                // Handle empty search text if needed
            }
        });

        // Your existing code continues...
        tricroissant.setOnAction(event -> {
            List<Produit> sortedProducts = sortProductsAscending(fetchProductsFromDatabase());
            updateProductGrid(sortedProducts);
        });

        // Event handler for sorting in descending order (tridecroissant button)
        tridecroissant.setOnAction(event -> {
            List<Produit> sortedProducts = sortProductsDescending(fetchProductsFromDatabase());
            updateProductGrid(sortedProducts);
        });

        // Your existing code continues...
    }

    private List<Produit> sortProductsAscending(List<Produit> products) {
        List<Produit> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort(Comparator.comparingDouble(produit -> Double.parseDouble(produit.getPrix())));
        return sortedProducts;
    }

    private List<Produit> sortProductsDescending(List<Produit> products) {
        List<Produit> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort((produit1, produit2) -> Double.compare(Double.parseDouble(produit2.getPrix()), Double.parseDouble(produit1.getPrix())));
        return sortedProducts;
    }

    private void updateProductGrid(List<Produit> products) {
        // Clear existing products from the grid pane
        container.getChildren().clear();

        // Create panes for the sorted products and add them to the grid pane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50);
        gridPane.setVgap(50);

        for (int i = 0; i < products.size(); i++) {
            Produit produit = products.get(i);
            Pane productPane = createProductPane(produit);
            gridPane.add(productPane, i % 3, i / 3);
        }

        container.getChildren().add(gridPane);
    }

    private void searchProduct(String searchText) {
        // Perform the search in your list of products
        List<Produit> matchingProducts = new ArrayList<>();
        for (Produit produit : fetchProductsFromDatabase()) {
            if (produit.getNom().toLowerCase().contains(searchText.toLowerCase())) {
                matchingProducts.add(produit);
            }
        }

        // Update the UI with the search results
        updateSearchResults(matchingProducts);
    }

    private void updateSearchResults(List<Produit> matchingProducts) {
        // Clear existing products from the grid pane
        container.getChildren().clear();

        // Create panes for the matching products and add them to the grid pane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(50);
        gridPane.setVgap(50);

        for (int i = 0; i < matchingProducts.size(); i++) {
            Produit produit = matchingProducts.get(i);
            Pane productPane = createProductPane(produit);
            gridPane.add(productPane, i % 3, i / 3);
        }

        container.getChildren().add(gridPane);
    }
    @FXML
    void redirectToChatBotUI(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatBotUI.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Print the exception trace for debugging
        }
    }

    private Pane createProductPane(Produit produit) {
        AnchorPane productPane = new AnchorPane(); // Use AnchorPane for flexible layout
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

        // Position elements using AnchorPane constraints
        AnchorPane.setTopAnchor(imageView, 20.0);
        AnchorPane.setLeftAnchor(imageView, (productPane.getPrefWidth() - imageView.getFitWidth()) / 2);

        Label nameLabel = new Label(produit.getNom());
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(180);

        Label priceLabel = new Label("Price: $" + produit.getPrix());
        priceLabel.setStyle("-fx-font-size: 12px;");

        Rating rating = new Rating();
        rating.setMax(5); // Set the maximum rating value
        rating.setRating(produit.getRating()); // Set the rating value from your Produit class
        rating.setDisable(false); // Disable user interaction with the Rating

        // Position nameLabel below imageView
        AnchorPane.setTopAnchor(nameLabel, 20 + imageView.getFitHeight() + 10);
        AnchorPane.setLeftAnchor(nameLabel, (productPane.getPrefWidth() - nameLabel.getMaxWidth()) / 2);

        // Position priceLabel below nameLabel
        AnchorPane.setTopAnchor(priceLabel, 20 + imageView.getFitHeight() + 10 + nameLabel.getHeight() + 5);
        AnchorPane.setLeftAnchor(priceLabel, (productPane.getPrefWidth() - priceLabel.getWidth()) / 2);

        // Position rating below priceLabel
        AnchorPane.setTopAnchor(rating, 20 + imageView.getFitHeight() + 10 + nameLabel.getHeight() + 5 + priceLabel.getHeight() + 5);
        AnchorPane.setLeftAnchor(rating, (productPane.getPrefWidth() - rating.getWidth()) / 2);

        productPane.getChildren().addAll(imageView, nameLabel, priceLabel, rating);

        productPane.setOnMouseClicked(event -> {
            selectedProduct = produit;
            highlightProductPane(productPane);
        });

        // Add change listener for rating
        rating.ratingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                double newRating = newValue.doubleValue();
                updateProductRatingInDatabase(produit.getId(), newRating);
            }
        });

        return productPane;
    }
    private void updateProductRatingInDatabase(int productId, double newRating) {
        String updateRatingSQL = "UPDATE produit SET rate = ? WHERE id = ?";

        try {
            connection = MyDatabase.getInstance().getConnection();
            pst = connection.prepareStatement(updateRatingSQL);
            pst.setDouble(1, newRating);
            pst.setInt(2, productId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product rating updated successfully.");
            } else {
                System.err.println("Failed to update product rating.");
            }
        } catch (SQLException ex) {
            System.err.println("Error updating product rating: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            //closeResources(); // Close resources if needed
        }
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
