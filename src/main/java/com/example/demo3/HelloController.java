package com.example.demo3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.User;
import services.ServiceUser;
import utils.MyDatabase;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

        User user = new User( 25, username.getText() , pwd.getText());
        ServiceUser sp = new ServiceUser(); // Use connection object

        try {
            sp.ajouterT(user);
            System.out.println(sp.recupererT());
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

        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
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





