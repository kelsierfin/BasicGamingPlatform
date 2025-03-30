package ca.ucalgary.seng.p3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class StartGameController {

    @FXML
    private Label titleLabel;

    @FXML
    private VBox menuPopup;

    @FXML
    private VBox notificationPopup;

    @FXML
    private VBox profilePopup;
    @FXML
    private Button profileButton, bellButton;


    @FXML
    private Button menuButton;


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
    private ImageView boardImage;

    @FXML
    private Circle notificationDot;

    private List<String> notifications = new ArrayList<>();

    @FXML
    private void initialize() {
        menuPopup.setVisible(false);
        notificationPopup.setVisible(false);
        profilePopup.setVisible(false);
    }

    // Called when the menu button is clicked
    @FXML
    private void handleMenuButton() {
        // Toggle visibility of the popup
        menuPopup.setVisible(!menuPopup.isVisible());
        if(notificationPopup.isVisible()) {
            notificationPopup.setVisible(false);
        }
        if(profilePopup.isVisible()) {
            profilePopup.setVisible(false);
        }
    }

    // More event handlers for other buttons, unfinished:
    @FXML
    private void handleProfileButton() {
        // Toggle visibility of the popup
        profilePopup.setVisible(!profilePopup.isVisible());
        if(notificationPopup.isVisible()) {
            notificationPopup.setVisible(false);
        }
        if(menuPopup.isVisible()) {
            menuPopup.setVisible(false);
        }
    }

    @FXML
    private void handleBellButton() {
        // Toggle visibility of the popup
        updateNotificationDot();

        for(String notification: notifications) {
            Button btn = new Button(notification);
            btn.getStyleClass().add("notification-button");
            notificationPopup.getChildren().add(btn);
        }

        notificationPopup.setVisible(!notificationPopup.isVisible());
        if (profilePopup.isVisible()) {
            profilePopup.setVisible(false);
        }
        if (menuPopup.isVisible()) {
            menuPopup.setVisible(false);
        }
        notifications = new ArrayList<>();
    }

    private void updateNotificationDot() {
        notificationDot.setVisible(!notifications.isEmpty());
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
    // Called when the menu button is clicked

    @FXML
    private void handleEditProfileButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void handleLogOutButton() {
        Alert logOutVerification = new Alert(Alert.AlertType.CONFIRMATION);
        logOutVerification.setTitle("Log out");
        logOutVerification.setHeaderText("Are you sure you want to log out?");
        ButtonType logOutButton = new ButtonType("Log Out");
        ButtonType cancelButton = ButtonType.CANCEL;

        logOutVerification.getButtonTypes().setAll(cancelButton, logOutButton);
        logOutVerification.showAndWait().ifPresent(response -> {
            if (response == logOutButton) {
                // Perform the log-out action here
                PageNavigator.navigateTo("landing");
                logOutVerification.close();
            }else{
                logOutVerification.close();
            }
        });
    }

    @FXML
    private void handleDashboardButton() {
        PageNavigator.navigateTo("home");
    }

    @FXML
    private void handleLeaderboardButton() {
        //main leaderboard
    }

    @FXML
    private void handleFindAPlayerButton() {
        //player look up page
    }

    @FXML
    private void handleSettingsButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void handleGameRulesButton() {
        //game rules page
    }

    public void handleFindMatch() {
    }
}
