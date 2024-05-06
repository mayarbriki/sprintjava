package services;

import models.reponse;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReponse implements RService<reponse>{

    Connection cnx;

    public ServiceReponse() {
        cnx = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouterR(reponse reponse) throws SQLException {

    }

    @Override
    public void modifierTR(reponse reponse) throws SQLException {

    }

    @Override
    public void supprimerR(int id) throws SQLException {

    }

    @Override
    public List<reponse> recupererR() throws SQLException {
        return null;
    }
    public int nonreponse() throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) AS count FROM reclamation WHERE id NOT IN (SELECT idrec_id FROM reponse)";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt("count");
            }
        }
        return count;
    }
    public int countreponse() throws SQLException {
        String query = "SELECT COUNT(*) FROM reponse";
        int count = 0;

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }

        return count;
    }
}

