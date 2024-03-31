package com.example.demo3;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Produit;
import services.ServiceProduit;
import utils.MyDatabase;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class produitcontroller extends Application {
    Connection cnx;

    public String path;
    @FXML
    private Button addproduit;
    @FXML
    private Label imagePathLabel;
    @FXML
    private TextField nomproduit;
    @FXML
    private BorderPane layout;

    @FXML
    private Button button;
    @FXML
    private ImageView imgview;
    @FXML
    private TextArea description;
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
    public void addproduit(ActionEvent event) throws SQLException {
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






    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(produitcontroller.class.getResource("ajouterproduit.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 650);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
