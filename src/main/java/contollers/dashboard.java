package contollers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.reclamation;
import models.reponse;
import services.ServiceReclamation;
import services.ServiceReponse;
import utils.MyDatabase;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.beans.value.ObservableValue;


import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

import static javafx.application.Application.launch;

public class dashboard implements Initializable {
    @FXML
    private Button ajouterrep;
    @FXML
    private Button suprec;

    @FXML
    private Button soumettrelareponse;

    @FXML
    private AnchorPane ajoutterreppage;


    @FXML
    private TableColumn<reclamation, Date> daterec;

    @FXML
    private TableColumn<reclamation, String> descrec;
    @FXML
    private TableColumn<reclamation, Date> daterec1;

    @FXML
    private TableColumn<reclamation, String> descrec1;

    @FXML
    private AnchorPane home;

    @FXML
    private TableColumn<reclamation, String> idrec;
    @FXML
    private TableColumn<reclamation, String> idrec1;

    @FXML
    private Button modstatus;

    @FXML
    private Button nav_acc;
    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Button nav_rec;

    @FXML
    private Button nav_rep;
    @FXML
    private Button supprep;
    @FXML
    private Button modifreponse;

    @FXML
    private FontAwesomeIconView out;

    @FXML
    private TextField reachrec;
    @FXML
    private TextField reptextfield;
    @FXML
    private TextField casemodif;

    @FXML
    private TextField stattext;
    @FXML
    private TextField reachrec1;

    @FXML
    private AnchorPane reclamationss;



    @FXML
    private TableColumn<reclamation, String> statrec;

    @FXML
    private TableColumn<reclamation, String> titrerec;
    @FXML
    private TableColumn<reclamation, String> statrec1;

    @FXML
    private TableColumn<reclamation, String> titrerec1;
    @FXML
    private TableColumn<reponse, String>idreponse;
    @FXML
    private TableColumn<reponse, String>idreclamation;
    @FXML
    private TableColumn<reponse, String> reponserep;

    @FXML
    private TableView<reclamation> viewrec;
    @FXML
    private TableView<reponse> reponsetable;
    @FXML
    private AnchorPane pagemodif;

    @FXML
    private TableView<reclamation> rectable;
    @FXML
    private AnchorPane rep_page;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;
    private ReclamationC ReclamationC;

    @FXML
    private Label idsat;
    @FXML
    private Label titstat;
    @FXML
    private Label desstat;
    @FXML
    private Label datestat;
    @FXML
    private TableColumn<reclamation, String> coltitrep;
    Connection cnx;


    public void switchForm(ActionEvent event) {

        if (event.getSource() == nav_rec) {
            reclamationss.setVisible(true);
            rep_page.setVisible(false);
            home.setVisible(false);
            ajoutterreppage.setVisible(false);


            nav_rec.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            nav_rep.setStyle("-fx-background-color:transparent");
            nav_acc.setStyle("-fx-background-color:transparent");



        } else if (event.getSource() == nav_rep) {
            reclamationss.setVisible(false);
            rep_page.setVisible(true);
            home.setVisible(false);
            ajoutterreppage.setVisible(false);


            nav_rep.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            nav_rec.setStyle("-fx-background-color:transparent");
            nav_acc.setStyle("-fx-background-color:transparent");


        } else if (event.getSource() == nav_acc) {
            reclamationss.setVisible(false);
            rep_page.setVisible(false);
            home.setVisible(true);
            ajoutterreppage.setVisible(false);


            nav_acc.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            nav_rep.setStyle("-fx-background-color:transparent");
            nav_rec.setStyle("-fx-background-color:transparent");

        }else if (event.getSource() == ajouterrep) {
                reclamationss.setVisible(false);
                rep_page.setVisible(false);
                home.setVisible(false);
                ajoutterreppage.setVisible(true);




        }}
    @FXML
    private Label nonreponse_count;
    @FXML
    private Label reponse_count;
    @FXML
    private Label reclamation_count;
    @FXML
    private BarChart<String, Integer> reclamationbydate;
/////page acceuilll

