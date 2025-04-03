module ca.ucalgary.seng.p3.client{
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;

    exports ca.ucalgary.seng.p3.client;
    opens ca.ucalgary.seng.p3.client to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.controllers;
    opens ca.ucalgary.seng.p3.client.controllers to javafx.fxml;
}
