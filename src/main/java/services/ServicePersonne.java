package services;

import models.Personne;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePersonne implements IService<Personne>{

    Connection cnx;

    public ServicePersonne() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Personne personne) throws SQLException {
        String sql = "INSERT INTO personne (nom, prenom, age) values('"+ personne.getNom() +"'," +
                "'"+ personne.getPrenom() +"',"+ personne.getAge() +")";
        Statement st = cnx.createStatement();
        st.executeUpdate(sql);
    }

    @Override
    public void modifier(Personne personne) throws SQLException {
        String sql = "UPDATE personne set nom = ? , prenom = ?, age = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1,personne.getNom());
        ps.setString(2,personne.getPrenom());
        ps.setInt(3,personne.getAge());
        ps.setInt(4,personne.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM personne where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    @Override
    public List<Personne> recuperer() throws SQLException {
        List<Personne> personnes = new ArrayList<>();
        String sql = "SELECT * FROM personne";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            Personne personne = new Personne();
            personne.setId(rs.getInt("id"));
            personne.setAge(rs.getInt("age"));
            personne.setNom(rs.getString("nom"));
            personne.setPrenom(rs.getString("prenom"));

            personnes.add(personne);

        }
        return personnes;
    }
}
