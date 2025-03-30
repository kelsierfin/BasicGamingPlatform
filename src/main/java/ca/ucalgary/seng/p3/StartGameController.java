package ca.ucalgary.seng.p3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StartGameController {

    @FXML
    private Button findMatchButton;

    @FXML
    private ImageView boardImage;

    @FXML
    private NavigationBar navBar;

    // Field to store the target fxml page for the actual game
    private String targetPage;

    @FXML
    private void initialize() {
        // Retrieve game type from the NavigationBar
        String game = navBar.getGameType();

        String imagePath = "";
        // Determine the start game screen image and the actual game page fxml
        if ("tictactoe".equalsIgnoreCase(game)) {
            imagePath = "/icons/tictactoe.png";
            targetPage = "tictactoe";      // Navigates to tictactoe.fxml
        } else if ("chess".equalsIgnoreCase(game)) {
            imagePath = "/icons/chessimage.jpg";
            targetPage = "Chess";          // Navigates to Chess.fxml
        } else if ("connect4".equalsIgnoreCase(game)) {
            imagePath = "/icons/connect4image.png";
            targetPage = "connect4";       // Navigates to connect4.fxml
        } else if ("go".equalsIgnoreCase(game)) {
            imagePath = "/icons/gogameimage.png";
            targetPage = "go";             // Navigates to go.fxml
        } else {
            // Unknown game type; do nothing, currently no error messages
            return;
        }

        // Load the appropriate image for the start game screen
        boardImage.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));

        // Set the action for the "FIND A MATCH" button using a method reference
        findMatchButton.setOnAction(this::handleFindMatch);
    }

    private void handleFindMatch(ActionEvent event) {
        // Use PageNavigator to navigate to the actual game page for now instead of matchmaking logic
        if (targetPage != null && !targetPage.isEmpty()) {
            PageNavigator.navigateTo(targetPage);
        }
    }
}
