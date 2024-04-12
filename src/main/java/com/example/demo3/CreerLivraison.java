package com.example.demo3;

import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.Livraison;
import models.Transport;
import services.ServiceLivraison;
import services.ServiceTransport;

public class CreerLivraison extends Application implements Initializable{

    @FXML
    private TextField adresseLiv;

    @FXML
    private DatePicker dateLiv;

    @FXML
    private ComboBox<String> description;

    @FXML
    private TextField etat;

    public void ajouterL(ActionEvent event) throws SQLException {
        LocalDate selectedDate = dateLiv.getValue();
        String adresseLivValue = adresseLiv.getText();
        String descriptionValue = description.getSelectionModel().getSelectedItem();
        String etatValue = etat.getText();

        if (selectedDate == null || adresseLivValue.isEmpty() || descriptionValue.isEmpty() || etatValue.isEmpty()) {
            showAlert(AlertType.ERROR, "Error Message", "Veuillez remplir tous les champs vides");
            return;
        }

        Livraison livraison = new Livraison(0, Date.valueOf(selectedDate), adresseLivValue, descriptionValue, etatValue);

        ServiceLivraison serviceLivraison = new ServiceLivraison();

        try {
            serviceLivraison.ajouterL(livraison);
            System.out.println("Livraison added successfully.");
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error Message", "Failed to add Livraison: " + e.getMessage());
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("creerLivraison.fxml")));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

