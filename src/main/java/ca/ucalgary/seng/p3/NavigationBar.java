package ca.ucalgary.seng.p3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NavigationBar extends AnchorPane {

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    public String getTitle() {
        return titleLabel.getText();
    }
    public void setUsername(String username) {
        this.username.setText(username);
    }
    public String getUsername() {
        return username.getText();
    }

    public void setGameType(String gameType) {
        this.gameType.set(gameType);
    }

    public String getGameType() {
        return gameType.get();
    }

    @FXML
    private Button menuButton;
    @FXML
    private Button backButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Button profileButton;
    @FXML
    private Button bellButton;
    @FXML
    private Circle notificationDot;
    @FXML
    private Button editProfileButton;
    @FXML
    private Button logOutButton;
    @FXML
    private VBox menuPopup;
    @FXML
    private VBox profilePopup;
    @FXML
    private VBox notificationPopup;
    @FXML
    private Label username;
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

    private final StringProperty gameType= new SimpleStringProperty();
    private final List<String> notifications = new ArrayList<>();

    public NavigationBar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LeaderBoardController.class.getResource("/navigationbar.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {

        }
        notifications.add("Welcome user!");
        notifications.add("Match invite from: ...");
        menuButton.setOnAction(e -> menuPopup.setVisible(!menuPopup.isVisible()));
        profileButton.setOnAction(e -> profilePopup.setVisible(!profilePopup.isVisible()));
        bellButton.setOnAction(e -> handleBellButton());
        logOutButton.setOnAction(e -> handleLogoutButton());
        editProfileButton.setOnAction(e -> handleEditProfileButton());
        dashboardButton.setOnAction(e -> handleDashboardButton());
        settingsButton.setOnAction(e->handleSettingsButton());
    }

    private void handleSettingsButton() {
        PageNavigator.navigateTo("settings");
    }

    private void handleDashboardButton() {
        PageNavigator.navigateTo("home");
    }

    private void handleEditProfileButton() {
        PageNavigator.navigateTo("settings");
    }

    private void handleLogoutButton() {
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

        notifications.clear();
    }

    private void updateNotificationDot() {
        notificationDot.setVisible(!notifications.isEmpty());
    }
}
