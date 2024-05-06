package com.example.demo3;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.stage.Stage;
import models.stock;
import utils.MyDatabase;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

public class StockController {

    @FXML
    private TableView<stock> stockTable;

    @FXML
    private TableColumn<stock, String> nomp;

    @FXML
    private TableColumn<stock, Integer> quantitep;

    @FXML
    private TableColumn<stock, Date> datep;

    @FXML
    private TableColumn<stock, String> type;

    @FXML
    private TableColumn<stock, Float> prixx;

    @FXML
    private TableColumn<stock, String> fourn;


    @FXML
    private Button retour;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField tnom;

    @FXML
    private TextField tprix;


    @FXML
    private TextField tquan;

    @FXML
    private TextField ttype;

    @FXML
    private ChoiceBox<String> typeChoice;


    @FXML
    private ChoiceBox<String> fourChoice;



    @FXML
    private TextField nomChoice;

    @FXML
    private Button refr;



    @FXML
    public void initialize() {

        startAutomaticStockCheck();

        initializeColumns();
        afficherStocks();

        nomChoice.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercher(newValue);
        });

        populateTypeChoice();
        populatefourChoice();

        typeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("All Types")) {
                    afficherStocks(); // Show all products
                } else {
                    filter(newValue); // Filter products based on selected type
                }
            }
        });

        table();

        retour.setOnAction(event -> loadScene("dashboard.fxml"));
    }

    private void initializeColumns() {
        nomp.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom_produit()));
        quantitep.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantite()).asObject());
        datep.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        type.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        prixx.setCellValueFactory(data -> new SimpleFloatProperty(data.getValue().getPrix()).asObject()); // Corrected cell value factory
        fourn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFournisseurEmail()));
    }

    private void afficherStocks() {
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s.nom_produit, s.type, s.date, s.quantite, s.prix, \n" +
                    "       CASE \n" +
                    "           WHEN f.id IS NULL THEN 'Pas de fournisseur'\n" +
                    "           ELSE f.email\n" +
                    "       END AS email\n" +
                    "FROM stock s\n" +
                    "LEFT JOIN fournisseur f ON f.id = s.fournisseur_id;\n");
            while (resultSet.next()) {
                stock s = new stock(
                        resultSet.getString("nom_produit"),
                        resultSet.getString("type"),
                        resultSet.getDate("date"),
                        resultSet.getInt("quantite"),
                        resultSet.getFloat("prix"),
                        resultSet.getString("email"));
                stockTable.getItems().add(s);
                System.out.println(s);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void add(ActionEvent event) {
        String nomProduit = tnom.getText();
        String produitType = ttype.getText();
        String quantiteText = tquan.getText();
        String prixText = tprix.getText();
        String fournisseurEmail = fourChoice.getValue();

        // Check if any of the fields are empty
        if (nomProduit.isEmpty() || produitType.isEmpty() || quantiteText.isEmpty() || prixText.isEmpty()) {
            showAlert("Tous les champs doivent être remplis!", Alert.AlertType.ERROR);
            return;
        }

        // Validate and parse quantity
        int quantite = 0;
        try {
            quantite = Integer.parseInt(quantiteText);
            if (quantite <= 0) {
                showAlert("La quantité doit être supérieure à 0!", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("La quantité doit être une valeur numérique!", Alert.AlertType.ERROR);
            return;
        }

        // Validate and parse price
        float prix = 0;
        try {
            prix = Float.parseFloat(prixText);
            if (prix <= 0) {
                showAlert("Le prix unitaire doit être supérieur à 0!", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Le prix unitaire doit être une valeur numérique!", Alert.AlertType.ERROR);
            return;
        }

        // Validate product type
        if (!produitType.matches("[a-zA-Z]+")) {
            showAlert("Le type de produit ne doit contenir que des lettres!", Alert.AlertType.ERROR);
            return;
        }

        // Check if the product already exists in the database
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement checkStatement = connection.prepareStatement("SELECT * FROM stock WHERE nom_produit = ?");
            checkStatement.setString(1, nomProduit);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                showAlert("Le produit existe déjà dans la base de données!", Alert.AlertType.ERROR);
                return;
            }
            checkStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de la vérification de l'existence du produit!", Alert.AlertType.ERROR);
            return;
        }

        // Proceed with adding to the database
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO stock (nom_produit, type, date, quantite, prix, fournisseur_id) VALUES (?, ?, ?, ?, ?, (SELECT id FROM fournisseur WHERE email = ?))");
            preparedStatement.setString(1, nomProduit);
            preparedStatement.setString(2, produitType);
            preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
            preparedStatement.setInt(4, quantite);
            preparedStatement.setFloat(5, prix);
            preparedStatement.setString(6, fournisseurEmail);


            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                // Show success message
                showAlert("Produit ajouté avec succès !", Alert.AlertType.CONFIRMATION);
                // Clear text fields after adding
                tnom.clear();
                tquan.clear();
                tprix.clear();
                ttype.clear();
                // Refresh table view
                stockTable.getItems().clear(); // Clear table
                afficherStocks(); // Re-populate table
            } else {
                // Show error message
                showAlert("Échec de l'ajout du produit !", Alert.AlertType.ERROR);
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message
            showAlert("Une erreur s'est produite lors de l'ajout du produit !", Alert.AlertType.ERROR);
        }
    }





    @FXML
    public void table() {
        stockTable.setRowFactory(tv -> {
            TableRow<stock> myRow = new TableRow<>();
            myRow.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
                    int myIndex = stockTable.getSelectionModel().getSelectedIndex();
                    if (myIndex >= 0) {
                        stock selectedItem = stockTable.getItems().get(myIndex);
                        tnom.setText(selectedItem.getNom_produit());
                        tquan.setText(String.valueOf(selectedItem.getQuantite()));
                        ttype.setText(selectedItem.getType());
                        tprix.setText(String.valueOf(selectedItem.getPrix()));

                        // Set the selected fournisseur email in the choice box
                        String fournisseurEmail = selectedItem.getFournisseurEmail();
                        if (fournisseurEmail != null) {
                            fourChoice.setValue(fournisseurEmail);
                        } else {
                            // If no fournisseur is associated, set the choice box value to null
                            fourChoice.setValue(null);
                        }
                    }
                }
            });
            return myRow;
        });
    }




    @FXML
    void delete(ActionEvent event) {
        stock selectedItem = stockTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                Connection connection = MyDatabase.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM stock WHERE nom_produit = ?");
                preparedStatement.setString(1, selectedItem.getNom_produit());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows > 0) {
                    showAlert("Produit supprimé avec succès !", Alert.AlertType.CONFIRMATION);
                    // Refresh table view
                    stockTable.getItems().remove(selectedItem);
                } else {
                    showAlert("Échec de la suppression du produit !", Alert.AlertType.ERROR);
                }

                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Une erreur s'est produite lors de la suppression du produit !", Alert.AlertType.ERROR);

            }
        } else {
            showAlert("Veuillez sélectionner un produit à supprimer !", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void update(ActionEvent event) {
        // Get selected item from the table
        stock selectedItem = stockTable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedItem != null) {
            // Retrieve updated values from text fields
            String newNomProduit = tnom.getText();
            String newQuantiteText = tquan.getText();
            String newPrixText = tprix.getText();
            String newProduitType = ttype.getText();
            String newFournisseurEmail = fourChoice.getValue(); // Get selected fournisseur email from choice box

            // Check if any of the fields are empty
            if (newNomProduit.isEmpty() || newQuantiteText.isEmpty() || newPrixText.isEmpty() || newProduitType.isEmpty()) {
                showAlert("Tous les champs doivent être remplis!", Alert.AlertType.ERROR);
                return;
            }

            // Validate and parse quantity
            int newQuantite = 0;
            try {
                newQuantite = Integer.parseInt(newQuantiteText);
                if (newQuantite <= 0) {
                    showAlert("La quantité doit être supérieure à 0!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("La quantité doit être une valeur numérique!", Alert.AlertType.ERROR);
                return;
            }

            // Validate and parse price
            float newPrix = 0;
            try {
                newPrix = Float.parseFloat(newPrixText);
                if (newPrix <= 0) {
                    showAlert("Le prix unitaire doit être supérieur à 0!", Alert.AlertType.ERROR);
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Le prix unitaire doit être une valeur numérique!", Alert.AlertType.ERROR);
                return;
            }

            // Validate product type
            if (!newProduitType.matches("[a-zA-Z]+")) {
                showAlert("Le type de produit ne doit contenir que des lettres!", Alert.AlertType.ERROR);
                return;
            }

            try {
                // Establish connection to the database
                Connection connection = MyDatabase.getInstance().getConnection();

                // Construct SQL update statement
                PreparedStatement preparedStatement = connection.prepareStatement("UPDATE stock SET nom_produit = ?, type = ?, date = ?, quantite = ?, prix = ?, fournisseur_id = (SELECT id FROM fournisseur WHERE email = ?) WHERE nom_produit = ?");
                preparedStatement.setString(1, newNomProduit);
                preparedStatement.setString(2, newProduitType);
                preparedStatement.setDate(3, new Date(System.currentTimeMillis()));
                preparedStatement.setInt(4, newQuantite);
                preparedStatement.setFloat(5, newPrix);
                preparedStatement.setString(6, newFournisseurEmail);
                preparedStatement.setString(7, selectedItem.getNom_produit());

                // Execute update statement
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    showAlert("Produit mis à jour avec succès !", Alert.AlertType.CONFIRMATION);

                    // Update the selected item in the observable list
                    selectedItem.setNom_produit(newNomProduit);
                    selectedItem.setQuantite(newQuantite);
                    selectedItem.setType(newProduitType);
                    selectedItem.setPrix(newPrix);
                    // Update fournisseur email
                    selectedItem.setFournisseurEmail(newFournisseurEmail);

                    // Refresh table view
                    stockTable.refresh();
                } else {
                    showAlert("Échec de la mise à jour du produit !", Alert.AlertType.ERROR);
                }

                // Close prepared statement
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Une erreur s'est produite lors de la mise à jour du produit !", Alert.AlertType.ERROR);
            }
        } else {
            // Show error message if no item is selected
            showAlert("Veuillez sélectionner un produit à mettre à jour !", Alert.AlertType.ERROR);
        }
    }


    void rechercher(String nomProduit) {
        ObservableList<stock> stocks = FXCollections.observableArrayList();
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT s.nom_produit, s.type, s.date, s.quantite, s.prix, IFNULL(f.email, 'pas de fournisseur') AS email FROM stock s LEFT JOIN fournisseur f ON f.id = s.fournisseur_id WHERE s.nom_produit LIKE ?");
            preparedStatement.setString(1, "%" + nomProduit + "%"); // Utilisation de l'opérateur LIKE
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stock s = new stock(
                        resultSet.getString("nom_produit"),
                        resultSet.getString("type"),
                        resultSet.getDate("date"),
                        resultSet.getInt("quantite"),
                        resultSet.getFloat("prix"),
                        resultSet.getString("email"));
                stocks.add(s);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stockTable.setItems(stocks);
    }


    @FXML
    private void filter(String selectedType) {
        ObservableList<stock> filteredStocks = FXCollections.observableArrayList();
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            PreparedStatement preparedStatement;
            if (selectedType.equals("All Types")) {
                preparedStatement = connection.prepareStatement("SELECT s.nom_produit, s.type, s.date, s.quantite, s.prix, IFNULL(f.email, 'pas de fournisseur') AS email FROM stock s LEFT JOIN fournisseur f ON f.id = s.fournisseur_id");
            } else {
                preparedStatement = connection.prepareStatement("SELECT s.nom_produit, s.type, s.date, s.quantite, s.prix, IFNULL(f.email, 'pas de fournisseur') AS email FROM stock s LEFT JOIN fournisseur f ON f.id = s.fournisseur_id WHERE s.type = ?");
                preparedStatement.setString(1, selectedType);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stock s = new stock(
                        resultSet.getString("nom_produit"),
                        resultSet.getString("type"),
                        resultSet.getDate("date"),
                        resultSet.getInt("quantite"),
                        resultSet.getFloat("prix"),
                        resultSet.getString("email")); // Ajout de l'email du fournisseur
                filteredStocks.add(s);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stockTable.setItems(filteredStocks);
    }





    private void populateTypeChoice() {
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT type FROM stock");
            ObservableList<String> types = FXCollections.observableArrayList();
            types.add("All Types"); // Add "All Types" option
            while (resultSet.next()) {
                types.add(resultSet.getString("type"));
            }
            typeChoice.setItems(types);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void populatefourChoice() {
        try {
            Connection connection = MyDatabase.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT email FROM fournisseur");
            ObservableList<String> emails = FXCollections.observableArrayList();

            // Add "pas de fournisseur" choice representing null values
            emails.add("pas de fournisseur");

            while (resultSet.next()) {
                emails.add(resultSet.getString("email"));
            }
            fourChoice.setItems(emails);
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void informSupplier() {
        String username = "khalilichri@gmail.com";
        String password = "suufmxngzjbbbkaf";
        String NomProduit = tnom.getText();
        String supplierEmail = fourChoice.getValue();

        if (supplierEmail == null || supplierEmail.isEmpty() || supplierEmail.equals("pas de fournisseur")) {
            showAlert("No supplier email available.", Alert.AlertType.ERROR);
            return;
        }

        String subject = "Besoin de plus de produits en stock";
        String content = "Cher fournisseur,\n\nNous tenons à vous informer que nous avons besoin de plus du produit suivant en stock : " + NomProduit + "\n\nVeuillez prendre les mesures nécessaires pour répondre à cette demande.\n\nCordialement,\nENIGMA";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(supplierEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
            showAlert("Email sent to supplier.", Alert.AlertType.INFORMATION);
        } catch (MessagingException e) {
            e.printStackTrace();
            showAlert("Failed to send email.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void refresh(ActionEvent event) {
        tnom.clear();
        tquan.clear();
        ttype.clear();
        tprix.clear();
        nomChoice.clear();

        populateTypeChoice();
        populatefourChoice();


        stockTable.getItems().clear();
        afficherStocks();
    }


    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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



    public void startAutomaticStockCheck() {
        Timer timer = new Timer();
        // Planifiez la vérification chaque minutes (millisecondes)
        timer.scheduleAtFixedRate(new StockCheckTask(), 0, 60000 );
    }

    class StockCheckTask extends TimerTask {
        @Override
        public void run() {
            for (stock product : stockTable.getItems()) {
                if (product.getQuantite() <= 50) {
                    // Si le stock est inférieur ou égal à 50, envoyez un e-mail au fournisseur
                    autoinform(product.getNom_produit(), product.getFournisseurEmail());
                }
            }
        }
    }

    private void autoinform(String NomProduit, String supplierEmail) {

        String username = "khalilichri@gmail.com";
        String password = "suufmxngzjbbbkaf";


        String subject = "Besoin de plus de produits en stock";
        String content = "Cher fournisseur,\n\nNous tenons à vous informer que nous avons besoin de plus du produit suivant en stock : " + NomProduit + "\n\nVeuillez prendre les mesures nécessaires pour répondre à cette demande.\n\nCordialement,\nENIGMA";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "*");
        properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(supplierEmail));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }    }


}