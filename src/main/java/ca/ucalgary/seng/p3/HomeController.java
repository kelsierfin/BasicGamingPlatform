package ca.ucalgary.seng.p3;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.swing.text.Style;
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

    @FXML
    private Button playChessButton;

    @FXML
    private Button playGoButton;

    @FXML
    private Button playTttButton;

    @FXML
    private Button playConnectButton;


    @FXML
    public void initialize() {
        btnPrev.setOnAction(event -> scrollLeft());
        btnNext.setOnAction(event -> scrollRight());
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);
        notifications.add("Welcome user!");
        notifications.add("Match invite from: ...");

        List<ImageView> icons = List.of(chessIcon, goIcon, tttIcon, connectIcon);

        for (ImageView icon : icons) {
            Rectangle rect = new Rectangle();
            rect.setWidth(icon.getFitWidth());
            rect.setHeight(icon.getFitHeight());
            rect.setArcWidth(20);
            rect.setArcHeight(20);

            icon.setClip(rect);
        }

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
    }

    @FXML
    private void handleLogOutButton() {
    }


    @FXML
    private void handleDashboardButton() {
    }

    @FXML
    private void handleLeaderboardButton() {
    }

    @FXML
    private void handleFindAPlayerButton() {
    }

    @FXML
    private void handleSettingsButton() {
    }
    @FXML
    private void handleGameRulesButton() {
    }


    @FXML
    private void handlePlayChessButton() {
        PageNavigator.navigateTo("startgame_chess");
    }

    @FXML
    private void handlePlayGoButton() {
        PageNavigator.navigateTo("startgame_go");
    }

    @FXML
    private void handlePlayTttButton() {
        PageNavigator.navigateTo("startgame_tictactoe");
    }

    @FXML
    private void handlePlayConnectButton() {
        PageNavigator.navigateTo("startgame_connect4");
    }

}



