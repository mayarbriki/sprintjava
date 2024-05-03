package com.example.demo3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Livraison;
import services.ServiceLivraison;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardAdminL extends Application implements Initializable {

    @FXML
    private Button acceuil_btn;

    @FXML
    private TextField adresseLiv;

    @FXML
    private Button close;

    @FXML
    private DatePicker dateLiv;

    @FXML
    private ComboBox<String> description;

    @FXML
    private TextField etat;

    @FXML
    private Button gestionCommande_btn;

    @FXML
    private Button gestionProduit_btn;

    @FXML
    private Button gestionlivraison_btn;

    @FXML
    private Button gestiontransport_btn;

    @FXML
    private TableColumn<Livraison, String> livraison_col_AdresseLiv;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Commande;

    @FXML
    private TableColumn<Livraison, String> livraison_col_DateLiv;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Description;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Etat;

    @FXML
    private TableColumn<Livraison, String> livraison_col_ID;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Matricule;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Nom;

    @FXML
    private TextField livraison_search;

    @FXML
    private TableView<Livraison> livraison_tableview;

    @FXML
    private Button logout;

    @FXML
    private Label name;
    private FilteredList<Livraison> filteredLivraisonData;

    public void modifier_L() {
        Livraison selectedLivraison = livraison_tableview.getSelectionModel().getSelectedItem();
        if (selectedLivraison != null) {
            LocalDate selectedDate = dateLiv.getValue();
            String adresseLivValue = adresseLiv.getText();
            String descriptionValue = description.getSelectionModel().getSelectedItem();
            String etatValue = etat.getText();

            if (selectedDate == null || adresseLivValue.isEmpty() || descriptionValue.isEmpty() || etatValue.isEmpty()) {
                showAlert(AlertType.ERROR, "Error Message", "Veuillez remplir tous les champs vides");
                return;
            }

            selectedLivraison.setDateLiv(Date.valueOf(selectedDate));
            selectedLivraison.setAdresseLiv(adresseLivValue);
            selectedLivraison.setDescription(descriptionValue);
            selectedLivraison.setEtat(etatValue);

            try {
                ServiceLivraison st = new ServiceLivraison();
                st.modifier_L(selectedLivraison);
                refreshLivraisonTable();
                System.out.println("Livraison mise à jour avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Échec de la mise à jour de la livraison: " + e.getMessage());
            }

        }
    }

    public void supprimerL(ActionEvent event) {

        Livraison selectedLivraison = livraison_tableview.getSelectionModel().getSelectedItem();
        if (selectedLivraison != null) {
            try {
                ServiceLivraison st = new ServiceLivraison();
                st.supprimerL(selectedLivraison.getId());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Delete Success");
                alert.setHeaderText(null);
                alert.setContentText("Livraison supprimée avec succès.");
                alert.showAndWait();
                refreshLivraisonTable();
            } catch (SQLException ex) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Delete Error");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de la suppression de la livraison: " + ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Pas de choix");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une livraison à supprimer.");
            alert.showAndWait();
        }
    }

    private void setupLivraisonTableColumns() {
        livraison_col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        livraison_col_DateLiv.setCellValueFactory(new PropertyValueFactory<>("dateLiv"));
        livraison_col_AdresseLiv.setCellValueFactory(new PropertyValueFactory<>("adresseLiv"));
        livraison_col_Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        livraison_col_Etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
//        livraison_col_Commande.setCellValueFactory(new PropertyValueFactory<>("commande"));
        livraison_col_Matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
    }

    @FXML
    private void loadDataIntoLivraisonTableView() {
        ServiceLivraison serviceLivraison = new ServiceLivraison();
        ObservableList<Livraison> livraisons = null;
        try {
            livraisons = FXCollections.observableArrayList(serviceLivraison.recupererL());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        livraison_tableview.setItems(livraisons);
    }

    @FXML
    private void refreshLivraisonTable() {
        try {
            List<Livraison> livraisonList = new ServiceLivraison().recupererL();
            ObservableList<Livraison> observableList = FXCollections.observableArrayList(livraisonList);

            livraison_tableview.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void resetLivraisonFields() {
        dateLiv.setValue(null);
        adresseLiv.setText("");
        description.getSelectionModel().clearSelection();
        setComboBoxPromptText(description, "Choisir");
        etat.setText("");
//        commande.setText("");
//        matricule.getSelectionModel().clearSelection();
    }

    private void setComboBoxPromptText(ComboBox<String> comboBox, String promptText) {
        comboBox.setPromptText(promptText);
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(promptText);
                } else {
                    setText(item);
                }
            }
        });
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setupLivraisonSearch() {
        filteredLivraisonData = new FilteredList<>(livraison_tableview.getItems(), p -> true);

        livraison_tableview.setItems(filteredLivraisonData);

        livraison_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredLivraisonData.setPredicate(livraison -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return livraison.getAdresseLiv().toLowerCase().contains(lowerCaseFilter)
                        || livraison.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || livraison.getMatricule().toLowerCase().contains(lowerCaseFilter)
                        || livraison.getEtat().toLowerCase().contains(lowerCaseFilter)
                        || livraison.getDateLiv().toString().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(livraison.getId()).toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    @FXML
    public void switchToDashboardAdminT(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardAdminT.fxml"));
            Parent root = loader.load();
            DashboardAdminT controller = loader.getController();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashboardAdminL.fxml")));
        stage.getIcons().add(new Image("com/example/demo3/images/MediCare (1).png"));
        stage.setTitle("ParaPharma+");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupLivraisonTableColumns();
        refreshLivraisonTable();
        loadDataIntoLivraisonTableView();
        setupLivraisonSearch();

        livraison_tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Livraison selectedLivraison = livraison_tableview.getSelectionModel().getSelectedItem();
                dateLiv.setValue(selectedLivraison.getDateLiv().toLocalDate());
                adresseLiv.setText(selectedLivraison.getAdresseLiv());
                description.setValue(selectedLivraison.getDescription());
                etat.setText(selectedLivraison.getEtat());
//                commande.setText(selectedLivraison.getCommande());
//                matricule.getSelectionModel().select(selectedLivraison.getMatricule());
            } else {
                resetLivraisonFields();
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
