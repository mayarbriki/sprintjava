package com.enigmaaa;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.blog;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

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
    private ImageView iv;

    @FXML
    private ImageView iv1;


    @FXML
    private TextField nomChoice;


    @FXML
    public void initialize() {
        initializeColumns();
        displayBlogs();

        Image myImage = new Image(getClass().getResourceAsStream("/images/health.png"));
        iv.setImage(myImage);

        Image myImage1 = new Image(getClass().getResourceAsStream("/images/care.jpg"));
        iv1.setImage(myImage1);


        nomChoice.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercher(newValue);
        });

        retour.setOnAction(event -> loadScene("Home.fxml"));


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

}
