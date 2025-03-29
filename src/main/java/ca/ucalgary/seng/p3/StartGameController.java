package ca.ucalgary.seng.p3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class StartGameController {

    @FXML
    private Label titleLabel;

    @FXML
    private VBox menuPopup;

    @FXML
    private Button menuButton;

    @FXML
    private Button profileButton;

    @FXML
    private Button findMatchButton;

    @FXML
    private Button dashboardButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button findAPlayerButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button gameRulesButton;

    @FXML
    private Button bellButton;

    @FXML
    private ImageView boardImage;

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

    // Method to load the appropriate image based on the game
    private void loadGameImage(String gameType) {
        String imagePath = "";

        // Set the image path based on the current game type
        if ("tictactoe".equals(gameType)) {
            imagePath = "/icons/tictactoeimage.png";  // Path for Tic Tac Toe image
        } else if ("go".equals(gameType)) {
            imagePath = "/icons/goimage.png";  // Path for Go image
        } else if ("chess".equals(gameType)) {
            imagePath = "/icons/chessimage.png";  // Path for Chess image
        } else if ("connect4".equals(gameType)) {
            imagePath = "/icons/connect4image.png";  // Path for Connect 4 image
        }

        // Load the image and set it to the ImageView
        Image gameImage = new Image(getClass().getResource(imagePath).toExternalForm());
        boardImage.setImage(gameImage);
    }

    // Method to handle page navigation and load the appropriate image
    public void handleGamePage(String gameType) {
        loadGameImage(gameType);  // Load the appropriate image for the game
    }

    // More event handlers for other buttons, add the logic and page navigation later
    @FXML
    private void handleProfileButton(ActionEvent event) {
    }

    @FXML
    private void handleFindMatch(ActionEvent event) {
    }

    @FXML
    private void handleDashboardButton(ActionEvent event) {
    }

    @FXML
    private void handleLeaderboardButton(ActionEvent event) {
    }

    @FXML
    private void handleFindAPlayerButton(ActionEvent event) {
    }

    @FXML
    private void handleSettingsButton(ActionEvent event) {
    }

    @FXML
    private void handleGameRulesButton(ActionEvent event) {
    }

    @FXML
    private void handleBellButton(ActionEvent event) {
    }
}
