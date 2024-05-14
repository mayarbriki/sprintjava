package services;

import models.Transport;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceTransport implements IService<Transport>{

    Connection cnx;


    public ServiceTransport() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouterT(Transport transport, int livreur_id) throws SQLException {
        String requete1 = "INSERT INTO transport (type, marque, matricule, etat, anneefab, livreur_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete1)) {
            // Set the parameters of the query using the attributes of the Transport object
            preparedStatement.setString(1, transport.getType());
            preparedStatement.setString(2, transport.getMarque());
            preparedStatement.setString(3, transport.getMatricule());
            preparedStatement.setString(4, transport.getEtat());
            preparedStatement.setDate(5, transport.getAnneefab());
            preparedStatement.setInt(6, livreur_id); // Set the user ID

            // Execute the query to insert the new record
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifierT(Transport transport, int livreur_id) throws SQLException {
        String requete2 = "UPDATE transport SET type=?, marque=?, matricule=?, etat=?, anneefab=? WHERE id=? AND livreur_id=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete2)) {
            preparedStatement.setString(1, transport.getType());
            preparedStatement.setString(2, transport.getMarque());
            preparedStatement.setString(3, transport.getMatricule());
            preparedStatement.setString(4, transport.getEtat());
            preparedStatement.setDate(5, transport.getAnneefab());
            preparedStatement.setInt(6, transport.getId());
            preparedStatement.setInt(7, livreur_id); // Set the user ID

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
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
    public List<Transport> recupererT(int livreur_id) throws SQLException {
        List<Transport> transports = new ArrayList<>();
        String sql = "SELECT * FROM transport WHERE livreur_id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, livreur_id);
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
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return transports;
    }
    @Override
    public List<Transport> recupererT() throws SQLException {
        List<Transport> transports = new ArrayList<>();
        String sql = "SELECT t.*, u.name AS livreur_name " +
                "FROM transport t " +
                "LEFT JOIN user u ON t.livreur_id = u.id";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Transport transport = new Transport();
                    transport.setId(resultSet.getInt("id"));
                    transport.setType(resultSet.getString("type"));
                    transport.setMarque(resultSet.getString("marque"));
                    transport.setMatricule(resultSet.getString("matricule"));
                    transport.setEtat(resultSet.getString("etat"));
                    transport.setAnneefab(resultSet.getDate("anneefab"));
                    transport.setLivreur(resultSet.getString("livreur_name"));

                    transports.add(transport);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return transports;
    }


    @Override
    public List<String> Matriculescombobox(int livreur_id) throws SQLException {
        List<String> matriculeList = new ArrayList<>();
        String query = "SELECT DISTINCT matricule FROM transport WHERE livreur_id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, livreur_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    matriculeList.add(resultSet.getString("matricule"));
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return matriculeList;
    }


    @Override
    public void ajouter(Transport transport) throws SQLException {

    }

    @Override
    public void modifier(Transport transport) throws SQLException {

    }

    @Override
    public void supprimer(int id) throws SQLException {

    }

    @Override
    public List<Transport> recuperer() throws SQLException {
        return null;
    }

    public Transport getTransportByMatricule(String matricule, int livreur_id) throws SQLException {
        Transport transport = null;
        String sql = "SELECT * FROM transport WHERE matricule = ? AND livreur_id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, matricule);
            preparedStatement.setInt(2, livreur_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve the transport details from the ResultSet
                    int id = resultSet.getInt("id");
                    String type = resultSet.getString("type");
                    String marque = resultSet.getString("marque");
                    String etat = resultSet.getString("etat");
                    Date anneefab = resultSet.getDate("anneefab");

                    // Create a new Transport object with the retrieved details
                    transport = new Transport(id, type, marque, matricule, etat, anneefab);
                }
            }
        }
        return transport;
    }


    public int countTransports(int livreur_id) throws SQLException {
        String query = "SELECT COUNT(*) FROM transport WHERE livreur_id = ?";
        int count = 0;

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, livreur_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }

        return count;
    }

    public Map<String, Long> countTransportsByEtat(int livreur_id) throws SQLException {
        String query = "SELECT etat, COUNT(*) AS count FROM transport WHERE livreur_id = ? GROUP BY etat";
        Map<String, Long> transportCountsByEtat = new HashMap<>();

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, livreur_id);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String etat = resultSet.getString("etat");
                    long count = resultSet.getLong("count");
                    transportCountsByEtat.put(etat, count);
                }
            }
        }

        return transportCountsByEtat;
    }

    public boolean transportExists(Transport transport) throws SQLException {
        String query = "SELECT COUNT(*) FROM transport WHERE type = ? AND marque = ? AND matricule = ? AND etat = ? AND anneefab = ?";
        int count = 0;

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, transport.getType());
            statement.setString(2, transport.getMarque());
            statement.setString(3, transport.getMatricule());
            statement.setString(4, transport.getEtat());
            statement.setDate(5, transport.getAnneefab());

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                }
            }
        }

        return count > 0;
    }


}
