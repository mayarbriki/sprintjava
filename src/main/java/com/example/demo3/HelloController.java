package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Personne;
import services.IService;
import services.ServicePersonne;
import utils.MyDatabase;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
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
            // Handle the case where the connection is null
            System.out.println("Connection is null!");
            return;
        }

        Personne personne = new Personne(1, 25, username.getText(), pwd.getText());
        ServicePersonne sp = new ServicePersonne(); // Use connection object

        try {
            sp.ajouter(personne);
            System.out.println(sp.recuperer());
            // sp.supprimer(2); // Uncomment if you want to implement delete functionality
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    @FXML
    private void Login(ActionEvent event) throws Exception {
        Connection connection = MyDatabase.getInstance().getConnection(); // Get connection

        if (connection == null) {
            // Handle connection error
            System.out.println("Connection is null!");
            return;
        }

        String sql = "SELECT * FROM personne WHERE nom = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) { // Use prepared statement with try-with-resources
            pst.setString(1, usenamelogin.getText());
            pst.setString(2, pwdlogin.getText());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText("Welcome!");
                alert.setContentText("You have logged in successfully.");
                alert.showAndWait();

                // Switch scenes or perform other actions on successful login
                login.getScene().getWindow().hide(); // Hide current scene
                Stage mainStage = new Stage();

                mainStage.show(); // Show CPanel scene
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

    }





