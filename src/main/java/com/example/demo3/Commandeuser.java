package com.example.demo3;
import  com.example.demo3.Paniercontroller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Commande;
import services.UserSessionService;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;


public class Commandeuser implements Initializable{
    @FXML
    private Button payer;
    @FXML
    private TableView<Commande> tableView1;

    @FXML
    private TableColumn<Commande, Integer> colId;

    @FXML
    private TableColumn<Commande, Integer> colPaniId;

    @FXML
    private TableColumn<Commande, Integer> colUserId;

    @FXML
    private TableColumn<Commande, Integer> colTotal;

    private final ObservableList<Commande> commandes = FXCollections.observableArrayList();
    public void setCommandes(List<Commande> commandes) {
        this.commandes.setAll(commandes);  // Set all commandes to refresh the list
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize your TableView and TableColumns here
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colPaniId.setCellValueFactory(cellData -> cellData.getValue().pani_idProperty().asObject());
        colUserId.setCellValueFactory(cellData -> cellData.getValue().userIdProperty().asObject());
        colTotal.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        // Populate TableView with data from the database
        tableView1.setItems(fetchCommandesFromDatabase());
    }

    private ObservableList<Commande> fetchCommandesFromDatabase() {
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = MyDatabase.getInstance().getConnection();
            if (conn == null) {
                System.err.println("Failed to obtain database connection.");
                return commandes; // Return empty list if connection is not established
            }

            // Query to fetch commandes for the logged-in user
            String query = "SELECT * FROM commande WHERE user_id = ?";
            int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();

            pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, loggedInUserId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int paniId = rs.getInt("pani_id");
                int total = rs.getInt("totale");

                // Create Commande object and add it to the list
                Commande commande = new Commande(id, paniId, loggedInUserId, total);
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching commandes from database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(pstmt, rs); // Close resources after use
        }

        return commandes;
    }

    // Method to close PreparedStatement and ResultSet
    private void closeResources(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }
    private ObservableList<Commande> getSampleCommandes() {
        ObservableList<Commande> commandes = FXCollections.observableArrayList();
        // Add sample data (replace with actual data retrieval logic)

        return commandes;
    }
    @FXML
    private void open() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PayementForm.fxml"));
            Parent root = loader.load();

            // Show the loaded scene
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            // Handle the error (e.g., show an error dialog)
        }
    }

    // Other methods and event handlers as needed
}
