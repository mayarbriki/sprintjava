package services;

import models.Transport;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceTransport implements IService<Transport>{

    Connection cnx;


    public ServiceTransport() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouterT(Transport transport) throws SQLException {

        String requete1 = "INSERT INTO transport (type, marque, matricule, etat, anneefab) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete1)) {
            // Set the parameters of the query using the attributes of the Transport object
            preparedStatement.setString(1, transport.getType());
            preparedStatement.setString(2, transport.getMarque());
            preparedStatement.setString(3, transport.getMatricule());
            preparedStatement.setString(4, transport.getEtat());
            preparedStatement.setDate(5, transport.getAnneefab());

            // Execute the query to insert the new record
            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void modifierT(Transport transport) throws SQLException {

        String requete2 = "UPDATE transport SET type=?, marque=?, matricule=?, etat=?, anneefab=? WHERE id=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete2)) {

            preparedStatement.setString(1, transport.getType());
            preparedStatement.setString(2, transport.getMarque());
            preparedStatement.setString(3, transport.getMatricule());
            preparedStatement.setString(4, transport.getEtat());
            preparedStatement.setDate(5, transport.getAnneefab());
            preparedStatement.setInt(6, transport.getId());

            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void supprimerT(int id) throws SQLException {

        String requete3 = "DELETE FROM transport WHERE id=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete3)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    @Override
    public  List<Transport> recupererT() throws SQLException {
        List<Transport> transports = new ArrayList<>();
        String sql = "SELECT * FROM transport";

        try ( PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transport transport = new Transport();
                    transport.setId(resultSet.getInt("id"));
                    transport.setType(resultSet.getString("type"));
                    transport.setMarque(resultSet.getString("marque"));
                    transport.setMatricule(resultSet.getString("matricule"));
                    transport.setEtat(resultSet.getString("etat"));
                    transport.setAnneefab(resultSet.getDate("anneefab"));

                    // Add the created Transport object to the list
                    transports.add(transport);
                }
            }
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }

        return transports;
    }

}
