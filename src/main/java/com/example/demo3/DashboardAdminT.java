package com.example.demo3;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Transport;
import services.ServiceTransport;
import javafx.scene.image.WritableImage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

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
    private TableColumn<Transport, String> transport_col_nom;

    @FXML
    private AnchorPane transport_form;

    @FXML
    private TextField transport_search;

    @FXML
    private TableView<Transport> transport_tableview;
    private FilteredList<Transport> filteredTransportData;

    @FXML
    private Button qrCode;

    @FXML
    private ImageView qrcodeImage;

    private void setupTableColumns() {
        transport_col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        transport_col_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        transport_col_marque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        transport_col_matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        transport_col_annefab.setCellValueFactory(new PropertyValueFactory<>("anneefab"));
        transport_col_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
        transport_col_nom.setCellValueFactory(new PropertyValueFactory<>("livreur"));

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

            ObservableList<Transport> observableList = FXCollections.observableArrayList(transportList);

            transport_tableview.setItems(observableList);
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
                        || transport.getLivreur().toLowerCase().contains(lowerCaseFilter)
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

    public void handle() {
        qrCode.setOnAction(event -> {
            Transport selectedTransport = transport_tableview.getSelectionModel().getSelectedItem();

            if (selectedTransport != null) {
                String qrData = "ID: " + selectedTransport.getId() +
                        "\nType: " + selectedTransport.getType() +
                        "\nMarque: " + selectedTransport.getMarque() +
                        "\nMatricule: " + selectedTransport.getMatricule() +
                        "\nAnnée de fabrication: " + selectedTransport.getAnneefab() +
                        "\nÉtat: " + selectedTransport.getEtat() +
                        "\nLivreur: " + selectedTransport.getLivreur();

                generateAndDisplayQRCode(qrData);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucun transport sélectionné");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez sélectionner un transport pour générer le QR code.");
                alert.showAndWait();
            }
        });
    }


    private void generateAndDisplayQRCode(String qrData) {
        try {

            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            BitMatrix matrix = new MultiFormatWriter().encode(qrData, BarcodeFormat.QR_CODE, 184, 199, hints);

            qrcodeImage.setFitWidth(100);
            qrcodeImage.setFitHeight(100);

            Image qrCodeImage = matrixToImage(matrix);

            qrcodeImage.setImage(qrCodeImage);
            Alert a = new Alert(Alert.AlertType.WARNING);

            a.setTitle("Succes");
            a.setContentText("QR code générer");
            a.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Image matrixToImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixelColor = matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                pixelWriter.setArgb(x, y, pixelColor);
            }
        }

        System.out.println("Matrice convertie en image avec succès");

        return writableImage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashboardAdminT.fxml")));
        stage.getIcons().add(new Image("com/example/demo3/images/MediCare (1).png"));
        stage.setTitle("ParaPharma+");
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

