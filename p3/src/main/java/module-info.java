module ca.ucalgary.cpsc.p3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.ucalgary.cpsc.p3 to javafx.fxml;
    exports ca.ucalgary.cpsc.p3;
}