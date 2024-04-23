package services;

import models.reclamation;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public void modifier(reclamation reclamation) throws SQLException {
        String sql = "UPDATE reclamation set titre = ? , description_r = ?, status_r= ? , Date_r= ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1,reclamation.getTitre());
        ps.setString(2,reclamation.getDescription_r());
        ps.setString(3,reclamation.getStatus_r());
        ps.setInt(4,reclamation.getId());
        ps.setDate(3,reclamation.getDate_r());
        ps.executeUpdate();
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
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            reclamation reclamation = new reclamation();
            reclamation.setId(rs.getInt("id"));
            reclamation.setStatus_r(rs.getString("Status"));
            reclamation.setTitre(rs.getString("Titre"));
            reclamation.setDescription_r(rs.getString("Description"));
            reclamation.setDate_r(rs.getDate("Date"));

            reclamations.add(reclamation);

        }
        return reclamations;
    }
}
