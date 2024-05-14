package services;

import models.Panier;
import models.Produit;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePanier implements panierService<Panier> {
    private Connection cnx = MyDatabase.getInstance().getConnection();
    public ServicePanier() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Panier panier) throws SQLException {
        String sql = "INSERT INTO panier (owner_id) VALUES (?)";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, panier.getOwner().getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void modifier(Panier panier) throws SQLException {
        String sql = "UPDATE panier SET owner_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, panier.getOwner().getId());
            preparedStatement.setInt(2, panier.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM panier WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<Panier> recuperer() throws SQLException {
        List<Panier> paniers = new ArrayList<>();
        String sql = "SELECT * FROM panier";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Panier panier = new Panier();
                panier.setId(resultSet.getInt("id"));
                // Set other properties as needed
                paniers.add(panier);
            }
        }
        return paniers;
    }
    public void addToCart(Panier panier, Produit produit) throws SQLException {
        try (PreparedStatement panierStmt = cnx.prepareStatement("INSERT INTO panier (id, owner_id) VALUES (?, ?)")) {
            panierStmt.setInt(1, panier.getId());
            panierStmt.setInt(2, panier.getOwner().getId()); // Assuming cart has an owner
            panierStmt.executeUpdate();

            try (PreparedStatement panierProduitStmt = cnx.prepareStatement("INSERT INTO panier_produit (panier_id, produit_id) VALUES (?, ?)")) {
                panierProduitStmt.setInt(1, panier.getId());
                panierProduitStmt.setInt(2, produit.getId());
                panierProduitStmt.executeUpdate();
            }
        }
    }
    public void savePanier(Produit[] cart) throws SQLException {
        StringBuilder sql = new StringBuilder("INSERT INTO panier(produit_name) VALUES ");
        boolean first = true;
        int productsAdded = 0;

        // Construct the VALUES part of the SQL statement
        for (Produit produit : cart) {
            if (produit != null) {
                if (!first) {
                    sql.append(", ");
                }
                sql.append("(?)");
                first = false;
                productsAdded++;
            }
        }

        // If no products to add, return without executing the query
        if (productsAdded == 0) {
            System.out.println("No products to add to the panier.");
            return;
        }

        // Prepare the statement and set parameter values
        try (PreparedStatement ps = cnx.prepareStatement(sql.toString())) {
            int parameterIndex = 1;
            for (Produit produit : cart) {
                if (produit != null) {
                    ps.setString(parameterIndex++, produit.getNom());
                }
            }

            // Execute the update
            ps.executeUpdate();
        }
        System.out.println("Panier contents saved to the database.");
    }



}
