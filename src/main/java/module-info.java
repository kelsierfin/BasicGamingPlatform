module ca.ucalgary.seng.p3.client{
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires com.google.gson;
    requires org.json;
    requires org.junit.jupiter.api;

    exports ca.ucalgary.seng.p3.client;
    opens ca.ucalgary.seng.p3.network to com.google.gson;
    opens ca.ucalgary.seng.p3.client to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.controllers;
    opens ca.ucalgary.seng.p3.client.controllers to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.gamelogic;
    opens ca.ucalgary.seng.p3.client.gamelogic to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.game_to_gui_commands.chess to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.game_to_gui_commands.connect4 to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.game_to_gui_commands.go to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.game_to_gui_commands.tictactoe to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.gamelogic.chess to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.gamelogic.connect4 to javafx.fxml;
    exports ca.ucalgary.seng.p3.client.gamelogic.tictactoe to javafx.fxml;
}
