package com.example.demo3;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Transport;
import services.ServiceTransport;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardAdminT extends Application implements Initializable {

    @FXML
    private Button acceuil_btn;

    @FXML
    private Button close;

    @FXML
    private Button gestionCommande_btn;

    @FXML
    private Button gestionProduit_btn;

    @FXML
    private Button gestionlivraison_btn;

    @FXML
    private Button gestiontransport_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button maximizeButton;

    @FXML
    private FontAwesomeIconView minimize;

    @FXML
    private Label name;

    @FXML
    private TableColumn<Transport, Integer> transport_col_ID;

    @FXML
    private TableColumn<Transport, String> transport_col_Type;

    @FXML
    private TableColumn<Transport, String> transport_col_annefab;

    @FXML
    private TableColumn<Transport, String> transport_col_etat;

    @FXML
    private TableColumn<Transport, String> transport_col_marque;

    @FXML
    private TableColumn<Transport, String> transport_col_matricule;

    @FXML
    private AnchorPane transport_form;

    @FXML
    private TextField transport_search;

    @FXML
    private TableView<Transport> transport_tableview;
    private FilteredList<Transport> filteredTransportData;

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
            List<Transport> transportList = new ServiceTransport().recupererT();

            transport_tableview.getItems().clear();

            transport_tableview.getItems().addAll(transportList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setupTransportSearch() {
        filteredTransportData = new FilteredList<>(transport_tableview.getItems(), p -> true);

        transport_tableview.setItems(filteredTransportData);

        transport_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTransportData.setPredicate(transport -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return transport.getType().toLowerCase().contains(lowerCaseFilter)
                        || transport.getMarque().toLowerCase().contains(lowerCaseFilter)
                        || transport.getMatricule().toLowerCase().contains(lowerCaseFilter)
                        || transport.getEtat().toLowerCase().contains(lowerCaseFilter)
                        || transport.getAnneefab().toString().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(transport.getId()).toLowerCase().contains(lowerCaseFilter);
            });
        });
    }


    @FXML
    public void switchToDashboardAdminL(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardAdminL.fxml"));
            Parent root = loader.load();
            DashboardAdminL controller = loader.getController();
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
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashboardAdminT.fxml")));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setupTableColumns();
        refreshTable();
        loadDataIntoTableView();
        setupTransportSearch();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}

