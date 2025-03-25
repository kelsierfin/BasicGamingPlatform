package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private Button backButton;

    @FXML
    private Button profileTab;

    @FXML
    private Button matchHistoryTab;

    @FXML
    private BorderPane contentArea;

    @FXML
    private void initialize() {
        // Set Profile as the default tab to load
        handleProfileTab();
    }

    @FXML
    private void handleBack() {
        PageNavigator.navigateTo("dashboard"); // Navigate back to dashboard
    }


    @FXML
    private void handleProfileTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile_settings.fxml"));
            Parent profileSettings = loader.load();
            contentArea.setCenter(profileSettings);

            // Highlight the selected tab
            profileTab.getStyleClass().add("selected-tab");
            matchHistoryTab.getStyleClass().remove("selected-tab");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMatchHistoryTab() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/match_history.fxml"));
            Parent matchHistory = loader.load();
            contentArea.setCenter(matchHistory);

            // Highlight the selected tab
            matchHistoryTab.getStyleClass().add("selected-tab");
            profileTab.getStyleClass().remove("selected-tab");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}