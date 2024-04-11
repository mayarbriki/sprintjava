package com.example.demo3;
import javafx.util.Pair;
import services.UserSessionService;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Produit;
import utils.MyDatabase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import models.Panier;
import services.ServicePanier;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Vueuser extends Application implements Initializable {
    private int loggedInUserId; // Class-level variable to store logged-in user ID
    @FXML
    private Button panier;

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
            // Handle database exception
            System.err.println("Error fetching products from database: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
        } finally {
            // Close resources in the finally block
            // ...
        }

        return produits;
    }

    private void insertProduitPanier(int loggedInUserId, int produitId) {
        String sql = "INSERT INTO produit_panier (panier_id, produit_id) VALUES (?, ?)";

        try (Connection connection = MyDatabase.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loggedInUserId);
            statement.setInt(2, produitId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Inserted into produit_panier: user=" + loggedInUserId + ", produit=" + produitId);
            } else {
                System.err.println("Failed to insert into produit_panier.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting into produit_panier: " + e.getMessage());
            // Handle the exception gracefully, such as displaying an error message to the user
            // You can also log the exception or perform any necessary cleanup
        }
    }


    private Pair<Integer, Panier> getCurrentUserCart() {
        int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();
        if (loggedInUserId <= 0) {
            System.err.println("User is not logged in.");
            return null;
        }

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Get the connection
            connection = MyDatabase.getInstance().getConnection();

            // Check if the user already has a cart
            pst = connection.prepareStatement("SELECT * FROM panier WHERE owner_id = ?");
            pst.setInt(1, loggedInUserId);
            rs = pst.executeQuery();

            // If the cart exists, retrieve its details
            if (rs.next()) {
                int cartId = rs.getInt("id");
                // You can fetch other details of the cart from the ResultSet if needed

                // Create a new Panier instance with the retrieved details
                Panier cart = new Panier();
                cart.setId(cartId);
                // Set other details of the cart if needed

                // Return the owner ID and the cart
                return new Pair<>(loggedInUserId, cart);
            } else {
                // If the cart doesn't exist, create a new one
                pst = connection.prepareStatement("INSERT INTO panier (owner_id) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                pst.setInt(1, loggedInUserId);
                int rowsAffected = pst.executeUpdate();
                if (rowsAffected > 0) {
                    rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        int cartId = rs.getInt(1);

                        // Create a new Panier instance for the newly created cart
                        Panier cart = new Panier();
                        cart.setId(cartId);
                        // Set other details of the cart if needed

                        // Return the owner ID and the newly created cart
                        return new Pair<>(loggedInUserId, cart);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("error");
         //   e.printStackTrace();
            // Handle database exception
        }


        // Return null if an error occurs or if no cart is found
        return null;
    }

    private List<Produit> cart = new ArrayList<>();
    public void addToCart(Produit produit) {
        // Check if a product is selected
        if (produit == null) {
            System.out.println("Please select a product first.");
            return;
        }

        // Check if a user is logged in
        if (loggedInUserId <= 0) {
            System.err.println("User is not logged in.");
            return;
        }

        // Retrieve or create the current user's cart
        Pair<Integer, Panier> currentUserCartPair = getCurrentUserCart();
        if (currentUserCartPair == null) {
            System.err.println("Cannot add product to cart: User doesn't have a cart.");
            return;
        }

        // Add the product to the cart in the database
        int cartId = currentUserCartPair.getValue().getId(); // Retrieve the cart ID
        insertProduitPanier(cartId, produit.getId()); // Insert the product into the cart

        // Update the local cart list (if needed)
        cart.add(produit);

       // System.out.println("Product added to cart successfully.");
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        servicePanier = new ServicePanier();

        List<Produit> produits = fetchProductsFromDatabase();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(150); // Horizontal gap between grid items
        gridPane.setVgap(150); // Vertical gap between grid items

        // Retrieve current user's cart
        Pair<Integer, Panier> currentUserCartPair = getCurrentUserCart();
        Panier currentUserCart;
        if (currentUserCartPair != null) {
            loggedInUserId = currentUserCartPair.getKey(); // Update the loggedInUserId
            currentUserCart = currentUserCartPair.getValue();
        } else {
            loggedInUserId = -1; // Set it to a default value indicating no user logged in
            currentUserCart = null;
        }

        for (int i = 0; i < produits.size(); i++) {
            Produit produit = produits.get(i);
            Pane productPane = new Pane();
            productPane.setPrefSize(200, 250); // Adjust size as needed
            productPane.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-border-radius: 5px;");

            ImageView imageView = new ImageView();
            imageView.setFitWidth(150); // Adjust image width
            imageView.setFitHeight(150); // Adjust image height
            imageView.setPreserveRatio(true); // Preserve aspect ratio
            try {
                // Load image from resource
                imageView.setImage(new Image(produit.getImage()));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                continue; // Skip this product if image loading fails
            }

            Label nameLabel = new Label(produit.getNom());
            nameLabel.setWrapText(true); // Wrap text if it exceeds the label width
            nameLabel.setMaxWidth(150); // Set max width for label
            nameLabel.setLayoutY(160); // Position label below image
            nameLabel.setStyle("-fx-font-weight: bold;");

            Label priceLabel = new Label("Price: $" + produit.getPrix());
            priceLabel.setLayoutY(200); // Position label below name
            priceLabel.setStyle("-fx-font-size: 12px;");

            productPane.getChildren().addAll(imageView, nameLabel, priceLabel);

            // Add event handler to select the item
            productPane.setOnMouseClicked(event -> {
                selectedProduct = produit; // Store the selected product
                System.out.println("Selected: " + selectedProduct.getId());
                System.out.println("Selected: " + selectedProduct.getNom());
                System.out.println("Selected: " + selectedProduct.getDescription());
                highlightProductPane(productPane); // Highlight the selected product pane
            });

            gridPane.add(productPane, i % 3, i / 3);
        }

        // Assuming you have a container in your FXML file with fx:id "container"
        container.getChildren().add(gridPane);

        // Add event handler for the Add to Cart button
        addtocart.setOnAction(event -> {
            if (selectedProduct != null) {
                if (loggedInUserId > 0) {
                    addToCart(selectedProduct);
                    System.out.println("Product added to cart successfully.");
                    // You can navigate to the cart page here if needed
                } else {
                    System.out.println("Cannot add product to cart: User is not logged in");
                }
            } else {
                System.out.println("Please select a product first.");
            }
        });

    }



}
