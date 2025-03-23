module ca.ucalgary.seng.p3 {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports ca.ucalgary.seng.p3;
    opens ca.ucalgary.seng.p3 to javafx.fxml;
}
