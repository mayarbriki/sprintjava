package com.enigmaaa;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.event.ActionEvent;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BlogController {

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
    private Button retour;

    @FXML
    private TextField txtContent;

    @FXML
    private ImageView iv;
    @FXML
    private TextField nomChoice;


    @FXML
    public void initialize() {



        initializeColumns();
        displayBlogs();

        Image myImage = new Image(getClass().getResourceAsStream("/images/alimentaire.jpeg"));
        iv.setImage(myImage);

        table();

        nomChoice.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercher(newValue);
        });

        refresh(null);

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


    @FXML
    void add(ActionEvent event) {
        // Get the input values from the text fields
        String title = txtTitle.getText();
        String content = txtContent.getText();
        // Check if title and content fields are empty
        if (title.isEmpty() || content.isEmpty()) {
            showAlert("Le titre et le contenu ne peuvent pas être vides!", Alert.AlertType.ERROR);
            return;
        }
        // Proceed with adding to the database
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO blog (Title, Content, PublicationDate , LastUpdatedDate ) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, content);
            preparedStatement.setDate(3,new java.sql.Date(System.currentTimeMillis()));
            preparedStatement.setDate(4,new java.sql.Date(System.currentTimeMillis()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                // Show success message
                showAlert("Article ajouté avec succès !", Alert.AlertType.CONFIRMATION);
                // Clear text fields after adding
                txtTitle.clear();
                txtContent.clear();
                // Refresh table view
                blogTable.getItems().clear(); // Clear table
                displayBlogs(); // Re-populate table
            } else {
                // Show error message
                showAlert("Échec de l'ajout de l'article !", Alert.AlertType.ERROR);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message
            showAlert("Une erreur s'est produite lors de l'ajout de l'article !", Alert.AlertType.ERROR);
        }
    }





    @FXML
    void delete(ActionEvent event) {
        blog selectedItem = blogTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                Connection connection = MyDatabase.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM blog WHERE Title = ?");
                preparedStatement.setString(1, selectedItem.getTitle());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Article supprimé avec succès !", Alert.AlertType.CONFIRMATION);
                    // Refresh table view
                    blogTable.getItems().remove(selectedItem);
                } else {
                    showAlert("Échec de la suppression de l'article !", Alert.AlertType.ERROR);
                }

                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Une erreur s'est produite lors de la suppression de l'article !", Alert.AlertType.ERROR);

            }
        } else {
            showAlert("Veuillez sélectionner un article à supprimer !", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void update(ActionEvent event) {
        // Get selected item from the table
        blog selectedItem = blogTable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedItem != null) {
            // Retrieve updated values from text fields
            String newTitle = txtTitle.getText();
            String newContent = txtContent.getText();

            // Check if any of the fields are empty
            if (newTitle.isEmpty() || newContent.isEmpty()) {
                showAlert("Le titre et le contenu ne peuvent pas être vides!", Alert.AlertType.ERROR);
                return;
            }

            try {
                // Get the current system date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = dateFormat.format(new Date());

                // Establish connection to the database
                Connection connection = MyDatabase.getInstance().getConnection();

                // Construct SQL update statement
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE blog SET Title = ?, Content = ?, LastUpdatedDate = ? WHERE Title = ?");
                preparedStatement.setString(1, newTitle);
                preparedStatement.setString(2, newContent);
                preparedStatement.setDate(3, java.sql.Date.valueOf(currentDate)); // Convert string to SQL Date
                preparedStatement.setString(4, selectedItem.getTitle());

                // Execute update statement
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    showAlert("Article mis à jour avec succès !", Alert.AlertType.CONFIRMATION);

                    // Update the selected item in the observable list
                    selectedItem.setTitle(newTitle);
                    selectedItem.setContent(newContent);
                    selectedItem.setLastUpdatedDate(java.sql.Date.valueOf(currentDate)); // Convert string to SQL Date

                    // Refresh table view
                    blogTable.refresh();
                } else {
                    showAlert("Échec de la mise à jour de l'article !", Alert.AlertType.ERROR);
                }

                // Close prepared statement
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Une erreur s'est produite lors de la mise à jour de l'article !", Alert.AlertType.ERROR);
            }

        } else {
            // Show error message if no item is selected
            showAlert("Veuillez sélectionner un article à mettre à jour !", Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void table() {
        Connection connection = MyDatabase.getInstance().getConnection();

        ObservableList<blog> blogs = FXCollections.observableArrayList();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `Title`, `Content` FROM `blog`");
            while (resultSet.next()) {
                blog b = new blog(
                        resultSet.getString("Title"),
                        resultSet.getString("Content"));
                blogs.add(b);
            }
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(BlogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        blogTable.setItems(blogs);

        blogTable.setRowFactory(tv -> {
            TableRow<blog> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    int myIndex = blogTable.getSelectionModel().getSelectedIndex();
                    if (myIndex >= 0) {
                        blog selectedItem = blogTable.getItems().get(myIndex);
                        txtTitle.setText(selectedItem.getTitle());
                        txtContent.setText(selectedItem.getContent());
                    }
                }
            });
            return myRow;
        });
    }




    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void refresh(ActionEvent event) {
        txtTitle.clear();
        txtContent.clear();
        nomChoice.clear();

        blogTable.getItems().clear();
        displayBlogs();


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
        }


    }
}