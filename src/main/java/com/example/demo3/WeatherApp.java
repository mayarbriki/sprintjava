package com.example.demo3;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class WeatherApp extends Application {
    @FXML
    private Button acceuil_btn;

    @FXML
    private Button close;

    @FXML
    private Label currentTempLabel;

    @FXML
    private Button gestionlivraison_btn;

    @FXML
    private Button gestiontransport_btn;

    @FXML
    private Button gestiontransport_btn1;

    @FXML
    private Label humidityLabel;

    @FXML
    private Button logout;

    @FXML
    private Label matriculeErrorLabel;

    @FXML
    private Label maxTempLabel;

    @FXML
    private Button maximizeButton;

    @FXML
    private Label minTempLabel;

    @FXML
    private FontAwesomeIconView minimize;

    @FXML
    private Label name;

    @FXML
    private TextField transport_search;

    @FXML
    private Label weatherDescriptionLabel;

    @FXML
    private ImageView weatherDescriptionImage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DashboardWeather.fxml")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void handleSearchButtonClicked(ActionEvent event) {
        // Get the city name from the TextField
        String city = transport_search.getText();

        try {
            String encodedCity = URLEncoder.encode(city, "UTF-8");

            String apiKey = "1c3a76a7609a1b37f16259d09a818058";
            URI uri = URI.create("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey + "&units=metric");

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                JsonObject jsonResponse = new Gson().fromJson(response.body(), JsonObject.class);

                double minTemp = jsonResponse.getAsJsonObject("main").get("temp_min").getAsDouble();
                double maxTemp = jsonResponse.getAsJsonObject("main").get("temp_max").getAsDouble();
                double currentTemp = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
                double humidity = jsonResponse.getAsJsonObject("main").get("humidity").getAsDouble();
                String weatherDescription = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

                minTempLabel.setText("Min Temp: " + minTemp + "°C");
                maxTempLabel.setText("Max Temp: " + maxTemp + "°C");
                currentTempLabel.setText("Current Temp: " + currentTemp + "°C");
                humidityLabel.setText("Humidity: " + humidity + "%");
                weatherDescriptionLabel.setText("Weather: " + weatherDescription);

                displayWeatherImage(weatherDescription);
            } else {
                System.out.println("Error fetching weather data: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayWeatherImage(String weatherDescription) {
        String imagePath = "";

        switch (weatherDescription.toLowerCase()) {
            case "clear sky":
                imagePath = "/com/example/demo3/images/01d.png";
                break;
            case "few clouds":
            case "scattered clouds":
            case "broken clouds":
                imagePath = "/com/example/demo3/images/02d.png";
                break;
            case "shower rain":
            case "rain":
            case "light rain":
                imagePath = "/com/example/demo3/images/09.png";
                break;
            case "thunderstorm":
                imagePath = "/com/example/demo3/images/11.png";
                break;
            case "snow":
                imagePath = "/com/example/demo3/images/13.png";
                break;
            case "mist":
                imagePath = "/com/example/demo3/images/50.png";
                break;
            default:
                imagePath = "/com/example/demo3/images/01d.png";
                break;
        }

        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
            weatherDescriptionImage.setImage(image);
        } catch (NullPointerException e) {
            System.err.println("Failed to load image: " + imagePath);
            e.printStackTrace();
        }
    }
}
