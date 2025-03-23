package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartGameController {

    @FXML
    private VBox menuPopup;

    @FXML
    private Button menuButton;

    @FXML
    private void initialize() {
        menuPopup.setVisible(false);
    }

    // Called when the menu button is clicked
    @FXML
    private void handleMenuButton() {
        // Toggle visibility of the popup
        menuPopup.setVisible(!menuPopup.isVisible());
    }

    // More event handlers for other buttons, unfinished:
    @FXML
    private void handleProfileButton() {
    }

    @FXML
    private void handleFindMatch() {
    }

    @FXML
    private void handleDashboardButton() {
    }

    @FXML
    private void handleLeaderboardButton() {
    }

    @FXML
    private void handleFriendsListButton() {
    }

    @FXML
    private void handleGameRequestsButton() {
    }

    @FXML
    private void handleGameRulesButton() {
    }
}
