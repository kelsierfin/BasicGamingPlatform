package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
    private VBox menuPopup;

    @FXML
    private void initialize() {
        // Set Profile as the default tab to load
        handleProfileTab();

        menuPopup.setVisible(false);

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
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/match_history.fxml"));
//            Parent matchHistory = loader.load();
//            contentArea.setCenter(matchHistory);
//
//            // Highlight the selected tab
//            matchHistoryTab.getStyleClass().add("selected-tab");
//            profileTab.getStyleClass().remove("selected-tab");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // Called when the menu button is clicked
    @FXML
    private void handleMenuButton() {
        // Toggle visibility of the popup
        menuPopup.setVisible(!menuPopup.isVisible());
    }

    @FXML
    private void handleDashboardButton() {
        PageNavigator.navigateTo("home");
    }

    @FXML
    private void handleLeaderboardButton() {
        PageNavigator.navigateTo("leaderboard_home");
    }

    @FXML
    private void handleFindAPlayerButton() {
        PageNavigator.navigateTo("player_Finder");
    }

    @FXML
    private void handleSettingsButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void handleGameRulesButton() {
        //game rules page
    }
}