package com.example.demo3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import models.Commande;
import models.Panier;
import services.UserSessionService;
import utils.MyDatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Commandeview implements Initializable {
    @FXML
    private TableView<Commande> commandeTableView;
    @FXML
    private TableColumn<Commande, Integer> commandeIdColumn;
    @FXML
    private TableColumn<Commande, Integer> panierIdColumn;
    @FXML
    private TableColumn<Commande, Integer> totalColumn;

    private Connection conn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = MyDatabase.getInstance().getConnection();
        if (conn == null) {
            System.err.println("Failed to obtain database connection.");
            return;
        }

        commandeIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        // Using Callback for more complex property access
        panierIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Commande, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Commande, Integer> param) {
                return new SimpleIntegerProperty(param.getValue().getPanier().getId()).asObject();
            }
        });

        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty().asObject());

        loadConfirmedOrders();
    }

    private void loadConfirmedOrders() {
        int loggedInUserId = UserSessionService.getInstance().getLoggedInUserId();
        String query = "SELECT * FROM commande WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, loggedInUserId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int panierId = rs.getInt("pani_id");
                int total = rs.getInt("totale");

                Panier panier = getPanierById(panierId);
                Commande commande = new Commande(id, total, loggedInUserId, panier);
                commandeTableView.getItems().add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Panier getPanierById(int panierId) throws SQLException {
        String panierQuery = "SELECT p.id AS panier_id, p.owner_id " +
                "FROM produit_panier pp " +
                "JOIN panier p ON pp.panier_id = p.id " +
                "WHERE pp.panier_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(panierQuery)) {
            pstmt.setInt(1, panierId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("panier_id");
                int ownerId = rs.getInt("owner_id");


                // Fetch other panier details as needed
                return new Panier(id, ownerId);
            }
        }
        return null;
    }


}
