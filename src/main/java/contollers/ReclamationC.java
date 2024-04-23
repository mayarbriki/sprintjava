package contollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;



public class ReclamationC {
    @FXML
    private Button annulerButton;

    public void annulerButtonOneAction(ActionEvent event){
        Stage stage= (Stage) annulerButton.getScene().getWindow();
        stage.close();
    }




}
