package contollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.reclamation;
import services.ServiceReclamation;


import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;
import utils.MyDatabase;


public class ReclamationC  implements Initializable {
    @FXML
    private Button annulerButton;

    @FXML
    private Button dash;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private AnchorPane leftt;

    @FXML
    private Button soumettreButton;

    @FXML
    private TextField titreTextField;
    private double x = 0;
    private double y = 0;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;


    private dashboard dashboard; // Référednce au contrôleur dashboard

    // Méthode pour définir le contrôleur dashboard
    public void setDashboardController(dashboard dashboardController) {
        this.dashboard= dashboardController;
    }

    public void addReclamationAdd() {

        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO reclamation (titre, description_r, date_r, status_r) VALUES (?, ?, ?, 'nouvelle')";


        connect = MyDatabase.getInstance().getConnection();

        try {
            Alert alert;
            if (titreTextField.getText().isEmpty()
                    || descriptionTextField.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();

                } else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, titreTextField.getText());
                    prepare.setString(2, descriptionTextField.getText());


                    prepare.setString(3, String.valueOf(sqlDate));

                    prepare.executeUpdate();



                   // prepare = connect.prepareStatement(insertInfo);
                    //prepare.setString(1, addEmployee_employeeID.getText());
                    //prepare.setString(2, addEmployee_firstName.getText());
                    //prepare.setString(3, addEmployee_lastName.getText());
                    //prepare.setString(4, (String) addEmployee_position.getSelectionModel().getSelectedItem());
                    //prepare.setString(5, "0.0");
                    //prepare.setString(6, String.valueOf(sqlDate));
                    //prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                titreTextField.clear();
                descriptionTextField.clear();

                    dashboard.addReclamationShowListData();

                }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void soumettreButtonOneAction() {

        }

    public void annulerButtonOneAction(ActionEvent event){
        Stage stage= (Stage) annulerButton.getScene().getWindow();
        stage.close();
        
    }
    public void dash(ActionEvent event) throws IOException {
       Stage stage = (Stage) dash.getScene().getWindow();
       stage.close();
       //Stage primaryStage = new Stage();
       //Parent root = FXMLLoader.load(getClass().getResource("/src/main/java/contollers/dashboard.fxml"));
       //primaryStage.show();


        // Chargez le fichier FXML de la nouvelle page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        Parent root = loader.load();

        // Obtenez le contrôleur de la nouvelle page
        dashboard controller = loader.getController();

        // Affichez la nouvelle page dans une nouvelle fenêtre
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);



       // root.setOnMousePressed((MouseEvent event1) ->{
         //   x = event1.getSceneX();
           // y = event1.getSceneY();
        //});

        //root.setOnMouseDragged((MouseEvent event1) ->{
          //  stage.setX(event1.getScreenX() - x);
            //stage.setY(event1.getScreenY() - y);
        //});

        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.show();
    }
    //public void dashh(Stage primaryStage) throws IOException {

               // Stage stage = (Stage) dash.getScene().getWindow();
                //stage.close();
                //FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
                //Parent root = loader.load();
                //Scene scene = new Scene(root);

                //primaryStage.setScene(scene);
                //primaryStage.show();

    //}



    public void close() {
        System.exit(0);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
