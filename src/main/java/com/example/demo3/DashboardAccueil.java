package com.example.demo3;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Transport;
import services.ServiceLivraison;
import services.ServiceTransport;
import services.UserSessionService;

import javax.imageio.IIOParam;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardAccueil extends Application implements Initializable {
    @FXML
    private Button acceuil_btn;

    @FXML
    private Button close;

    @FXML
    private Button gestionlivraison_btn;

    @FXML
    private Button gestiontransport_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Label name;

    @FXML
    private Label transport_count;

    @FXML
    private Label livraison_count;

    @FXML
    private Label livraison_Aaffecter;

    @FXML
    private BarChart<String, Integer> livraisonbydate;

    @FXML
    private PieChart transportbyetat;

    private void displayTransportCount() {

        int livreur_id = UserSessionService.getInstance().getLoggedInUserId();

        try {
            ServiceTransport serviceTransport = new ServiceTransport();
            int transportCount = serviceTransport.countTransports(livreur_id);
            transport_count.setText("" + transportCount);
        } catch (SQLException e) {
            e.printStackTrace();
            transport_count.setText("0");
        }
    }
    private void displayLivraisonCount() {

        int livreur_id = UserSessionService.getInstance().getLoggedInUserId();

        try {
            ServiceLivraison serviceLivraison = new ServiceLivraison();
            int livraisonCount = serviceLivraison.countLivraisons(livreur_id);
            livraison_count.setText("" + livraisonCount);
        } catch (SQLException e) {
            e.printStackTrace();
            livraison_count.setText("0");
        }
    }
    private void displayLivraisonAaffecter() {

        int livreur_id = UserSessionService.getInstance().getLoggedInUserId();

        try {
            ServiceLivraison serviceLivraison = new ServiceLivraison();
            int livraisonAaffecter = serviceLivraison.livraisons_Aaffecter(livreur_id);
            livraison_Aaffecter.setText("" + livraisonAaffecter);
        } catch (SQLException e) {
            e.printStackTrace();
            livraison_Aaffecter.setText("0");
        }
    }

    @FXML
    public void switchToDashboard1_livraison(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard1.fxml"));
            Parent root = loader.load();

            DashboardController dashboard1Controller = loader.getController();

            dashboard1Controller.navigateToLivraisonForm();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void switchToDashboard1_transport(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard1.fxml"));
            Parent root = loader.load();

            DashboardController dashboard1Controller = loader.getController();

            dashboard1Controller.navigateToTransportForm();

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToDashboardWeather(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardWeather.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void populateLivraisonByDateChart() {

        int livreur_id = UserSessionService.getInstance().getLoggedInUserId();

        try {
            ServiceLivraison serviceLivraison = new ServiceLivraison();
            Map<String, Integer> livraisonsByDate = serviceLivraison.countLivraisonsByDate(livreur_id);
            XYChart.Series<String, Integer> series = new XYChart.Series<>();
            series.setName("Livraisons");

            for (Map.Entry<String, Integer> entry : livraisonsByDate.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            livraisonbydate.getData().add(series);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateTransportByEtatChart() {

        int livreur_id = UserSessionService.getInstance().getLoggedInUserId();

        try {
            ServiceTransport serviceTransport = new ServiceTransport();
            Map<String, Long> transportsByEtat = serviceTransport.countTransportsByEtat(livreur_id);

            List<PieChart.Data> chartData = transportsByEtat.entrySet().stream()
                    .map(entry -> new PieChart.Data(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            transportbyetat.getData().addAll(chartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupPieChartMouseEvents(PieChart chart) {
        final Label caption = new Label("");
        caption.setTextFill(Color.BLACK);
        caption.setStyle("-fx-font: 18 arial;");

        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    e -> {
                        double offsetX = -20;
                        double offsetY = -20;
                        caption.setTranslateX(e.getSceneX() + offsetX);
                        caption.setTranslateY(e.getSceneY() + offsetY );
                        double total = chart.getData().stream().mapToDouble(PieChart.Data::getPieValue).sum();
                        double percentage = (data.getPieValue() / total) * 100;
                        String info = String.format("%s: %.2f%%", data.getName(), percentage);
                        caption.setText(info);
                    });
        }

        main_form.getChildren().add(caption);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashboardAccueil.fxml")));
        stage.getIcons().add(new Image("com/example/demo3/images/MediCare (1).png"));
        stage.setTitle("ParaPharma+");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTransportCount();
        displayLivraisonCount();
        displayLivraisonAaffecter();
        acceuil_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #08b4c7 , #007fff);");
        populateLivraisonByDateChart();
        populateTransportByEtatChart();
        setupPieChartMouseEvents(transportbyetat);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
