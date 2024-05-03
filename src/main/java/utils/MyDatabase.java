package utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Produit;
import services.ServicePanier;
import services.ServicePersonne;
import models.Personne;
import services.ServiceProduit;
import models.Panier;
import java.io.IOException;

public class MyDatabase {

    private final String URL = "jdbc:mysql://localhost:3306/parapharmacie";
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
        ServicePanier servicepanier = new ServicePanier() ;
        try {
         //   Panier panier = new Panier();
            Produit produit = new Produit();

            // Add code to initialize Panier and Produit objects with appropriate values

            // Call the addToCart function
           // servicepanier.addToCart(panier, produit , conn);
   //   sp.ajouter(new Personne(1,25,"Ben" , "1234"));
            //serviceProduit.ajouter(new Produit(2 , 2 ,  "5", "vf" , "fd" , "fv" ));

            //serviceProduit.modifier(new Produit(2 , 3 , "5" , "vf" , "ffvd" , "fd"));

            System.out.println(sp.recuperer());
            sp.supprimer(2);
           // Panier panier = new Panier(5); // Create a Panier with a capacity of 5 produits
         //   Produit produit1 = new Produit(2 , 2 ,  "5", "vf" , "fd" , "fv" );
          //  Produit produit2 = new Produit(3 , 2 ,  "5", "vf" , "fd" , "fv" );

          //  panier.addProduit1(produit1); // Add produit1 to the cart
          //  panier.addProduit1(produit2); // Add produit2 to the cart
          //  Produit[] cart = {produit1 , produit2};
          //  servicepanier.savePanier(cart);
// You can continue adding more produits to the cart using panier.addProduit() method

        } catch (SQLException e) {
           // System.err.println(e.getMessage());
            System.err.println("Error connecting to the database: " + e.getMessage());

        }

    }
}
