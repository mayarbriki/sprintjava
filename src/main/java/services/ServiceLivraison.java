package services;

import models.Livraison;
import models.Transport;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceLivraison implements LService<Livraison> {

    Connection cnx;

    public ServiceLivraison() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouterL(Livraison livraison) throws SQLException {
        String requete1 = "INSERT INTO livraison (date_liv, adresseLiv, description, etat) VALUES (?, ?, ?, ?)";

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
        String requete2 = "UPDATE livraison SET date_liv=?, adresse_Liv=?, description=?, etat=?, livreur_id=? WHERE id=?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(requete2)) {

            String livreurName = livraison.getLivreur();
            int livreurId = getLivreurIdByName(livreurName);

            preparedStatement.setDate(1, livraison.getDateLiv());
            preparedStatement.setString(2, livraison.getAdresseLiv());
            preparedStatement.setString(3, livraison.getDescription());
            preparedStatement.setString(4, livraison.getEtat());
            preparedStatement.setInt(5, livreurId);
            preparedStatement.setInt(6, livraison.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
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
        String sql = "SELECT l.id, l.adresse_liv, l.description, l.etat, l.date_liv, t.matricule AS transport_matricule, l.reference_id, u.name AS livreur " +
                "FROM livraison l " +
                "LEFT JOIN transport t ON l.matricule_id = t.id " +
                "LEFT JOIN user u ON l.livreur_id = u.id";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Livraison livraison = new Livraison();
                    livraison.setId(resultSet.getInt("id"));
                    livraison.setAdresseLiv(resultSet.getString("adresse_liv"));
                    livraison.setDescription(resultSet.getString("description"));
                    livraison.setEtat(resultSet.getString("etat"));
                    livraison.setDateLiv(resultSet.getDate("date_liv"));
                    String matricule = resultSet.getString("transport_matricule");
                    if (matricule != null) {
                        livraison.setMatricule(matricule);
                    } else {
                        livraison.setMatricule("N/A");
                    }
                    int referenceId = resultSet.getInt("reference_id");
                    livraison.setReference_id(referenceId);
                    String livreurName = resultSet.getString("livreur");
                    livraison.setLivreur(livreurName);
                    livraisons.add(livraison);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return livraisons;
    }

    @Override
    public List<Livraison> recupererL(int livreur_id) throws SQLException {
        List<Livraison> livraisons = new ArrayList<>();
        String sql = "SELECT l.id, l.adresse_liv, l.description, l.etat, l.date_liv, " +
                "t.matricule AS transport_matricule, l.reference_id, " +
                "u.name AS livreur_name " +
                "FROM livraison l " +
                "LEFT JOIN transport t ON l.matricule_id = t.id " +
                "LEFT JOIN user u ON l.livreur_id = u.id " +
                "WHERE l.livreur_id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, livreur_id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Livraison livraison = new Livraison();
                    livraison.setId(resultSet.getInt("id"));
                    livraison.setAdresseLiv(resultSet.getString("adresse_liv"));
                    livraison.setDescription(resultSet.getString("description"));
                    livraison.setEtat(resultSet.getString("etat"));
                    livraison.setDateLiv(resultSet.getDate("date_liv"));
                    String matricule = resultSet.getString("transport_matricule");
                    if (matricule != null) {
                        livraison.setMatricule(matricule);
                    } else {
                        livraison.setMatricule("N/A");
                    }
                    int referenceId = resultSet.getInt("reference_id");
                    livraison.setReference_id(referenceId);
                    livraison.setLivreur(resultSet.getString("livreur_name"));
                    livraisons.add(livraison);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            // Handle the exception
        }
        return livraisons;
    }




    //    public List<Livraison> retrieveLivraisonsWithMatricule() throws SQLException {
//        List<Livraison> livraisons = new ArrayList<>();
//        String sql = "SELECT l.*, t.matricule_id AS transport_matricule FROM livraison l JOIN transport t ON l.matricule = t.matricule";
//
//        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                while (resultSet.next()) {
//                    Livraison livraison = new Livraison();
//                    livraison.setId(resultSet.getInt("id"));
//                    livraison.setAdresseLiv(resultSet.getString("adresseLiv"));
//                    livraison.setDescription(resultSet.getString("description"));
//                    livraison.setEtat(resultSet.getString("etat"));
//                    livraison.setDateLiv(resultSet.getDate("date_liv"));
//                    livraison.setMatricule(String.valueOf(new Transport(resultSet.getString("transport_matricule")).getId()));
//
//                    livraisons.add(livraison);
//                }
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
//
//        return livraisons;
//    }

    private int getLivreurIdByName(String livreur) throws SQLException {
        int livreurId = -1;

        String query = "SELECT id FROM user WHERE name=?";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, livreur);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    livreurId = resultSet.getInt("id");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        return livreurId;
    }

    public List<String> getAllLivreur() throws SQLException {
        List<String> livreurs = new ArrayList<>();
        String query = "SELECT name FROM user WHERE roles LIKE '%ROLE_LIVREUR%'";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String livreurName = resultSet.getString("name");
                livreurs.add(livreurName);
            }
        }
        return livreurs;
    }

    public int countLivraisons(int livreur_id) throws SQLException {
        String query = "SELECT COUNT(*) FROM livraison WHERE livreur_id = ?";
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

    public int livraisons_Aaffecter(int livreur_id) throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) AS count FROM livraison WHERE (matricule_id IS NULL OR matricule_id = '') AND livreur_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, livreur_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }
            }
        }
        return count;
    }


    public Map<String, Integer> countLivraisonsByDate(int livreur_id) throws SQLException {
        Map<String, Integer> livraisonsByDate = new HashMap<>();
        String query = "SELECT date_liv, COUNT(*) AS count FROM livraison WHERE livreur_id = ? GROUP BY date_liv";

        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, livreur_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Date date = resultSet.getDate("date_liv");
                    int count = resultSet.getInt("count");
                    livraisonsByDate.put(date.toString(), count);
                }
            }
        }

        return livraisonsByDate;
    }

}
