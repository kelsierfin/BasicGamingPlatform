package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox menuPopup = new VBox();
    @FXML
    private VBox profilePopup = new VBox();
    @FXML
    private VBox notificationPopup = new VBox();

    @FXML
    private Button profileButton, bellButton;

    @FXML
    private Circle notificationDot;

    private List<String> notifications = new ArrayList<>();

    @FXML
    private Button btnPrev, btnNext;

    private final double scrollStep = 0.5; // Adjust sliding speed

    @FXML
    private ImageView chessIcon;
    @FXML
    private ImageView goIcon;

    @FXML
    private ImageView tttIcon;

    @FXML
    private ImageView connectIcon;

    //PROFILE SECTION
    //TODO: Initialize with information from database from Auth & Leaderboard/Match
   @FXML
    public ImageView avatarImage;
   @FXML
   public Label username;
   @FXML
   public Label winRate;
   @FXML
   public Label gamesPlayed;
   @FXML
   public Label rank;


    // Add a static variable to store the selected game type
    public static String selectedGameType;

    @FXML
    public void initialize() {
        btnPrev.setOnAction(event -> scrollLeft());
        btnNext.setOnAction(event -> scrollRight());
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);
        username.setText(LogInController.getCurrentUsername());

        //TODO: Placeholders for now. set text information from database.
        gamesPlayed.setText("0");
        winRate.setText("0%");
        rank.setText("0");

        List<ImageView> icons = List.of(chessIcon, goIcon, tttIcon, connectIcon);

        for (ImageView icon : icons) {
            // AI Assistance Declaration: The following block of code was developed with AI(ChatGPT) guidance.
            // AI provided recommendations on using JavaFX rectangles to clip images (game thumbnails)
            // to fit within game cards on the homepage.

            Rectangle rect = new Rectangle();
            rect.setWidth(icon.getFitWidth());
            rect.setHeight(icon.getFitHeight());
            rect.setArcWidth(20);
            rect.setArcHeight(20);

            icon.setClip(rect);
        }

        Circle circle = new Circle(75, 75, 75);
        avatarImage.setClip(circle);

        updateNotificationDot();
    }

    private void scrollLeft() {
        double currentScroll = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.max(currentScroll - scrollStep, 0)); // Move left
    }

    private void scrollRight() {
        double currentScroll = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.min(currentScroll + scrollStep, 1)); // Move right
    }

    //NAVIGATION BAR FUNCTIONALITY - DON'T EDIT
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

    @FXML
    private void handleEditProfileButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void handleLogOutButton() {
       LogInController.performLogout();
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
    //END OF NAVIGATION BAR FUNCTIONALITY

    @FXML
    private void handlePlayChessButton() {
        selectedGameType = "Chess";
        PageNavigator.navigateTo("startgame_chess");
    }

    @FXML
    private void handlePlayGoButton() {
        selectedGameType = "go";
        PageNavigator.navigateTo("startgame_go");
    }

    @FXML
    private void handlePlayTttButton() {
        selectedGameType = "tictactoe";
        PageNavigator.navigateTo("startgame_tictactoe");
    }

    @FXML
    private void handlePlayConnectButton() {
        selectedGameType = "connect4";
        PageNavigator.navigateTo("startgame_connect4");
    }

    @FXML
    private void handleEditAvatar(){
        PageNavigator.navigateTo("settings");
    }

    @FXML void handleUsernameClick(){
        PageNavigator.navigateTo("settings");
    }
}

