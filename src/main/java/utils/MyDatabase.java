package utils;

import services.ServiceReclamation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/parapharmacy";
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
        ServiceReclamation su = new ServiceReclamation();

        try {

            // serviceReclamation.ajouter(new Reclamation(2 , "rania" , "ktata" ));

            // serviceReclamation.modifier(new Reclamation(2 , "3ktata" , "5" ));

            System.out.println(su.recuperer());
            su.supprimer(2);
        } catch (SQLException e) {
            System.err.println(e.getMessage());

        }

    }
}
