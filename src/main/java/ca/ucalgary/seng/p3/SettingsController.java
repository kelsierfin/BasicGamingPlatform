package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close(); // Close the Settings window; Integration team can adjust navigation
    }

    @FXML
    private void handleProfile() throws IOException {
        Stage stage = (Stage) profileButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
    }

    @FXML
    private void handleMatchHistory() {
        // Placeholder for Match History navigation
        // Matchmaking/Leaderboard team will handle this
        System.out.println("Match History button clicked");
    }

    @FXML
    private javafx.scene.control.Button backButton;

    @FXML
    private javafx.scene.control.Button profileButton;

    @FXML
    private javafx.scene.control.Button matchHistoryButton;
}