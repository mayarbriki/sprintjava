package contollers;

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
import models.reclamation;
import utils.MyDatabase;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;

public class dashboard implements Initializable {
    @FXML
    private Button ajouterrep;

    @FXML
    private Button ajouterreponse;

    @FXML
    private AnchorPane ajoutterreppage;

    @FXML
    private Button clearreponse;

    @FXML
    private TableColumn<?, ?> coldes;

    @FXML
    private TableColumn<?, ?> colid;

    @FXML
    private TableColumn<?, ?> colstat;

    @FXML
    private TableColumn<reclamation, String> coltitre;

    @FXML
    private TableColumn<reclamation, Date> daterec;

    @FXML
    private TableColumn<reclamation, String> descrec;

    @FXML
    private AnchorPane home;

    @FXML
    private TableColumn<reclamation, String> idrec;

    @FXML
    private Button modstatus;

    @FXML
    private Button nav_acc;

    @FXML
    private Button nav_rec;

    @FXML
    private Button nav_rep;

    @FXML
    private FontAwesomeIconView out;

    @FXML
    private TextField reachrec;

    @FXML
    private AnchorPane reclamationss;

    @FXML
    private TableColumn<reclamation, String> rep;

    @FXML
    private TableColumn<reclamation, String> statrec;

    @FXML
    private TableColumn<reclamation, String> titrerec;

    @FXML
    private TableView<?> viewreprec;

    @FXML
    private TableView<reclamation> rectable;
    @FXML
    private AnchorPane rep_page;
    private Connection connect;
    private Statement statement;
    private PreparedStatement prepare;
    private ResultSet result;


    public void switchForm(ActionEvent event) {

        if (event.getSource() == nav_rec) {
            reclamationss.setVisible(true);
            rep_page.setVisible(false);
            home.setVisible(false);

            nav_rec.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            nav_rep.setStyle("-fx-background-color:transparent");
            nav_acc.setStyle("-fx-background-color:transparent");

            //homeTotalEmployees();
          //  homeEmployeeTotalPresent();
          //  homeTotalInactive();
            //homeChart();

        } else if (event.getSource() == nav_rep) {
            reclamationss.setVisible(false);
            rep_page.setVisible(true);
            home.setVisible(false);

            nav_rep.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            nav_rec.setStyle("-fx-background-color:transparent");
            nav_acc.setStyle("-fx-background-color:transparent");

          //  addEmployeeGendernList();
           // addEmployeePositionList();
          //  addEmployeeSearch();//
        } else if (event.getSource() == nav_acc) {
            reclamationss.setVisible(false);
            rep_page.setVisible(false);
            home.setVisible(true);

            nav_acc.setStyle("-fx-background-color:linear-gradient(to bottom right, #3a4368, #28966c);");
            nav_rep.setStyle("-fx-background-color:transparent");
            nav_rec.setStyle("-fx-background-color:transparent");

            //salaryShowListData();

        }


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
                reclamat = new reclamation(result.getString("titre"), result.getString("status_r"), result.getString("description_r"), result.getDate("date_r"));
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


        titrerec.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        descrec.setCellValueFactory(new PropertyValueFactory<>("Description"));
        daterec.setCellValueFactory(new PropertyValueFactory<>("Date"));
        statrec.setCellValueFactory(new PropertyValueFactory<>("Status"));

        rectable.setItems(addReclamationList);

    }
    public void addReclamationSelect() {
        reclamation reclamationD = rectable.getSelectionModel().getSelectedItem();
        int num = rectable.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        titrerec.setText(String.valueOf(reclamationD.getTitre()));
        descrec.setText(reclamationD.getDescription_r());


    }


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

    public void close() {
        System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
