module ar.edu.unlu.poo.corazones {
    requires javafx.controls;
    requires javafx.fxml;


    opens ar.edu.unlu.poo.corazones to javafx.fxml;
    exports ar.edu.unlu.poo.corazones;
}