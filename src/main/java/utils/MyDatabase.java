package utils;

import services.ServiceProduit;
import services.ServiceUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/3A60";
    private final String USER = "root";
    private final String PASS = "";
    private Connection connection;
    private static MyDatabase instance;

    private MyDatabase()  {
        try {
            connection = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("Connected");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if(instance == null)
            instance = new MyDatabase();
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    public static void main(String[] args) {
        MyDatabase db = new MyDatabase();
        ServiceUser su = new ServiceUser();
        ServiceProduit serviceProduit = new ServiceProduit();
        try {

            // serviceProduit.ajouter(new Produit(2 , 2 ,  "5", "vf" , "fd" , "fv" ));

            // serviceProduit.modifier(new Produit(2 , 3 , "5" , "vf" , "ffvd" , "fd"));

            System.out.println(su.recupererT());
            su.supprimerT(2);
        } catch (SQLException e) {
            System.err.println(e.getMessage());

        }

    }
}