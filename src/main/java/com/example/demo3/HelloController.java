package com.example.demo3;
import services.UserSessionService;
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
   private int loggedInUserId;
    @FXML
    private Button signin;

    @FXML
    private Button signup;
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
        pane_login.setVisible(true);
        pane_signup.setVisible(false);

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
    @FXML
    public void LoginpaneShow(){

        pane_login.setVisible(true);
        pane_signup.setVisible(false);
    }

    @FXML
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

        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) { // Use prepared statement with try-with-resources
            pst.setString(1, usenamelogin.getText());
            pst.setString(2, pwdlogin.getText());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("roles");
                // Assuming the role column is named "role" in your database
                loggedInUserId = rs.getInt("id");
                UserSessionService.getInstance().setLoggedInUserId(loggedInUserId); // Set logged-in user ID
                System.out.println("Logged-in User ID: " + loggedInUserId);
                FXMLLoader loader = new FXMLLoader();

                if ("[\"ROLE_ADMIN\"]".equals(role)) {
                    loader.setLocation(getClass().getResource("dashboard2.fxml"));
                } else if ("[\"ROLE_USER\"]".equals(role)) {
                    loader.setLocation(getClass().getResource("vueuser.fxml"));
                } else if ("[\"ROLE_LIVREUR\"]".equals(role)) {
                    loader.setLocation(getClass().getResource("Dashboard1.fxml"));
                }else {
                    // Handle unrecognized roles
                    System.out.println("Unrecognized role: " + role);
                    return;
                }

                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
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



}