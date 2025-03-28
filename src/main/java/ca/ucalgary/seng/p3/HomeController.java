package ca.ucalgary.seng.p3;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import java.util.List;
public class HomeController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox menuPopup = new VBox();

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

        List<ImageView> icons = List.of(chessIcon, goIcon, tttIcon, connectIcon);

        for (ImageView icon : icons) {
            Rectangle rect = new Rectangle();
            rect.setWidth(icon.getFitWidth());
            rect.setHeight(icon.getFitHeight());
            rect.setArcWidth(20);
            rect.setArcHeight(20);

            icon.setClip(rect);
        }
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
    }

    // More event handlers for other buttons, unfinished:
    @FXML
    private void handleProfileButton() {
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
    private void handleBellButton() {
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



