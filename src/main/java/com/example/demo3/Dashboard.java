package com.example.demo3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import models.Personne;
import services.ServicePersonne;
import utils.MyDatabase;


public class Dashboard extends Application implements Initializable {
    @FXML
    private Button add;
    @FXML
    private TextField pwd;
    @FXML
    private TextField age;
    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;
    @FXML
    private Button commande;
    @FXML
    private ComboBox<String> comb;

    @FXML
    private Button livraison;

    @FXML
    private Button produit;

    @FXML
    private Button user;
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 650);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //void Select(ActionEvent event) {
      //  String role = comb.getSelectionModel().getSelectedItem().toString();
    //}
    @FXML
    private void handleProduitButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterproduit.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList("admin", "livreur");
        comb.setItems(list);
    }
    public void add(ActionEvent event) throws SQLException {
        Connection connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return;
        }

        // Retrieve selected role from ComboBox
        String role = comb.getSelectionModel().getSelectedItem().toString(); // Retrieve selected item from ComboBox
        String ageString = age.getText();
        int ageInt = Integer.parseInt(ageString);

        // Now you can create the Personne object with retrieved values
        Personne personne = new Personne(ageInt, nom.getText(), prenom.getText(), pwd.getText(), role);
        ServicePersonne sp = new ServicePersonne(); // Use connection object

        try {
            sp.ajouteradmin(personne);
            System.out.println(sp.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }




}
