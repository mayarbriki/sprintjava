module java.enigmaaa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens com.enigmaaa to javafx.fxml;
    exports com.enigmaaa;
}