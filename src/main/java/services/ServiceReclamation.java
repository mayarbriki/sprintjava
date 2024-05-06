package services;

import models.reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceReclamation implements IService<reclamation>{

    Connection cnx;

    public ServiceReclamation() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(reclamation reclamation) throws SQLException {
        String sql = "INSERT INTO reclamation (titre, description_r, status_r, date_r) values('"+ reclamation.getTitre() +"'," +
                "'"+ reclamation.getDescription_r() +"',"+ reclamation.getStatus_r() +"',"+ reclamation.getDate_r() +"')";
        Statement st = cnx.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void modifierT(reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation set status_r= ?  where id = ?";
       try ( PreparedStatement ps = cnx.prepareStatement(sql)){

        ps.setString(1,reclamation.getStatus_r());

        ps.executeUpdate();
    }
       catch (SQLException ex) {
           System.out.println(ex.getMessage());
       }

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM reclamation where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    @Override
    public List<reclamation> recuperer() throws SQLException {
        List<reclamation> reclamations = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";
        try (Statement st = cnx.createStatement()) {
            try (ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    reclamation reclamation = new reclamation();
                    reclamation.setId(rs.getInt("id"));
                    reclamation.setStatus_r(rs.getString("Status"));
                    reclamation.setTitre(rs.getString("Titre"));
                    reclamation.setDescription_r(rs.getString("Description"));
                    reclamation.setDate_r(rs.getDate("Date"));

                    reclamations.add(reclamation);

                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());

        }
        return reclamations;
    }
    public int countreclamation() throws SQLException {
        String query = "SELECT COUNT(*) FROM reclamation";
        int count = 0;

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }

        return count;

    }
    public Map<String, Integer> countreclamationByDate() throws SQLException {
        Map<String, Integer> reclamationByDate = new HashMap<>();
        String query = "SELECT date_r, COUNT(*) AS count FROM reclamation GROUP BY date_r";

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Date date = resultSet.getDate("date_r");
                int count = resultSet.getInt("count");
                reclamationByDate.put(date.toString(), count);
            }
        }
        return reclamationByDate;

    }
    public Map<String, Integer> countReclamationByStatus() throws SQLException {
        Map<String, Integer> reclamationByStatus = new HashMap<>();
        String query = "SELECT status_r, COUNT(*) AS count FROM reclamation GROUP BY status_r";

        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String status = resultSet.getString("status_r");
                int count = resultSet.getInt("count");
                reclamationByStatus.put(status, count);
            }
        }
        return reclamationByStatus;
    }
}

