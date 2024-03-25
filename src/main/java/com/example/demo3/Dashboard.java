package com.example.demo3;
import javafx.scene.control.TextArea;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import services.ServiceProduit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import models.Personne;
import models.Produit;
import services.ServicePersonne;
import utils.MyDatabase;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;


public class Dashboard extends Application implements Initializable {
    public String path;
    @FXML
    private Label imagePathLabel;

    @FXML
    private TextArea description;
    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private TextField nomproduit;
    @FXML
    private AnchorPane anchorPane2;
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
    private Button addproduit;
    @FXML
    private Button livraison;

    @FXML
    private Button produit;
    @FXML
    private BorderPane layout;
    @FXML
    private Button user;
    @FXML
    private ImageView imgview;

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
    /*@FXML
    private void handleProduitButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterproduit.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    @FXML
    private void handleProduitButtonClick(ActionEvent event) {
        // This method is already the event handler for the button click,
        // so you don't need to define another event handler inside it.
        // Simply put your logic directly here.

        anchorPane1.setVisible(false);
        anchorPane2.setVisible(true);
    }
    @FXML
    private void handleProduitButtonClick1(ActionEvent event) {
        // This method is already the event handler for the button click,
        // so you don't need to define another event handler inside it.
        // Simply put your logic directly here.

        anchorPane1.setVisible(true);
        anchorPane2.setVisible(false);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList("admin", "livreur");
        comb.setItems(list);
        anchorPane1.setVisible(true);

        // Set anchorPane2 invisible initially
        anchorPane2.setVisible(false);
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
    @FXML
    Label label = new Label();
    public void insertImage() {

        // String chosenImagePath;
        FileChooser open = new FileChooser();

        Stage stage = (Stage) layout.getScene().getWindow();

        File file = open.showOpenDialog(stage);

        if (file != null) {

            path = file.getAbsolutePath();

            path = path.replace("\\", "\\\\");
            imagePathLabel.setText(path);

            label.setText(path);

            Image image = new Image(file.toURI().toString(), 110, 110, false, true);

            imgview.setImage(image);

        } else {

            System.out.println("NO DATA EXIST!");

        }
    }
    public void addproduit1(ActionEvent event) throws SQLException {
        Connection connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return;
        }
        //  String imagePath = insertImage();  // Call insertImage to get the path (or null)
        String imagePath = path;
        if (imagePath == null) {
            System.out.println("No image selected!");
            return;
        }
        Produit produit = new Produit(25, nomproduit.getText(), description.getText(), imagePath);
        ServiceProduit sp = new ServiceProduit(); // Use connection object

        try {
            sp.ajouter(produit);
            System.out.println(sp.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }





}
