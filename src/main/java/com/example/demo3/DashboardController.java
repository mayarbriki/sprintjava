package com.example.demo3;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Livraison;
import models.Transport;
import services.ServiceLivraison;
import services.ServicePDF;
import services.ServiceTransport;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController extends Application implements Initializable {

    @FXML
    public AnchorPane main_form;
    @FXML
    private AnchorPane Livraison_form;

    @FXML
    private Button acceuil_btn;

    @FXML
    private Label adresseLiv;

    @FXML
    private Button affecter;

    @FXML
    private Button close;

//    @FXML
//    private Label commande;

    @FXML
    private Label dateLiv;

    @FXML
    private Label description;

    @FXML
    private Label etat;

    @FXML
    private Button gestionlivraison_btn;

    @FXML
    private Button gestiontransport_btn;

    @FXML
    private Label id_livraison;

    @FXML
    private TableColumn<Livraison, String> livraison_col_AdresseLiv;

//    @FXML
//    private TableColumn<Livraison, Livraison> livraison_col_Commande;

    @FXML
    private TableColumn<Livraison, String> livraison_col_DateLiv;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Description;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Etat;

    @FXML
    private TableColumn<Livraison, String> livraison_col_ID;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Matricule;

    @FXML
    private TableColumn<Livraison, String> livraison_col_Nom;

    @FXML
    private TableView<Livraison> livraison_tableview;

    @FXML
    private Button logout;

    @FXML
    private ComboBox<String> matricule;

    @FXML
    private FontAwesomeIconView minimize;

    @FXML
    private Label name;

    @FXML
    private Button transport_ajouter_btn;

    @FXML
    private Button transport_clear_btn;

    @FXML
    private TableColumn<Transport, String> transport_col_ID;

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
    private DatePicker transport_date;

    @FXML
    private Button transport_demontrer_btn;

    @FXML
    private ComboBox<String> transport_etat;

    @FXML
    private AnchorPane transport_form;

    @FXML
    private ComboBox<String> transport_marque;

    @FXML
    private TextField transport_matricule;

    @FXML
    private Button transport_modifier_btn;

    @FXML
    private TextField transport_search;

    @FXML
    private Button transport_supprimer_btn;

    @FXML
    private TableView<Transport> transport_tableview;

    @FXML
    private ComboBox<String> transport_type;

    @FXML
    private Label matriculeErrorLabel;
    @FXML
    private Button maximizeButton;

    @FXML
    private Button imprimer_PDF;
    private final boolean maximized = false;

    private FilteredList<Livraison> filteredLivraisonData;
    private FilteredList<Transport> filteredTransportData;

    public static final String ACCOUNT_SID = "ACabe5d1eed1c6f8de1ab6c80123cee667";
    public static final String AUTH_TOKEN = "b010f739d3b37887038eb44b99f5464e";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    private ObservableList<Transport> currentPagePosts;
    private static final int ROWS_PER_PAGE = 7;

    public final ServiceTransport serviceTransport = new ServiceTransport();
    @FXML
    private Pagination pagination;
    private List<Transport> recupererT;




    public void ajouterT(ActionEvent event) throws SQLException {

        String selectedType = transport_type.getSelectionModel().getSelectedItem();
        String selectedMarque = transport_marque.getSelectionModel().getSelectedItem();
        String selectedEtat = transport_etat.getSelectionModel().getSelectedItem();
        String matriculeValue = transport_matricule.getText();
        LocalDate selectedDate = transport_date.getValue();

        if (selectedType == null || selectedMarque == null || selectedEtat == null || matriculeValue.isEmpty() || selectedDate == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs vides");
            alert.showAndWait();
            return;
        }

        if (!matriculeValue.matches("\\d{3}-TUN-\\d{4}")) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Matricule doit être dans le format ***-TUN-****");
            alert.showAndWait();
            return;
        }

        Transport transport = new Transport(0, selectedType, selectedMarque, matriculeValue, selectedEtat, Date.valueOf(selectedDate));

        ServiceTransport st = new ServiceTransport();

        if (st.transportExists(transport)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Transport déjà existant avec les mêmes détails.");
            alert.showAndWait();
            return;
        }

        try {
            st.ajouterT(transport);
            refreshTable();
            System.out.println("Transport ajouté avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Échec de l'ajout du transport: " + e.getMessage());
        }
    }

    @FXML
    public void modifierT() {
        Transport selectedTransport = transport_tableview.getSelectionModel().getSelectedItem();
        if (selectedTransport != null) {

            String selectedType = transport_type.getSelectionModel().getSelectedItem();
            String selectedMarque = transport_marque.getSelectionModel().getSelectedItem();
            String selectedEtat = transport_etat.getSelectionModel().getSelectedItem();
            String matriculeValue = transport_matricule.getText();
            LocalDate selectedDate = transport_date.getValue();

            if (selectedType == null || selectedMarque == null || selectedEtat == null || matriculeValue.isEmpty() || selectedDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs vides");
                alert.showAndWait();
                return;
            }

            if (!matriculeValue.matches("\\d{3}-TUN-\\d{4}")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Matricule doit être dans le format ***-TUN-****");
                alert.showAndWait();
                return;
            }

            selectedTransport.setType(selectedType);
            selectedTransport.setMarque(selectedMarque);
            selectedTransport.setMatricule(matriculeValue);
            selectedTransport.setEtat(selectedEtat);
            selectedTransport.setAnneefab(Date.valueOf(selectedDate));

            try {
                ServiceTransport st = new ServiceTransport();
                st.modifierT(selectedTransport);
                refreshTable();
                System.out.println("Transport mis à jour avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Échec de la mise à jour du transport: " + e.getMessage());
            }
        }
    }


    public void supprimerT(ActionEvent event) {

        Transport selectedTransport = transport_tableview.getSelectionModel().getSelectedItem();
        if (selectedTransport != null) {
            try {
                ServiceTransport st = new ServiceTransport();
                st.supprimerT(selectedTransport.getId());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Delete Success");
                alert.setHeaderText(null);
                alert.setContentText("Transport supprimé avec succès.");
                alert.showAndWait();
                refreshTable();
            } catch (SQLException ex) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Delete Error");
                alert.setHeaderText(null);
                alert.setContentText("Erreur lors de la suppression du transport: " + ex.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Pas de choix");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un transport à supprimer.");
            alert.showAndWait();
        }
    }

    private void setupTableColumns() throws SQLException {
        recupererT = serviceTransport.recupererT();

        // Clear existing items in the TableView
        transport_tableview.getItems().clear();
        int pageCount = (int) Math.ceil((double) recupererT.size() / ROWS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        createPage(0);

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
    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * ROWS_PER_PAGE;
        int toIndex = Math.min(fromIndex + ROWS_PER_PAGE, recupererT.size());
        currentPagePosts = FXCollections.observableArrayList(recupererT.subList(fromIndex, toIndex));

        transport_tableview.setItems(currentPagePosts);

        return new AnchorPane();
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

    private void setupLivraisonTableColumns() {
        livraison_col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        livraison_col_DateLiv.setCellValueFactory(new PropertyValueFactory<>("dateLiv"));
        livraison_col_AdresseLiv.setCellValueFactory(new PropertyValueFactory<>("adresseLiv"));
        livraison_col_Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        livraison_col_Etat.setCellValueFactory(new PropertyValueFactory<>("etat"));
//        livraison_col_Commande.setCellValueFactory(new PropertyValueFactory<>("commande"));
        livraison_col_Matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
    }

    @FXML
    private void loadDataIntoLivraisonTableView() {
        ServiceLivraison serviceLivraison = new ServiceLivraison();
        ObservableList<Livraison> livraisons = null;
        try {
            livraisons = FXCollections.observableArrayList(serviceLivraison.recupererL());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        livraison_tableview.setItems(livraisons);
    }

    private Transport getTransportByMatricule(String matricule) {
        try {
            ServiceTransport serviceTransport = new ServiceTransport();
            return serviceTransport.getTransportByMatricule(matricule);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    @FXML
    private void affecterLivraison(ActionEvent event) {
        Livraison selectedLivraison = livraison_tableview.getSelectionModel().getSelectedItem();
        String selectedMatricule = matricule.getValue();
        if (selectedLivraison != null && selectedMatricule != null) {
            Transport selectedTransport = getTransportByMatricule(selectedMatricule);
            if (selectedTransport != null) {
                selectedLivraison.setMatricule(String.valueOf(selectedTransport.getId()));

                try {
                    ServiceLivraison serviceLivraison = new ServiceLivraison();
                    serviceLivraison.modifierL(selectedLivraison);
                    System.out.println("Matricule ajouté à Livraison avec succès.");

                    refreshLivraisonTable();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Échec de l'ajout du matricule à la livraison: " + e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Le matricule sélectionné n'est associé à aucun transport.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Veuillez sélectionner une Livraison et un matricule.");
        }
        String recipientPhoneNumber = "+21641703090";
        String messageBody = "Un livreur est affecter pour votre livraison";
        sendSMS(recipientPhoneNumber, messageBody);
    }

    public static void sendSMS(String recipientPhoneNumber, String messageBody) {
        String twilioPhoneNumber = "+19706446501";

        Message message = Message.creator(
                        new PhoneNumber(recipientPhoneNumber),
                        new PhoneNumber(twilioPhoneNumber),
                        messageBody)
                .create();

        System.out.println("SMS sent successfully. SID: " + message.getSid());
    }

    @FXML
    private void refreshLivraisonTable() {
        try {
            List<Livraison> livraisonList = new ServiceLivraison().recupererL();
            ObservableList<Livraison> observableList = FXCollections.observableArrayList(livraisonList);

            livraison_tableview.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event) {
        /*if (event.getSource() == acceuil_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            addEmployee_btn.setStyle("-fx-background-color:transparent");
            salary_btn.setStyle("-fx-background-color:transparent");

            homeTotalEmployees();
            homeEmployeeTotalPresent();
            homeTotalInactive();
            homeChart();

        } else */
        if (event.getSource() == gestionlivraison_btn) {

            Livraison_form.setVisible(true);
            transport_form.setVisible(false);

            gestionlivraison_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #08b4c7 , #007fff);");
            acceuil_btn.setStyle("-fx-background-color:transparent");
            gestiontransport_btn.setStyle("-fx-background-color:transparent");

        } else if (event.getSource() == gestiontransport_btn) {
            Livraison_form.setVisible(false);
            transport_form.setVisible(true);

            gestiontransport_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #08b4c7 , #007fff);");
            gestionlivraison_btn.setStyle("-fx-background-color:transparent");
            acceuil_btn.setStyle("-fx-background-color:transparent");

        }

    }

    public void navigateToLivraisonForm(){

        Livraison_form.setVisible(true);
        transport_form.setVisible(false);

        gestionlivraison_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #08b4c7 , #007fff);");
        acceuil_btn.setStyle("-fx-background-color:transparent");
        gestiontransport_btn.setStyle("-fx-background-color:transparent");
    }

    public void navigateToTransportForm(){

        Livraison_form.setVisible(false);
        transport_form.setVisible(true);

        gestiontransport_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #08b4c7 , #007fff);");
        gestionlivraison_btn.setStyle("-fx-background-color:transparent");
        acceuil_btn.setStyle("-fx-background-color:transparent");
    }
    public void TransportReset() {

        transport_type.getSelectionModel().clearSelection();
        setComboBoxPromptText(transport_type, "Choisir");
        transport_marque.getSelectionModel().clearSelection();
        setComboBoxPromptText(transport_marque, "Choisir");
        transport_etat.getSelectionModel().clearSelection();
        setComboBoxPromptText(transport_etat, "Choisir");
        transport_matricule.setText("");
        transport_date.setValue(null);
    }

    private void setComboBoxPromptText(ComboBox<String> comboBox, String promptText) {
        comboBox.setPromptText(promptText);
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(promptText);
                } else {
                    setText(item);
                }
            }
        });
    }

    private void resetLivraisonFields() {
        id_livraison.setText("");
        dateLiv.setText("");
        adresseLiv.setText("");
        description.setText("");
        etat.setText("");
//        commande.setText("");
//        matricule.getSelectionModel().clearSelection();
    }

    private void loadDataIntoComboBox() {
        ServiceTransport sp = new ServiceTransport();
        List<String> matricules = null;
        try {
            matricules = sp.Matriculescombobox();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception
        }
        if (matricules != null) {
            ObservableList<String> matriculesList = FXCollections.observableArrayList(matricules);
            matricule.setItems(matriculesList);
        }
    }

    private void setupTransportSearch() {
        filteredTransportData = new FilteredList<>(FXCollections.observableArrayList(), p -> true);

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
    private void imprimerPDFButtonClicked(ActionEvent event) {
        Livraison selectedLivraison = livraison_tableview.getSelectionModel().getSelectedItem();
        if (selectedLivraison != null) {
            try {
                ServicePDF.generateCommandePDF("Commande.pdf", selectedLivraison);
            } catch (FileNotFoundException | MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("selectedLivraison is NULL");
        }
    }

    @FXML
    public void switchToDashboardAccueil(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardAccueil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        refreshTable();
        loadDataIntoTableView();
        pagination.setPageFactory(this::createPage);
        try {
            setupTableColumns();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setupLivraisonTableColumns();
        refreshLivraisonTable();
        loadDataIntoLivraisonTableView();

        loadDataIntoComboBox();
        setupTransportSearch();

        transport_tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Transport selectedTransport = transport_tableview.getSelectionModel().getSelectedItem();
                transport_type.setValue(selectedTransport.getType());
                transport_marque.setValue(selectedTransport.getMarque());
                transport_matricule.setText(selectedTransport.getMatricule());
                transport_etat.setValue(selectedTransport.getEtat());
                transport_date.setValue(selectedTransport.getAnneefab().toLocalDate());
            } else {
                TransportReset();
            }
        });
        livraison_tableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Livraison selectedLivraison = livraison_tableview.getSelectionModel().getSelectedItem();
                id_livraison.setText(String.valueOf(selectedLivraison.getId()));
                dateLiv.setText(String.valueOf(selectedLivraison.getDateLiv().toLocalDate()));
                adresseLiv.setText(selectedLivraison.getAdresseLiv());
                description.setText(selectedLivraison.getDescription());
                etat.setText(selectedLivraison.getEtat());
//                commande.setText(selectedLivraison.getCommande());
//                matricule.getSelectionModel().select(selectedLivraison.getMatricule());
            } else {
                resetLivraisonFields();
            }
        });

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Dashboard1.fxml")));

        stage.getIcons().add(new Image("com/example/demo3/images/MediCare (1).png"));
        stage.setTitle("ParaPharma+");

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}