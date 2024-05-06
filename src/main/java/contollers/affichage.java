package contollers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import models.reclamation;
import models.reponse;
import services.ServiceReclamation;
import utils.MyDatabase;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.beans.value.ObservableValue;


import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class affichage {
    @FXML
    private Label idLabel;

    @FXML
    private Label titreLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label dateLabel;
    @FXML
    private Label statuslabel;


    @FXML
    private Label reponseLabel;
    private contollers.ReclamationC ReclamationC;

    public void setReclamationC(ReclamationC reclamationc) {
        this.ReclamationC= reclamationc;
    }
    // Méthode pour récupérer et afficher les détails de la réclamation ainsi que sa réponse le cas échéant
    void afficherDetailsReclamation(int idReclamation) {
        // Récupérer les détails de la réclamation en fonction de son ID
        reclamation reclamation = getReclamationById(idReclamation);

        if (reclamation != null) {
            // Afficher les détails de la réclamation
            idLabel.setText(String.valueOf(reclamation.getId()));
            titreLabel.setText(reclamation.getTitre());
            descriptionLabel.setText(reclamation.getDescription_r());
            statuslabel.setText(reclamation.getStatus_r());
            dateLabel.setText(reclamation.getDate_r().toString());

            // Récupérer et afficher la réponse associée à la réclamation
            reponse reponse = getReponseForReclamation(idReclamation);
            if (reponse != null) {
                reponseLabel.setText(reponse.getReponse());
            } else {
                reponseLabel.setText("Aucune réponse disponible");
            }
        } else {
            // Afficher un message d'erreur si la réclamation n'est pas trouvée
            idLabel.setText("");
            titreLabel.setText("Réclamation non trouvée");
            descriptionLabel.setText("");
            statuslabel.setText("");
            dateLabel.setText("");
            reponseLabel.setText("");
        }
    }

    // Méthode pour récupérer les détails de la réclamation en fonction de son ID
    private reclamation getReclamationById(int idReclamation) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Connexion à la base de données (assurez-vous d'avoir initialisé la connexion correctement)
            connection = MyDatabase.getInstance().getConnection();
            // Requête SQL pour récupérer les détails de la réclamation
            String sql = "SELECT * FROM reclamation WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idReclamation);
            resultSet = preparedStatement.executeQuery();
            // Si une réclamation correspondante est trouvée, la retourner
            if (resultSet.next()) {
                reclamation reclamation = new reclamation();
                reclamation.setId(resultSet.getInt("id"));
                reclamation.setTitre(resultSet.getString("titre"));
                reclamation.setDescription_r(resultSet.getString("description_r"));
                reclamation.setStatus_r(resultSet.getString("status_r"));

                reclamation.setDate_r(resultSet.getDate("date_r"));
                return reclamation;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fermeture des ressources JDBC
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null; // Retourner null si aucune réclamation correspondante n'est trouvée
    }

    // Méthode pour récupérer la réponse associée à la réclamation en fonction de son ID
    private reponse getReponseForReclamation(int idlamation) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // Connexion à la base de données (assurez-vous d'avoir initialisé la connexion correctement)
            connection = MyDatabase.getInstance().getConnection();

            // Requête SQL pour récupérer la réponse associée à la réclamation
            String sql = "SELECT * FROM reponse WHERE idrec_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idlamation);
            resultSet = preparedStatement.executeQuery();
            // Si une réponse correspondante est trouvée, la retourner
            if (resultSet.next()) {
                reponse reponse = new reponse();
                reponse.setReponse(resultSet.getString("reponse"));
                return reponse;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            // Fermeture des ressources JDBC
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null; // Retourner null si aucune réponse correspondante n'est trouvée
    }

}

