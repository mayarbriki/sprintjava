package com.example.demo3;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Panier;
import utils.MyDatabase;
import services.UserSessionService;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Paniercontroller implements Initializable {
    @FXML
    private Button confirmer;
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
    @FXML
    private TableColumn<Panier, Integer> quantite;
    private Connection conn; // Database connection
    @FXML
    private Button modifier;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure the cells to display the appropriate data
        panier_id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        produitNom.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProduitNom()));
        produitPrix.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getProduitPrix()));
        quantite.setCellValueFactory(new PropertyValueFactory<>("quantite")); // Set cell value factory for quantite
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
            // Get the logged-in user's ID
            int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();

            // Construct the SQL query with the join and filter by owner_id
            String query = "SELECT p.panier_id, pr.nom AS produit_nom, pr.prix AS produit_prix, pr.image AS produit_image " +
                    "FROM produit_panier p " +
                    "JOIN produit pr ON p.produit_id = pr.id " +
                    "JOIN panier pa ON p.panier_id = pa.id " +
                    "WHERE pa.owner_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                // Set the owner_id parameter in the query
                pstmt.setInt(1, loggedInUserId);

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    int panierId = rs.getInt("panier_id");
                    String produitNom = rs.getString("produit_nom");
                    int produitPrix = rs.getInt("produit_prix");
                    String produitImage = rs.getString("produit_image");

                    // Get quantité from panier using the second query
                    int quantite = getQuantiteFromPanier(loggedInUserId, panierId);

                    // Debug output to check the retrieved image path
                    System.out.println("Image path from database: " + produitImage);

                    // Create Panier object and add it to TableView
                    Panier panier = new Panier(panierId, produitNom, produitPrix, produitImage);
                    panier.setQuantite(quantite); // Set the quantite property
                    tableView.getItems().add(panier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getQuantiteFromPanier(int loggedInUserId, int panierId) throws SQLException {
        String quantiteQuery = "SELECT pa.quantite " +
                "FROM panier pa " +
                "JOIN produit_panier p ON pa.id = p.panier_id " +
                "WHERE pa.owner_id = ? AND p.panier_id = ?";
        try (PreparedStatement pstmtQuantite = conn.prepareStatement(quantiteQuery)) {
            pstmtQuantite.setInt(1, loggedInUserId);
            pstmtQuantite.setInt(2, panierId);
            ResultSet rsQuantite = pstmtQuantite.executeQuery();
            if (rsQuantite.next()) {
                return rsQuantite.getInt("quantite");
            }
        }
        return 0; // Default value if quantité is not found
    }
    private void openUpdateDialog(Panier selectedPanier) {
        // Implement your dialog to update quantite here
        // You can use JavaFX dialog controls or create a custom dialog
        // For simplicity, let's assume you have a method updateQuantiteInDatabase in your MyDatabase class

        // Example: Open a TextInputDialog to update quantite
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedPanier.getQuantite()));
        dialog.setTitle("Update Quantite");
        dialog.setHeaderText("Enter new quantite:");
        dialog.setContentText("Quantite:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newQuantite -> {
            try {
                int newQuantiteValue = Integer.parseInt(newQuantite);
                selectedPanier.setQuantite(newQuantiteValue); // Update quantite in the TableView

                // Update quantite in the database
               // updateQuantityInDatabase();
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.err.println("Invalid quantite input: " + newQuantite);
            }
        });
    }
    @FXML
    private void updateQuantityInDatabase() {
        // Get the selected Panier item from the TableView
        Panier selectedPanier = tableView.getSelectionModel().getSelectedItem();
        if (selectedPanier == null) {
            // No item selected, handle this case if needed
            System.err.println("No item selected.");
            showAlert("Select a Product", "Please select a product first.");
            return;
        }

        // Open a dialog to get the new quantity from the user
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedPanier.getQuantite()));
        dialog.setTitle("Update Quantite");
        dialog.setHeaderText("Enter new quantite:");
        dialog.setContentText("Quantite:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newQuantite -> {
            try {
                int newQuantity = Integer.parseInt(newQuantite);

                // Update quantite in the TableView
                selectedPanier.setQuantite(newQuantity);

                // Update quantite in the database
                String sql = "UPDATE panier SET quantite = ? WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, newQuantity);
                    pstmt.setInt(2, selectedPanier.getId());
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Quantity updated in the database.");
                    } else {
                        System.out.println("Failed to update quantity in the database.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.err.println("Invalid quantite input: " + newQuantite);
            }
        });
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void deletePanierFromDatabase() {
        // Get the selected Panier item from the TableView
        Panier selectedPanier = tableView.getSelectionModel().getSelectedItem();
        if (selectedPanier == null) {
            // No item selected, handle this case if needed
            System.err.println("No item selected.");
            showAlert("Select a Product", "Please select a product first.");
            return;
        }

        // Confirm deletion with a dialog
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Are you sure you want to delete this panier?");
        confirmation.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion, proceed with deleting from the database
            String sql = "DELETE FROM panier WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selectedPanier.getId());
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    // Remove the item from the TableView if deletion was successful
                    tableView.getItems().remove(selectedPanier);
                    System.out.println("Panier deleted from the database.");
                } else {
                    System.out.println("Failed to delete panier from the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void confirmerCommande() {
        // Get the list of displayed "panier" items in the TableView
        ObservableList<Panier> paniers = tableView.getItems();

        if (paniers.isEmpty()) {
            showAlert("No Panier", "There are no paniers to confirm.");
            return;
        }

        // For simplicity, let's assume the first item in the list is the current "panier"
        Panier currentPanier = paniers.get(0); // You can adjust this based on your logic

        // Calculate the total sum of product prices in the cart
        int total = calculateTotalPrice(currentPanier);

        // Get the logged-in user's ID
        int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();

        // Insert data into the "commande" table
        String insertCommandeQuery = "INSERT INTO commande (pani_id, user_id, totale) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertCommandeQuery)) {
            pstmt.setInt(1, currentPanier.getId());
            pstmt.setInt(2, loggedInUserId);
            pstmt.setInt(3, total);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Commande inserted into the database.");
                // Optionally, you can clear the cart or update its status after confirmation
                // clearCart(currentPanier);

                // Navigate to the commandeview.fxml scene
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("commandeview.fxml"));
                    Parent root = loader.load();

                    // Create a new scene
                    Scene scene = new Scene(root);

                    // Get the current stage (window)
                    Stage stage = (Stage) confirmer.getScene().getWindow(); // Assuming confirmer is the Button

                    // Set the new scene on the stage
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle any exceptions that may occur during loading
                }
            } else {
                System.out.println("Failed to insert commande into the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private int calculateTotalPrice(Panier panier) {
        int total = 0;
        for (Panier item : tableView.getItems()) {
            total += item.getProduitPrix() * item.getQuantite();
        }
        return total;
    }

}




