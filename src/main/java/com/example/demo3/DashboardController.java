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

public class DashboardController extends Application implements Initializable {

    @FXML
    public AnchorPane main_form;
    @FXML
    private AnchorPane Livraison_form;

    @FXML
    private Button acceuil_btn;

    @FXML
    private Label adresseLiv;

    @FXML
    private Button affecter;

    @FXML
    private Button close;

    @FXML
    private Label commande;

    @FXML
    private Label dateLiv;

    @FXML
    private Label description;

    @FXML
    private Label etat;

    @FXML
    private Button gestionlivraison_btn;

    @FXML
    private Button gestiontransport_btn;

    @FXML
    private Label id_livraison;

    @FXML
    private TableColumn<?, ?> livraison_col_AdresseLiv;

    @FXML
    private TableColumn<?, ?> livraison_col_Commande;

    @FXML
    private TableColumn<?, ?> livraison_col_DateLiv;

    @FXML
    private TableColumn<?, ?> livraison_col_Description;

    @FXML
    private TableColumn<?, ?> livraison_col_Etat;

    @FXML
    private TableColumn<?, ?> livraison_col_ID;

    @FXML
    private TableColumn<?, ?> livraison_col_Matricule;

    @FXML
    private TableColumn<?, ?> livraison_col_Nom;

    @FXML
    private TableView<Livraison> livraison_tableview;

    @FXML
    private Button logout;

    @FXML
    private ComboBox<String> matricule;

    @FXML
    private FontAwesomeIconView minimize;

    @FXML
    private Label name;

    @FXML
    private Button transport_ajouter_btn;

    @FXML
    private Button transport_clear_btn;

    @FXML
    private TableColumn<?, ?> transport_col_ID;

    @FXML
    private TableColumn<?, ?> transport_col_Type;

    @FXML
    private TableColumn<?, ?> transport_col_annefab;

    @FXML
    private TableColumn<?, ?> transport_col_etat;

    @FXML
    private TableColumn<?, ?> transport_col_marque;

    @FXML
    private TableColumn<?, ?> transport_col_matricule;

    @FXML
    private DatePicker transport_date;

    @FXML
    private Button transport_demontrer_btn;

    @FXML
    private ComboBox<String> transport_etat;

    @FXML
    private AnchorPane transport_form;

    @FXML
    private ComboBox<String> transport_marque;

    @FXML
    private TextField transport_matricule;

    @FXML
    private Button transport_modifier_btn;

    @FXML
    private TextField transport_search;

    @FXML
    private Button transport_supprimer_btn;

    @FXML
    private TableView<Transport> transport_tableview;

    @FXML
    private ComboBox<String> transport_type;

    @FXML
    private Label matriculeErrorLabel;


    public void ajouterT(ActionEvent event) throws SQLException {
        // Retrieve selected items from ComboBoxes
        String selectedType = (String) transport_type.getSelectionModel().getSelectedItem();
        String selectedMarque = (String) transport_marque.getSelectionModel().getSelectedItem();
        String selectedEtat = (String) transport_etat.getSelectionModel().getSelectedItem();
        String matriculeValue = transport_matricule.getText();
        LocalDate selectedDate = transport_date.getValue();

        if (selectedType == null || selectedMarque == null || selectedEtat == null || matriculeValue.isEmpty() || selectedDate == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides");
            alert.showAndWait();
            return;
        }

        if (!matriculeValue.matches("\\d{3}-TUN-\\d{4}")) {
            // If the format is not matched, display an error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Matricule doit être dans le format ***-TUN-****");
            alert.showAndWait();
            return; // Exit the function without proceeding further
        }

        Transport transport = new Transport(0, selectedType, selectedMarque, matriculeValue, selectedEtat, Date.valueOf(selectedDate));

        ServiceTransport st = new ServiceTransport();

        try {
            st.ajouterT(transport);
            System.out.println("Transport added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to add transport: " + e.getMessage());
        }
    }

    @FXML
    public void modifierT() {
        Transport selectedTransport = (Transport) transport_tableview.getSelectionModel().getSelectedItem();
        if (selectedTransport != null) {
            // Update the selected transport with the new values
            selectedTransport.setType((String) transport_type.getSelectionModel().getSelectedItem());
            selectedTransport.setMarque((String) transport_marque.getSelectionModel().getSelectedItem());
            selectedTransport.setMatricule(transport_matricule.getText());
            selectedTransport.setEtat((String) transport_etat.getSelectionModel().getSelectedItem());
            selectedTransport.setAnneefab(Date.valueOf(transport_date.getValue()));

            try {
                ServiceTransport st = new ServiceTransport();
                st.modifierT(selectedTransport);
                refreshTable();
                System.out.println("Transport updated successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to update transport: " + e.getMessage());
            }
        }
    }