    private void displayReclamationCount() {
        try {
            ServiceReclamation serviceTransport = new ServiceReclamation();
            int reclamationCount = serviceTransport.countreclamation();
            reclamation_count.setText("" + reclamationCount);
        } catch (SQLException e) {
            e.printStackTrace();
            reclamation_count.setText("0");
        }
    }
    private void displayNonreponse() {
        try {
            ServiceReponse ServiceReponse = new ServiceReponse();
            int nonreponse = ServiceReponse.nonreponse();
            nonreponse_count.setText("" + nonreponse);
        } catch (SQLException e) {
            e.printStackTrace();
            nonreponse_count.setText("0");
        }
    }
    private void displayreponseCount() {
        try {
            ServiceReponse ServiceReponse = new ServiceReponse();
            int reponseCount = ServiceReponse.countreponse();
            reponse_count.setText("" + reponseCount);
        } catch (SQLException e) {
            e.printStackTrace();
            reponse_count.setText("0");
        }
    }
    @FXML
    private AnchorPane mainchart;
    private void populateReclamationByDateChart(XYChart<String, Integer> reclamationByDateChart) {
        try {
            ServiceReclamation serviceReclamation = new ServiceReclamation();
            Map<String, Integer> reclamationByDate = serviceReclamation.countreclamationByDate();

            XYChart.Series<String, Integer> seriesByDate = new XYChart.Series<>();
            seriesByDate.setName("Par date");
            for (Map.Entry<String, Integer> entry : reclamationByDate.entrySet()) {
                seriesByDate.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // Ajouter la série de données à la charte
            reclamationByDateChart.getData().add(seriesByDate);
            reclamationByDateChart.setLegendVisible(true);

            // Appliquer le style à la série
            for (XYChart.Data<String, Integer> data : seriesByDate.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: #FF0000;"); // Rouge pour la série par date
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateReclamationByStatusChart(XYChart<String, Integer> reclamationByStatusChart) {
        try {
            ServiceReclamation serviceReclamation = new ServiceReclamation();
            Map<String, Integer> reclamationByStatus = serviceReclamation.countReclamationByStatus();

            XYChart.Series<String, Integer> seriesByStatus = new XYChart.Series<>();
            seriesByStatus.setName("Par statut");
            for (Map.Entry<String, Integer> entry : reclamationByStatus.entrySet()) {
                seriesByStatus.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            // Ajouter la série de données à la charte
            reclamationByStatusChart.getData().add(seriesByStatus);
            reclamationByStatusChart.setLegendVisible(true);

            // Appliquer le style à la série
            for (XYChart.Data<String, Integer> data : seriesByStatus.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    node.setStyle("-fx-bar-fill: #0000FF;"); // Bleu pour la série par statut
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setReclamationC(ReclamationC reclamationController) {
        this.ReclamationC= reclamationController;
    }

    public ObservableList<reclamation> addReclamationListData() {

        ObservableList<reclamation> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reclamation";

        connect = MyDatabase.getInstance().getConnection();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            reclamation reclamat;

            while (result.next()) {
                reclamat = new reclamation(result.getInt("id"),result.getString("titre"), result.getString("description_r"), result.getString("status_r"),result.getDate("date_r"));
                listData.add(reclamat);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<reclamation> addReclamationList;

    public  void addReclamationShowListData() {
        addReclamationList = addReclamationListData();
        rectable.setItems(addReclamationList);

        idrec.setCellValueFactory(new PropertyValueFactory<>("id"));
        titrerec.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descrec.setCellValueFactory(new PropertyValueFactory<>("description_r"));
        daterec.setCellValueFactory(new PropertyValueFactory<>("date_r"));
        statrec.setCellValueFactory(new PropertyValueFactory<>("status_r"));



    }


    public void addReclamationSearch() {
// Créez votre FilteredList
        FilteredList<reclamation> addReclamationList = new FilteredList<>(rectable.getItems(), p -> true);
        rectable.setItems(addReclamationList);

// Ajoutez le listener pour filtrer la liste en fonction du texte entré
        reachrec.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            addReclamationList.setPredicate(reclamation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Si le texte est vide, afficher tous les éléments
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Appliquer votre logique de filtrage ici
                return reclamation.getTitre().toLowerCase().contains(lowerCaseFilter)
                        || reclamation.getDescription_r().toLowerCase().contains(lowerCaseFilter)
                        || reclamation.getDate_r().toString().toLowerCase().contains(lowerCaseFilter)
                        || reclamation.getStatus_r().toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(reclamation.getId()).toLowerCase().contains(lowerCaseFilter);
            });
            // Mettez à jour l'interface utilisateur sur le thread JavaFX
            Platform.runLater(() -> {
                // Insérez ici le code pour mettre à jour votre interface utilisateur, si nécessaire
            });
        });}

    // Méthode pour supprimer une réclamation en fonction de son ID
    public void deleteReclamation(ActionEvent event) {
        String sql = "DELETE FROM reclamation WHERE id = ?";
        connect = MyDatabase.getInstance().getConnection();
        reclamation selectedReclamation = rectable.getSelectionModel().getSelectedItem();

        try {
            int reclamationId = selectedReclamation.getId();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, reclamationId );
            prepare.executeUpdate();

            addReclamationShowListData();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String[] listStatus = {"En cours", "Terminé"};

    public void statusList() {
        List<String> listG = new ArrayList<>();

        for (String data : listStatus) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);
        statusComboBox.setItems(listData);
    }
    @FXML
    public void statusUpdate() {

        String newStatus = statusComboBox.getSelectionModel().getSelectedItem();

        // Vérifiez si le champ stattext est vide ou null
        if (newStatus == null || newStatus.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a status before updating.");
            alert.showAndWait();
            return; // Sortez de la méthode si le champ stattext est vide ou null
        }

        String sql = "UPDATE reclamation SET status_r = ? WHERE id = ?";
        Connection connect = null;
        PreparedStatement prepare = null;

        try {

            reclamation selectedReclamation = rectable.getSelectionModel().getSelectedItem();





            connect = MyDatabase.getInstance().getConnection();
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, newStatus);
            prepare.setInt(2, selectedReclamation.getId());
            int rowsAffected = prepare.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Réclamation mise à jour avec succès!");

                alert.showAndWait();

                addReclamationShowListData();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Échec de la mise à jour de la réclamation!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while updating: " + e.getMessage());
            alert.showAndWait();


    }}




    // public static void main(String[] args) {
    //    launch(args);
  //  }


  //  public void start(Stage primaryStage) {
    //    try {
      //      FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        //    Parent root = loader.load();
          //  Scene scene = new Scene(root);

            //primaryStage.setScene(scene);
            //primaryStage.show();
        //} catch (IOException e) {
          //  System.err.println(e.getMessage());
        //}
    //}





    private boolean reponseExistsForReclamation(int idReclamation) {
        String sql = "SELECT COUNT(*) FROM reponse WHERE idrec_id = ?";
        try {
            connect = MyDatabase.getInstance().getConnection();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, idReclamation);
            ResultSet resultSet = prepare.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Retourne true si une réponse existe pour la réclamation, sinon false
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } return false;}
    @FXML
    private void ajouterReponseAReclamation(ActionEvent event) {
        // Vérifier si le champ de texte pour la réponse est vide
        if (reptextfield.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            return; // Sortir de la méthode si le champ est vide
        }
        // Récupérer la réclamation sélectionnée
        reclamation selectedReclamation = viewrec.getSelectionModel().getSelectedItem();
        int idReclamation=selectedReclamation.getId();

        if (reponseExistsForReclamation(selectedReclamation.getId())) {
            // Afficher un message pour informer l'utilisateur qu'une réponse existe déjà pour cette réclamation
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("A response already exists for this reclamation");
            alert.showAndWait();
            return;
        }

        // Récupérer le contenu de la réponse
        String reponse = reptextfield.getText(); // Supposons que vous avez un champ de texte pour le contenu de la réponse
        String sql = "INSERT INTO reponse (idrec_id, reponse) VALUES (?, ?)";
        try {
            connect = MyDatabase.getInstance().getConnection();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, idReclamation); // Utiliser l'ID de la réclamation
            prepare.setString(2, reptextfield.getText()); // Contenu de la réponse
            prepare.executeUpdate();

            // Envoi du SMS
            SendSMS smsSender = new SendSMS();
            String smsResponse = smsSender.sendSms(String.valueOf(idReclamation));

            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added!");
            alert.showAndWait();
            ShowListDataREP();

            // Effacer le champ de texte pour la réponse
            reptextfield.clear();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void addReclamationShowListDataREP() {
        addReclamationList = addReclamationListData();
        viewrec.setItems(addReclamationList);

        idrec1.setCellValueFactory(new PropertyValueFactory<>("id"));
        titrerec1.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descrec1.setCellValueFactory(new PropertyValueFactory<>("description_r"));
        daterec1.setCellValueFactory(new PropertyValueFactory<>("date_r"));
        statrec1.setCellValueFactory(new PropertyValueFactory<>("status_r"));

    }

////table view reponse
    public ObservableList<reponse> ReponseListData() {

        ObservableList<reponse> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM reponse";

        connect = MyDatabase.getInstance().getConnection();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            reponse repon;

            while (result.next()) {
                repon = new reponse(result.getInt("id"),result.getString("reponse"),result.getInt("idrec_id"));
                listData.add(repon);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<reponse> ReponseList;
    public  void ShowListDataREP() {
        ReponseList = ReponseListData();
        reponsetable.setItems(ReponseList);

        idreponse.setCellValueFactory(new PropertyValueFactory<>("id"));
        idreclamation.setCellValueFactory(new PropertyValueFactory<>("idrec_id"));
        reponserep.setCellValueFactory(new PropertyValueFactory<>("reponse"));

    }
    @FXML
    public void deleteReponse(ActionEvent event) {
        String sql = "DELETE FROM reponse WHERE id = ?";
        connect = MyDatabase.getInstance().getConnection();
        reponse selectedReponse = reponsetable.getSelectionModel().getSelectedItem();

        try {
            int repId = selectedReponse.getId();
            prepare = connect.prepareStatement(sql);
            prepare.setInt(1, repId );
            prepare.executeUpdate();

            ShowListDataREP();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void reponseUpdate() {

        String newrep = casemodif.getText();

        // Vérifiez si le champ stattext est vide ou null
        if (newrep == null || newrep.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a reponse before updating.");
            alert.showAndWait();
            return; // Sortez de la méthode si le champ stattext est vide ou null
        }

        String sql = "UPDATE reponse SET reponse = ? WHERE id = ?";
        Connection connect = null;
        PreparedStatement prepare = null;

        try {

            reponse selectedReponse = reponsetable.getSelectionModel().getSelectedItem();





            connect = MyDatabase.getInstance().getConnection();
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, newrep);
            prepare.setInt(2, selectedReponse.getId());
            int rowsAffected = prepare.executeUpdate();

            if (rowsAffected > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Réponse mise à jour avec succès!");

                alert.showAndWait();

                ShowListDataREP();

            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Échec de la mise à jour de la réponse!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Error occurred while updating: " + e.getMessage());
            alert.showAndWait();


        }}
    @FXML
    public void ReponseSearch() {
// Créez votre FilteredList
        FilteredList<reponse> ReponseList = new FilteredList<>(reponsetable.getItems(), p -> true);
        reponsetable.setItems(ReponseList);

// Ajoutez le listener pour filtrer la liste en fonction du texte entré
        reachrec1.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            ReponseList.setPredicate(reponse -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Si le texte est vide, afficher tous les éléments
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Appliquer votre logique de filtrage ici
                return reponse.getReponse().toLowerCase().contains(lowerCaseFilter)

                        || String.valueOf(reponse.getId()).toLowerCase().contains(lowerCaseFilter)
                        || String.valueOf(reponse.getIdrec_id()).toLowerCase().contains(lowerCaseFilter);
            });
            // Mettez à jour l'interface utilisateur sur le thread JavaFX
            Platform.runLater(() -> {
                // Insérez ici le code pour mettre à jour votre interface utilisateur, si nécessaire
            });
        });}



    public void close() {
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
// Initialise les données dans la TableView au chargement de la scène
        addReclamationShowListData();
        addReclamationShowListDataREP();
        ShowListDataREP();
        ReponseSearch();

        statusList();
        addReclamationSearch();
        displayReclamationCount();
        displayNonreponse();
        displayreponseCount();
        populateReclamationByDateChart( reclamationbydate);
        populateReclamationByStatusChart( reclamationbydate);

    }}
