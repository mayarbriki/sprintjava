package services;

import models.Personne;
import models.Produit;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceProduit implements  PService<Produit> {

    Connection cnx;

    public ServiceProduit() {
        cnx = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Produit produit) throws SQLException {
        String sql = "INSERT INTO Produit (id, quantite, nom,  description, image, prix) VALUES (?, ?, ?, ? , ? ,? )";

        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, produit.getId());

        ps.setInt(2, produit.getQuantite());
        ps.setString(3, produit.getNom());
        ps.setString(4, produit.getDescription());
        ps.setString(5, produit.getImage());
        ps.setString(6, produit.getPrix());

        ps.executeUpdate();
    }

    @Override
    public void modifier(Produit produit ) throws SQLException {
        String sql = "UPDATE produit set quantite = ? , nom = ?, description = ? ,image= ? , prix = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);

        ps.setInt(1, produit.getQuantite());
        ps.setString(2, produit.getNom());
        ps.setString(3, produit.getDescription());
        ps.setString(4, produit.getImage());
        ps.setString(5, produit.getPrix());
        ps.setInt(6, produit.getId());
        ps.executeUpdate();
    }



    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public List<Produit> recuperer() throws SQLException {
        return null;
    }


}
