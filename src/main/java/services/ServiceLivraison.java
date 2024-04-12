package services;

import models.Livraison;
import models.Transport;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceLivraison implements LService<Livraison> {

    Connection cnx;

    public ServiceLivraison() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouterL(Livraison livraison) throws SQLException {
        String requete1 = "INSERT INTO livraison (dateLiv, adresseLiv, description, etat) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete1)) {

            preparedStatement.setDate(1, livraison.getDateLiv());
            preparedStatement.setString(2, livraison.getAdresseLiv());
            preparedStatement.setString(3, livraison.getDescription());
            preparedStatement.setString(4, livraison.getEtat());

            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Override
    public void modifier_L(Livraison livraison) throws SQLException {
        String requete2 = "UPDATE livraison SET dateLiv=?, adresseLiv=?, description=?, etat=? WHERE id=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete2)) {

            preparedStatement.setDate(1, livraison.getDateLiv());
            preparedStatement.setString(2, livraison.getAdresseLiv());
            preparedStatement.setString(3, livraison.getDescription());
            preparedStatement.setString(4, livraison.getEtat());
            preparedStatement.setInt(5, livraison.getId());

            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifierL(Livraison livraison) throws SQLException {
        String sql = "UPDATE livraison SET matricule_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, livraison.getMatricule());
            preparedStatement.setInt(2, livraison.getId());
            preparedStatement.executeUpdate();
        }
    }


    @Override
    public void supprimerL(int id) throws SQLException {

        String requete3 = "DELETE FROM livraison WHERE id=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete3)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public List<Livraison> recupererL() throws SQLException {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT l.id, l.adresseLiv, l.description, l.etat, l.dateLiv, t.matricule AS transport_matricule " +
                "FROM livraison l LEFT JOIN transport t ON l.matricule_id = t.id";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Create a new Livraison object with retrieved data
                    Livraison livraison = new Livraison();
                    livraison.setId(resultSet.getInt("id"));
                    livraison.setAdresseLiv(resultSet.getString("adresseLiv"));
                    livraison.setDescription(resultSet.getString("description"));
                    livraison.setEtat(resultSet.getString("etat"));
                    livraison.setDateLiv(resultSet.getDate("dateLiv"));
                    String matricule = resultSet.getString("transport_matricule");
                    if (matricule != null) {
                        livraison.setMatricule(matricule);
                    } else {
                        livraison.setMatricule("N/A");
                    }
                    livraisons.add(livraison);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return livraisons;
    }


    public List<Livraison> retrieveLivraisonsWithMatricule() throws SQLException {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT l.*, t.matricule_id AS transport_matricule FROM livraison l JOIN transport t ON l.matricule = t.matricule";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Livraison livraison = new Livraison();
                    livraison.setId(resultSet.getInt("id"));
                    livraison.setAdresseLiv(resultSet.getString("adresseLiv"));
                    livraison.setDescription(resultSet.getString("description"));
                    livraison.setEtat(resultSet.getString("etat"));
                    livraison.setDateLiv(resultSet.getDate("dateLiv"));
                    livraison.setMatricule(String.valueOf(new Transport(resultSet.getString("transport_matricule")).getId()));

                    livraisons.add(livraison);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return livraisons;
    }

}
