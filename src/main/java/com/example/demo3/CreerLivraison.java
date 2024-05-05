package com.example.demo3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Livraison;
import services.ServiceLivraison;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreerLivraison extends Application implements Initializable {

    @FXML
    private TextField adresseLiv;

    @FXML
    private DatePicker dateLiv;

    @FXML
    private ComboBox<String> description;

    @FXML
    private ComboBox<String> livreur;

    @FXML
    private TextField etat;
    private int commandeId;

    @FXML
    private Label commande;



    @FXML
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

        int livreurId = getSelectedLivreurId();

        // Create an instance of ServiceLivraison
        ServiceLivraison serviceLivraison = new ServiceLivraison();

        try {
            // Call the ajouterL method of ServiceLivraison with the Livraison object, commande_id, and livreur_id
            serviceLivraison.ajouterL(livraison, commandeId, livreurId);
            System.out.println("Livraison ajoutée avec succès.");
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error Message", "Échec de l'ajout de la livraison: " + e.getMessage());
        }
    }

    private int getSelectedLivreurId() {
        // Get the selected item from the livreur ComboBox
        String selectedLivreur = livreur.getValue();

        // Initialize a variable to store the livreur_id
        int livreurId = -1; // Default value if no livreur is selected

        try {
            // Call the existing method from your ServiceLivraison class to retrieve the livreur_id by name
            livreurId = new ServiceLivraison().getLivreurIdByName(selectedLivreur);
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return livreurId;
    }



    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void populateLivreurComboBox() {
        try {
            List<String> livreurs = new ServiceLivraison().getAllLivreur();
            livreur.setItems(FXCollections.observableArrayList(livreurs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
        commande.setText(String.valueOf(commandeId));
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("creerLivraison.fxml")));
        stage.getIcons().add(new Image("com/example/demo3/images/MediCare (1).png"));
        stage.setTitle("ParaPharma+");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateLivreurComboBox();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

