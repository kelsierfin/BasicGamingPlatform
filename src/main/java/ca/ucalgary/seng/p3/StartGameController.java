package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StartGameController {

    @FXML
    private Button findMatchButton;

    @FXML
    private ImageView boardImage;
    @FXML
    private NavigationBar navBar;

    @FXML
    private void initialize() {

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
        findMatchButton.setOnAction(e -> handleFindMatch());
    }

    // Method to handle page navigation and load the appropriate image
    public void handleGamePage(String gameType) {
        loadGameImage(gameType);  // Load the appropriate image for the game
    }

    // More event handlers for other buttons, unfinished:
    private void handleFindMatch() {

    }
}
