package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;


public class LeaderBoardHome {

    private static final String CHESS_TITLE = "Chess";
    private static final String CHESS_BCKGRND = "/icons/chess-background.png";
    private static final String GO_TITLE = "Go";
    private static final String GO_BCKGRND = "/icons/go-background.png";
    private static final String CONNECT_TITLE = "Connect4";
    private static final String CONNECT_BCKGRND = "/icons/connect4-background.png";
    private static final String TTT_TITLE = "TicTacToe";
    private static final String TTT_BCKGRND = "/icons/tictactoe-background.png";

    @FXML
    public AnchorPane root;

    //NAVIGATION BAR ELEMENTS - DONT EDIT
    @FXML
    private Circle notificationDot;

    private List<String> notifications = new ArrayList<>();
    @FXML
    private VBox menuPopup = new VBox();
    @FXML
    private VBox profilePopup = new VBox();
    @FXML
    private VBox notificationPopup = new VBox();

    @FXML
    private Button profileButton, bellButton;


    @FXML
    public void initialize() {
        root.setBackground(new Background(new BackgroundImage
                (new Image(getClass().getResourceAsStream("/icons/tictactoe-background.png")),
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,null,
                        new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true, true, false,true))));
    //NAVIGATION BAR
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);
    }

    @FXML
    private void chess() {

        toChessLeaderBoard();
    }

    @FXML
    private void go() {
        toGoLeaderBoard();
    }

    @FXML
    private void tictactoe() {
        toTTTLeaderBoard();
    }

    @FXML
    private void connect4() {
        toConnect4LeaderBoard();
    }

    public static void toChessLeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(CHESS_TITLE, CHESS_BCKGRND), CHESS_TITLE);
    }

    public static void toGoLeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(GO_TITLE, GO_BCKGRND), GO_TITLE);
    }

    public static void toTTTLeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(TTT_TITLE, TTT_BCKGRND), TTT_TITLE);
    }

    public static void toConnect4LeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(CONNECT_TITLE, CONNECT_BCKGRND), CONNECT_TITLE);
    }

    //NAVIGATION BAR FUNCTIONALITY - DON'T EDIT FCK WHO WRITE THIS
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
}
