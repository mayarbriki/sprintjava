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
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.scene.input.MouseEvent;


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


    public void soumettreButtonOneAction() {
        String titre = titreTextField.getText();
        String description = descriptionTextField.getText();

        if (titre.isEmpty() || description.isEmpty()) {
            // Afficher un message d'erreur si les champs sont vides
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs manquants");
            alert.setContentText("Veuillez remplir tous les champs avant de soumettre la réclamation.");
            alert.showAndWait();
        } else {
            reclamation nouvelleReclamation = new reclamation();
            nouvelleReclamation.setTitre(titre);
            nouvelleReclamation.setDescription_r(description);

            // Créer une instance du gestionnaire de réclamations
            ServiceReclamation gestionnaire = new ServiceReclamation();
            // Ajouter la réclamation à la base de données
            try {
                // Ajouter la réclamation à la base de données
                gestionnaire.ajouter(nouvelleReclamation);

                // Afficher un message de réussite si l'ajout est réussi
                Alert confirmationAlert = new Alert(Alert.AlertType.INFORMATION);
                confirmationAlert.setTitle("Réclamation ajoutée");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Réclamation ajoutée avec succès.");
                confirmationAlert.showAndWait();


            } catch (SQLException e) {
                // Afficher un message d'erreur si l'ajout échoue
                Alert erreurAlert = new Alert(Alert.AlertType.ERROR);
                erreurAlert.setTitle("Erreur");
                erreurAlert.setHeaderText(null);
                erreurAlert.setContentText("Une erreur est survenue lors de l'ajout de la réclamation. Veuillez réessayer.");
                erreurAlert.showAndWait();
                e.printStackTrace();
            }
        }
        }

    public void annulerButtonOneAction(ActionEvent event){
        Stage stage= (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }
    public void dash(ActionEvent event) throws IOException {
        Stage stage = (Stage) dash.getScene().getWindow();
        stage.close();

        // Chargez le fichier FXML de la nouvelle page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        Parent root = loader.load();

        // Obtenez le contrôleur de la nouvelle page
        dashboard controller = loader.getController();

        // Affichez la nouvelle page dans une nouvelle fenêtre
        Stage newStage = new Stage();
        Scene scene = new Scene(root);
        newStage.setScene(scene);



        root.setOnMousePressed((MouseEvent event1) ->{
            x = event1.getSceneX();
            y = event1.getSceneY();
        });

        root.setOnMouseDragged((MouseEvent event1) ->{
            stage.setX(event1.getScreenX() - x);
            stage.setY(event1.getScreenY() - y);
        });

        newStage.initStyle(StageStyle.TRANSPARENT);
        newStage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
