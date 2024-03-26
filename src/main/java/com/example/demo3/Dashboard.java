package com.example.demo3;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import java.util.List;

import java.sql.SQLException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import services.ServiceProduit;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Personne;
import models.Produit;
import services.ServicePersonne;
import utils.MyDatabase;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.TableView;


public class Dashboard extends Application implements Initializable {
    @FXML
    private Button supprimer;
    public String path;
    private Produit selectedProduit; // Variable to store the selected product
    @FXML
    private TableColumn<Produit, Integer> idP;

    @FXML
    private TableView<Produit> tableViewProduit;
    @FXML
    private Label imagePathLabel;

    @FXML
    private TableColumn<Produit, String> descriptionproduit;

    @FXML
    private TableColumn<Produit, String> imageP;
    @FXML
    private TableColumn<Produit, String> nomP;
    @FXML
    private TableColumn<Produit, String> prix;
    @FXML
    private TableColumn<Produit, Integer> quantite;


    @FXML
    private TextArea description;
    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private TextField nomproduit;
    @FXML
    private AnchorPane anchorPane2;
    @FXML
    private Button add;
    @FXML
    private TextField pwd;
    @FXML
    private TextField age;
    @FXML
    private TextField nom;
    @FXML
    private Button modifier;
    @FXML
    private TextField prenom;
    @FXML
    private Button commande;
    @FXML
    private ComboBox<String> comb;
    @FXML
    private Button addproduit;
    @FXML
    private Button livraison;

