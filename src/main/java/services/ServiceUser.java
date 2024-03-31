package services;

import models.User;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser implements IService<User>{

    Connection cnx;

    public ServiceUser() {
        cnx = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouterT(User user) throws SQLException {
        String sql = "INSERT INTO user (nom, prenom, age, password) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, user.getNom());
        ps.setString(2, user.getPrenom());
        ps.setInt(3, user.getAge());
        ps.setString(4, user.getPassword());
        ps.executeUpdate();

    }

    public void ajouteradminT(User user) throws SQLException {
        String sql = "INSERT INTO user (nom, prenom, age, password , role ) VALUES (?, ?, ?, ? , ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, user.getNom());
        ps.setString(2, user.getPrenom());
        ps.setInt(3, user.getAge());
        ps.setString(4, user.getPassword());
        ps.setString(5, user.getRole());

        ps.executeUpdate();

    }

    @Override
    public void modifierT(User user) throws SQLException {
        String sql = "UPDATE user set nom = ? , prenom = ?, age = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1,user.getNom());
        ps.setString(2,user.getPrenom());
        ps.setInt(3,user.getAge());
        ps.setInt(4,user.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimerT(int id) throws SQLException {
        String sql = "DELETE FROM user where id = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    @Override
    public List<User> recupererT() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()){
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setAge(rs.getInt("age"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));

            users.add(user);

        }
        return users;
    }
}
