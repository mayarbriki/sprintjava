package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Produit;
import services.ServicePersonne;
import models.Personne;
import services.ServiceProduit;

import java.io.IOException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/parapharmacy1";
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
        ServicePersonne sp = new ServicePersonne();
        ServiceProduit serviceProduit = new ServiceProduit();
        try {
   //   sp.ajouter(new Personne(1,25,"Ben" , "1234"));
            //serviceProduit.ajouter(new Produit(2 , 2 ,  "5", "vf" , "fd" , "fv" ));

            //serviceProduit.modifier(new Produit(2 , 3 , "5" , "vf" , "ffvd" , "fd"));

            System.out.println(sp.recuperer());
            sp.supprimer(2);
        } catch (SQLException e) {
            System.err.println(e.getMessage());

        }

    }
}
