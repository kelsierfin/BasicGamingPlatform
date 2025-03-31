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

    private String targetPage;

    private String gameType;


    @FXML
    private void initialize() {
        gameType = HomeController.selectedGameType;

        String imagePath = "";
        // Determine the start game screen image and the actual game page fxml
        if ("tictactoe".equalsIgnoreCase(gameType)) {
            imagePath = "/icons/tictactoe.png";
            targetPage = "tictactoe";      // Navigates to tictactoe.fxml
        } else if ("chess".equalsIgnoreCase(gameType)) {
            imagePath = "/icons/chessimage.jpg";
            targetPage = "Chess";          // Navigates to Chess.fxml
        } else if ("connect4".equalsIgnoreCase(gameType)) {
            imagePath = "/icons/connect4image.png";
            targetPage = "connect4";       // Navigates to connect4.fxml
        } else if ("go".equalsIgnoreCase(gameType)) {
            imagePath = "/icons/gogameimage.png";
            targetPage = "go";             // Navigates to go.fxml
        } else {
            // Unknown game type; do nothing, currently no error messages
            return;
        }

        // Load the appropriate image for the start game screen
        boardImage.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
    }

    @FXML
    private void handleFindMatch(ActionEvent event) {
        PageNavigator.navigateTo(targetPage);
    }
}