    @FXML
    private Button produit;
    @FXML
    private BorderPane layout;
    @FXML
    private Button user;
    @FXML
    private ImageView imgview;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard.fxml"));
       // Scene scene = new Scene(fxmlLoader.load(), 1520, 770);
        Scene scene = new Scene(fxmlLoader.load(), 720, 570);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    //void Select(ActionEvent event) {
      //  String role = comb.getSelectionModel().getSelectedItem().toString();
    //}
    /*@FXML
    private void handleProduitButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajouterproduit.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    @FXML
    private void handleModifierButtonClick(ActionEvent event) {
        System.out.println("Modifier button clicked");
        if (selectedProduit != null) {
            // Step 1: Populate text fields with data from the selected product
            nomproduit.setText(selectedProduit.getNom());
            description.setText(selectedProduit.getDescription());
            // Populate other text fields as needed

            // Step 2: Modify data if needed
            // For example, if the user changes the product name or description in the text fields,
            // update the corresponding fields of the selected product object accordingly
            selectedProduit.setNom(nomproduit.getText());
            selectedProduit.setDescription(description.getText());
            // Update other fields as needed

            try {
                // Step 3: Update the modified product in the database
                new ServiceProduit().modifier(selectedProduit);

                // Step 4: Refresh TableView to reflect the changes

            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
            }
        } else {
            // If no item is selected, display an error message or handle it accordingly
            System.out.println("No product selected for modification.");
        }
    }



    // Existing code...


    @FXML
    private void handleProduitButtonClick(ActionEvent event) {
        // This method is already the event handler for the button click,
        // so you don't need to define another event handler inside it.
        // Simply put your logic directly here.

        anchorPane1.setVisible(false);
        anchorPane2.setVisible(true);
    }
    @FXML
    private void handleProduitButtonClick1(ActionEvent event) {
        // This method is already the event handler for the button click,
        // so you don't need to define another event handler inside it.
        // Simply put your logic directly here.

        anchorPane1.setVisible(true);
        anchorPane2.setVisible(false);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> list = FXCollections.observableArrayList("admin", "livreur");
        comb.setItems(list);
        anchorPane1.setVisible(false);
        // Set anchorPane2 invisible initially
        anchorPane2.setVisible(true);
           setupTableColumns();

        loadDataIntoTableView();
        setupImageColumn();
        tableViewProduit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedProduit = newValue; // Update selectedProduit with the newly selected item
            System.out.println("Selected Product: " + selectedProduit); // Print selected product for debugging
        });


// Add similar listeners for other text fields as needed



    }

    private void setupImageColumn() {
        imageP.setCellFactory(column -> {
            return new TableCell<Produit, String>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(String imagePath, boolean empty) {
                    super.updateItem(imagePath, empty);

                    if (empty || imagePath == null) {
                        setGraphic(null);
                    } else {
                        // Load the image from the file path
                        File file = new File(imagePath);
                        Image image = new Image(file.toURI().toString());

                        // Set the image to the ImageView
                        imageView.setImage(image);
                        imageView.setFitWidth(50); // Adjust the width as needed
                        imageView.setFitHeight(50); // Adjust the height as needed
                        setGraphic(imageView);
                    }
                }
            };
        });
    }
    public void add(ActionEvent event) throws SQLException {
        Connection connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return;
        }

        // Retrieve selected role from ComboBox
        String role = comb.getSelectionModel().getSelectedItem().toString(); // Retrieve selected item from ComboBox
        String ageString = age.getText();
        int ageInt = Integer.parseInt(ageString);

        // Now you can create the Personne object with retrieved values
        Personne personne = new Personne(ageInt, nom.getText(), prenom.getText(), pwd.getText(), role);
        ServicePersonne sp = new ServicePersonne(); // Use connection object

        try {
            sp.ajouteradmin(personne);
            System.out.println(sp.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());

        }
    }
    @FXML
    Label label = new Label();
    public void insertImage() {

        // String chosenImagePath;
        FileChooser open = new FileChooser();

        Stage stage = (Stage) layout.getScene().getWindow();

        File file = open.showOpenDialog(stage);

        if (file != null) {

            path = file.getAbsolutePath();

            path = path.replace("\\", "\\\\");
            imagePathLabel.setText(path);

            label.setText(path);

            Image image = new Image(file.toURI().toString(), 110, 110, false, true);

            imgview.setImage(image);

        } else {

            System.out.println("NO DATA EXIST!");

        }
    }
    public void addproduit1(ActionEvent event) throws SQLException {
        Connection connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return;
        }
        //  String imagePath = insertImage();  // Call insertImage to get the path (or null)
        String imagePath = path;
        if (imagePath == null) {
            System.out.println("No image selected!");
            return;
        }
        Produit produit = new Produit(25, nomproduit.getText(), description.getText(), imagePath);
        ServiceProduit sp = new ServiceProduit(); // Use connection object

        try {
            sp.ajouter(produit);
            System.out.println(sp.recuperer());
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void modifyProduit(ActionEvent event) {
        Connection connection = MyDatabase.getInstance().getConnection();
        if (connection == null) {
            System.out.println("Connection is null!");
            return;
        }

        if (selectedProduit == null) {
            System.out.println("No product selected for modification.");
            return;
        }

        // Get modified data from text fields
        String nom = nomproduit.getText();
        String descriptionText = description.getText();
        // Get other modified fields as needed

        // Update selected product with modified data
        selectedProduit.setNom(nom);
        selectedProduit.setDescription(descriptionText);
        // Update other fields as needed

        ServiceProduit sp = new ServiceProduit(); // Use connection object

        try {
            // Update the modified product in the database
            sp.modifier(selectedProduit);
            System.out.println("Product modified successfully.");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }



    private void setupTableColumns() {
        // idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idP.setCellValueFactory(new PropertyValueFactory<>("id"));

        nomP.setCellValueFactory(new PropertyValueFactory<>("nom"));
        imageP.setCellValueFactory(new PropertyValueFactory<>("image"));
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        descriptionproduit.setCellValueFactory(new PropertyValueFactory<>("description"));



    }
    @FXML
    private void loadDataIntoTableView() {
        ServiceProduit sp = new ServiceProduit(); // Create an instance of your service
        ObservableList<Produit> produits = null;
        try {
            // Fetch the list of products from your service
            produits = FXCollections.observableArrayList(sp.recuperer());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }

        // Set the fetched products as the items of the TableView
        tableViewProduit.setItems(produits);
    }
    @FXML
    private void refreshTable() {
        try {
            // Fetch the list of products from the database
            List<Produit> produitList = new ServiceProduit().recuperer();

            // Clear the existing items in the TableView
            tableViewProduit.getItems().clear();

            // Add the fetched products to the TableView
            tableViewProduit.getItems().addAll(produitList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }
    @FXML
    private void handleSupprimerButtonClick(ActionEvent event) {
        Produit selectedProduit = tableViewProduit.getSelectionModel().getSelectedItem();
        if (selectedProduit != null) {
            try {
                // Delete the selected item from the database
                new ServiceProduit().supprimer(selectedProduit.getId());
                // Remove the item from the TableView
                tableViewProduit.getItems().remove(selectedProduit);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception appropriately
            }
        } else {
            System.out.println("No product selected for deletion.");
        }
    }
    @FXML
    private void handleTableViewSelection() {
        selectedProduit = tableViewProduit.getSelectionModel().getSelectedItem();
    }

}







