package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Personne;
import models.Produit;
import services.IService;
import services.ServicePersonne;
import utils.MyDatabase;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    Connection cnx;

    @FXML
    private Label welcomeText;
    @FXML
    private Button btnlogin;

    @FXML
    private AnchorPane pagelogin;

    @FXML
    private TextField pwd;
    @FXML
    private AnchorPane pane_login;

    @FXML
    private AnchorPane pane_signup;

    @FXML
    private TextField username;
    @FXML
    private Button login;
    @FXML
    private TextField pwdlogin;

    @FXML
    private TextField usenamelogin;


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }





    public void add(ActionEvent event) throws SQLException {
        Connection connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return;
        }

        Personne personne = new Personne(1, 25, username.getText() , pwd.getText());
        ServicePersonne sp = new ServicePersonne(); // Use connection object

        try {
            sp.ajouter(personne);
            System.out.println(sp.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void LoginpaneShow(){

        pane_login.setVisible(true);
        pane_signup.setVisible(false);
    }

    public void SignuppaneShow(){

        pane_login.setVisible(false);
        pane_signup.setVisible(true);
    }
    @FXML
    private void Login(ActionEvent event) throws Exception {
        Connection connection = MyDatabase.getInstance().getConnection(); // Get connection

        if (connection == null) {
            // Handle connection error
            System.out.println("Connection is null!");
            return;
        }

        String sql = "SELECT * FROM user WHERE nom = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) { // Use prepared statement with try-with-resources
            pst.setString(1, usenamelogin.getText());
            pst.setString(2, pwdlogin.getText());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role"); // Assuming the role column is named "role" in your database
                FXMLLoader loader = new FXMLLoader();

                if ("admin".equals(role)) {
                    loader.setLocation(getClass().getResource("dashboard.fxml"));
                } else if ("user".equals(role)) {
                    loader.setLocation(getClass().getResource("vueuser.fxml"));
                } else {
                    // Handle unrecognized roles
                    System.out.println("Unrecognized role: " + role);
                    return;
                }

                Parent root = loader.load();
                Stage stage = new Stage();
                List<Produit> produits = fetchProductsFromDatabase();

                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);

                for (int i = 0; i < produits.size(); i++) {
                    Produit produit = produits.get(i);
                    ImageView imageView = new ImageView();
                    Label nameLabel = new Label();
                    Label descriptionLabel = new Label();
                    Label priceLabel = new Label();

                    try {
                        // Load image from resource
                        imageView.setImage(new Image(produit.getImage()));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        continue; // Skip this product if image loading fails
                    }

                    nameLabel.setText(produit.getNom());
                    descriptionLabel.setText(produit.getDescription());
                    priceLabel.setText("Price: $" + produit.getPrix());

                    gridPane.add(imageView, 0, i);
                    gridPane.add(nameLabel, 1, i);
                    gridPane.add(descriptionLabel, 2, i);
                    gridPane.add(priceLabel, 3, i);
                }

                Scene scene = new Scene(gridPane, 800, 600);
                stage.setScene(scene);
                stage.show();

                // Close current login window
                // ((Node)(event.getSource())).getScene().getWindow().hide();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Username or Password");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            // Handle SQL exception
            System.err.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("An error occurred while connecting to the database.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
    private List<Produit> fetchProductsFromDatabase() {
        List<Produit> produits = new ArrayList<>();

        try (Connection connection = MyDatabase.getInstance().getConnection()) {
            String sql = "SELECT * FROM produit";
            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Produit produit = new Produit(
                            rs.getInt("id"),
                            rs.getInt("quantite"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            "file:" + rs.getString("image"), // Prefix "file:" to create a file URL
                            rs.getString("prix")
                    );
                    produits.add(produit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database exception
        }

        return produits;
    }


}