    public void supprimerT(ActionEvent event) {
        // Get the selected transport from the table view
        Transport selectedTransport = transport_tableview.getSelectionModel().getSelectedItem();
        if (selectedTransport != null) {
            try {
                ServiceTransport st = new ServiceTransport();
                st.supprimerT(selectedTransport.getId());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Delete Success");
                alert.setHeaderText(null);
                alert.setContentText("Transport deleted successfully.");
                alert.showAndWait();
                // Refresh the table view after deletion
                refreshTable();
            } catch (SQLException ex) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Delete Error");
                alert.setHeaderText(null);
                alert.setContentText("Error deleting transport: " + ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a transport to delete.");
            alert.showAndWait();
        }
    }

    private void setupTableColumns() {
        transport_col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        transport_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        transport_col_marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        transport_col_matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        transport_col_annefab.setCellValueFactory(new PropertyValueFactory<>("anneefab"));
        transport_col_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));

    }

    @FXML
    private void loadDataIntoTableView() {
        ServiceTransport sp = new ServiceTransport();
        ObservableList<Transport> transports = null;
        try {
            transports = FXCollections.observableArrayList(sp.recupererT());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        transport_tableview.setItems(transports);
    }

    @FXML
    private void refreshTable() {
        try {
            // Fetch the list of products from the database
            List<Transport> transportList = new ServiceTransport().recupererT();

            // Clear the existing items in the TableView
            transport_tableview.getItems().clear();

            // Add the fetched products to the TableView
            transport_tableview.getItems().addAll(transportList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupLivraisonTableColumns() {
        livraison_col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        livraison_col_DateLiv.setCellValueFactory(new PropertyValueFactory<>("dateLiv"));
        livraison_col_AdresseLiv.setCellValueFactory(new PropertyValueFactory<>("adresseLiv"));
        livraison_col_Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        livraison_col_Etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        livraison_col_Commande.setCellValueFactory(new PropertyValueFactory<>("commande"));
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
            // Fetch the list of Livraisons from the database
            List<Livraison> livraisonList = new ServiceLivraison().recupererL();

            // Clear the existing items in the TableView
            livraison_tableview.getItems().clear();

            // Add the fetched Livraisons to the TableView
            livraison_tableview.getItems().addAll(livraisonList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void switchForm(ActionEvent event) {
        /*if (event.getSource() == acceuil_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            homeTotalEmployees();
            homeEmployeeTotalPresent();
            homeTotalInactive();
            homeChart();

        } else */
        if (event.getSource() == gestionlivraison_btn) {

            Livraison_form.setVisible(true);
            transport_form.setVisible(false);

            gestionlivraison_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            acceuil_btn.setStyle("-fx-background-color:transparent");
            gestiontransport_btn.setStyle("-fx-background-color:transparent");

        } else if (event.getSource() == gestiontransport_btn) {
            Livraison_form.setVisible(false);
            transport_form.setVisible(true);

            gestiontransport_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            gestionlivraison_btn.setStyle("-fx-background-color:transparent");
            acceuil_btn.setStyle("-fx-background-color:transparent");

        }

    }

    public void TransportReset() {

        transport_type.getSelectionModel().clearSelection();
        transport_marque.getSelectionModel().clearSelection();
        transport_etat.getSelectionModel().clearSelection();
        transport_matricule.setText("");
        transport_date.setValue(null);
    }

    @FXML
    private Button maximizeButton;

    private boolean maximized = false;

    @FXML
    void maximize(ActionEvent event) {
        Stage stage = (Stage) main_form.getScene().getWindow();
        if (!maximized) {
            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            stage.setX(screenSize.getMinX());
            stage.setY(screenSize.getMinY());
            stage.setWidth(screenSize.getWidth());
            stage.setHeight(screenSize.getHeight());
            maximized = true;
        } else {
            stage.setMaximized(false);
            maximized = false;
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupTableColumns();
        refreshTable();
        loadDataIntoTableView();

        setupLivraisonTableColumns();
        refreshLivraisonTable();
        loadDataIntoLivraisonTableView();

        transport_tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Transport selectedTransport = transport_tableview.getSelectionModel().getSelectedItem();
                transport_type.setValue(selectedTransport.getType());
                transport_marque.setValue(selectedTransport.getMarque());
                transport_matricule.setText(selectedTransport.getMatricule());
                transport_etat.setValue(selectedTransport.getEtat());
                transport_date.setValue(selectedTransport.getAnneefab().toLocalDate());
            } else {
                TransportReset();
            }
        });

    }

    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard1.fxml")));

        Scene scene = new Scene(root);

        root.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);

            stage.setOpacity(1);
        });

        root.setOnMouseReleased((MouseEvent event) -> {
            stage.setOpacity(1);
        });

        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
