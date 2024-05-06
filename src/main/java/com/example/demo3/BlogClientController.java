package com.example.demo3;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.blog;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.Date;
import java.util.Objects;

public class BlogClientController {

    @FXML
    private Button retour;

    @FXML
    private TableView<blog> blogTable;

    @FXML
    private TableColumn<blog, String> titleColumn;

    @FXML
    private TableColumn<blog, String> contentColumn;

    @FXML
    private TextField ville;


    @FXML
    private Label currentTempLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private TableColumn<blog, Date> publicationDateColumn;

    @FXML
    private TableColumn<blog, Date> lastUpdatedDateColumn;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtContent;

    @FXML
    private ImageView weatherimage;




    @FXML
    private TextField nomChoice;


    //  clé d'API OpenWeatherMap
    private static final String API_KEY = "ec1e3d274394b5cecc3025749df08f92";


    @FXML
    public void initialize() {
        initializeColumns();
        displayBlogs();

    /*  Image myImage = new Image(getClass().getResourceAsStream("/images/care.jpg"));
        iv.setImage(myImage);
*/

        nomChoice.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercher(newValue);
        });

        retour.setOnAction(event -> loadScene("vueuser.fxml"));


    }

    private void initializeColumns() {
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        contentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getContent()));
        publicationDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getPublicationDate()));
        lastUpdatedDateColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLastUpdatedDate()));
    }

    private void displayBlogs() {
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Title, Content , PublicationDate, LastUpdatedDate FROM blog");

            ObservableList<blog> blogs = FXCollections.observableArrayList();
            while (resultSet.next()) {
                // Retrieve data from the result set
                String title = resultSet.getString("Title");
                String content = resultSet.getString("Content");
                Date publicationDate = resultSet.getDate("PublicationDate");
                Date lastUpdatedDate = resultSet.getDate("LastUpdatedDate");

                // Create a new blog object with retrieved data
                blog blog = new blog(title, content, publicationDate, lastUpdatedDate);
                blogs.add(blog);
            }
            // Set the items in the blogTable
            blogTable.setItems(blogs);

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void rechercher(String Title) {
        ObservableList<blog> blogs = FXCollections.observableArrayList();
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Title`, `Content`, `PublicationDate`, `LastUpdatedDate`  FROM `blog` WHERE `Title` LIKE ?");
            preparedStatement.setString(1, "%" + Title + "%"); // Using LIKE operator
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                blog b = new blog(
                        resultSet.getString("Title"),
                        resultSet.getString("Content"),
                        resultSet.getDate("PublicationDate"),
                        resultSet.getDate("LastUpdatedDate"));
                blogs.add(b);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        blogTable.setItems(blogs);
    }

    private void loadScene(String fxmlFileName) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFileName));
            Stage stage = (Stage) retour.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }}


    // Méthode pour récupérer les données météorologiques d'une ville spécifique
    private String getWeatherData(String city) {
        try {
            // Construire l'URL de l'API OpenWeatherMap avec le nom de la ville
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";

            // Créer une connexion HTTP
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .build();

            // Envoyer une requête GET et récupérer la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Retourner les données de réponse
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour afficher les données météorologiques dans l'interface utilisateur
    private void displayWeatherData(String weatherData) {
        if (weatherData != null) {
            // Extraire les valeurs de température directement à partir de la réponse
            double currentTemp = Double.parseDouble(getValueFromResponse(weatherData, "\"temp\":"));
            double humidity = Double.parseDouble(getValueFromResponse(weatherData, "\"humidity\":"));
            currentTempLabel.setText("");
            humidityLabel.setText("");
            // Update UI with weather information
            currentTempLabel.setText( currentTemp + "°C");
            humidityLabel.setText( humidity + "%");
            displayWeatherImage(currentTemp);
        } else {
            System.out.println("Erreur lors de la récupération des données météorologiques.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Erreur lors de la récupération des données météorologiques");
            alert.showAndWait();

        }
    }

    // Méthode utilitaire pour extraire la valeur d'un champ spécifique de la réponse JSON
    private String getValueFromResponse(String response, String field) {
        int index = response.indexOf(field);
        if (index != -1) {
            int startIndex = index + field.length();
            int endIndex = response.indexOf(",", startIndex);
            return response.substring(startIndex, endIndex);
        }
        return null;
    }


    // Méthode pour récupérer les données météorologiques pour la ville spécifiée dans le champ ville
    @FXML
    public void SearchWeather(ActionEvent event) {
        String city = ville.getText();

        // Check if the search field is empty
        if (city.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Veuillez saisir le nom d'une ville.");
            alert.showAndWait();
            return; // Exit the method
        }

        String weatherData = getWeatherData(city);

        // Check if the weather data is null or empty
        if (weatherData == null || weatherData.isEmpty()) {
            // Show an error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Impossible de trouver des données météorologiques pour cette ville.");
            alert.showAndWait();
            return; // Exit the method
        }

        // Display weather data
        displayWeatherData(weatherData);
    }


    private void displayWeatherImage(double currentTemp) {
        String imagePath = "";

        if (currentTemp >= 25) {
            // Hot weather
            imagePath = "/com/example/demo3/images/hot.jpg";
        } else if (currentTemp <= 10) {
            // Cold weather
            imagePath = "/com/example/demo3/images/cold.jpeg";
        } else {
            // Moderate weather
            imagePath = "/com/example/demo3/images/moderate.jpg";
        }

        try {
            // Debug output to print the absolute path of the image resource
            System.out.println("Image Path: " + getClass().getResource(imagePath).toExternalForm());

            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            weatherimage.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Failed to load image: " + imagePath);
            e.printStackTrace();
        }
    }





}
