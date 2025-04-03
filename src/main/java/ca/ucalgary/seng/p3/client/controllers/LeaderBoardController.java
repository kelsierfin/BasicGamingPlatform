package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * Example use to create TicTacToe leaderboard page (the same for other games):
 * LeaderBoardController page = new LeaderBoardController("TicTacToe", "/icons/tictactoe-background.png");
 * Scene scene = new Scene(page.root, 900, 600);
 */
public class LeaderBoardController extends AnchorPane {

    public LeaderBoardController(String label, String background){

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LeaderBoardController.class.getResource("/leaderboard.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setBackground(new Background(new BackgroundImage
                    (new Image(getClass().getResourceAsStream(background)),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,null,
                            new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true, true, false,true))));
        } catch (IOException e) {

        }
        // handle dumb navigation buttons
        profileButton.setOnAction(e -> handleProfileButton());
        bellButton.setOnAction(e -> handleBellButton());
        menuButton.setOnAction(e -> handleMenuButton());
        dashboardButton.setOnAction(e -> handleDashboardButton());
        leaderboardButton.setOnAction(e -> handleLeaderboardButton());
        findAPlayerButton.setOnAction(e -> handleFindAPlayerButton());
        settingsButton.setOnAction(e -> handleSettingsButton());
        gameRulesButton.setOnAction(e  -> handleGameRulesButton());
        editProfileButton.setOnAction(e -> handleEditProfileButton());
        logOutButton.setOnAction(e -> handleLogOutButton());
        //first medal
        rankList.add(new ImageView(new Image(getClass().getResourceAsStream("/icons/first-medal.png"))), 0,1);
        ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/icons/avatar.png")));
        rankList.add(avatar, 1,1);
        //add name, i: column 2, i1: row 1
        Label nameLabel = new Label("Pie");
        nameLabel.setStyle("-fx-font-size: 20");
        nameLabel.setTextFill(new Color(1,1,1,1));
        rankList.add(nameLabel,2,1);

        //add points
        Label pointsLabel = new Label("1000pts");
        pointsLabel.setStyle("-fx-font-size: 20" );
        pointsLabel.setTextFill(new Color(1,1,1,1));
        rankList.add(pointsLabel, 3, 1);
        //add winning rate
        Label winRateLabel = new Label("87%");
        winRateLabel.setStyle("-fx-font-size: 20");
        winRateLabel.setTextFill(new Color(1,1,1,1));
        rankList.add(winRateLabel, 4, 1);

        //set pop up
        avatar.setOnMouseClicked(e->{

            ProfilePopUp popup = new ProfilePopUp();
            popup.setTitle("Profile");
            popup.setUserName("Pie");
            popup.setBio("\"I love choco pie \"");
            popup.setAvatar("/icons/avatar.png");

            popup.setMatches("150");
            popup.setWinnings("100");
            popup.setLosses("50");
            popup.show();
        });
    }

    @FXML
    private GridPane rankList;
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
    private Button profileButton, bellButton, menuButton;
    @FXML
    private Button gameRulesButton, settingsButton, findAPlayerButton, leaderboardButton, dashboardButton;
    @FXML
    private Button logOutButton, editProfileButton;
    @FXML
    private void initialize(){

    }

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
        Alert logOutVerification = new Alert(Alert.AlertType.CONFIRMATION);
        logOutVerification.setTitle("Log out");
        logOutVerification.setHeaderText("Are you sure you want to log out?");
        ButtonType logOutButton = new ButtonType("Log Out");
        ButtonType cancelButton = ButtonType.CANCEL;

        logOutVerification.getButtonTypes().setAll(cancelButton, logOutButton);
        logOutVerification.showAndWait().ifPresent(response -> {
            if (response == logOutButton) {

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
