package com.example.demo3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Produit;
import models.User;
import services.ServiceProduit;
import services.ServiceUser;
import utils.MyDatabase;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;


public class Dashboard extends Application implements Initializable {
    public String path;

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
        Scene scene = new Scene(fxmlLoader.load(), 950, 650);
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
        refreshTable();

        loadDataIntoTableView();
        setupImageColumn();
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
        String role = comb.getSelectionModel().getSelectedItem(); // Retrieve selected item from ComboBox
        String ageString = age.getText();
        int ageInt = Integer.parseInt(ageString);

        // Now you can create the Personne object with retrieved values
        User user = new User(ageInt, nom.getText(), prenom.getText(), pwd.getText(), role);
        ServiceUser sp = new ServiceUser(); // Use connection object

        try {
            sp.ajouteradminT(user);
            System.out.println(sp.recupererT());
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

    private void setupTableColumns() {
        // idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
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


}
