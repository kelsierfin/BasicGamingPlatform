package ca.ucalgary.seng.p3;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class TicTacToeController {

    @FXML
    private Label turnLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextField chatTextField;

    @FXML
    private TextArea chatArea;

    @FXML
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;

    private String playerSymbol; // "X" or "O"

    @FXML
    private void initialize() {
        // Ensure the chat area is not editable by the user
        if (chatArea != null) {
            chatArea.setEditable(false);
        }
        // Set default text for board buttons (they remain empty)
        btn00.setText("");
        btn01.setText("");
        btn02.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("");
        btn20.setText("");
        btn21.setText("");
        btn22.setText("");

        // Randomly assign player symbol and show the popup message
        assignPlayerSymbolAndShowPopup();
    }

    // Randomly assigns player symbol and displays a temporary popup
    private void assignPlayerSymbolAndShowPopup() {
        Random random = new Random();
        boolean isPlayerX = random.nextBoolean(); // Randomly true or false

        if (isPlayerX) {
            playerSymbol = "X";
            turnLabel.setText("YOUR TURN");
        } else {
            playerSymbol = "O";
            turnLabel.setText("OPPONENT'S TURN");
        }

        String message;
        if ("X".equals(playerSymbol)) {
            message = "You have been assigned X. You go first.";
        } else {
            message = "You have been assigned O. Wait for opponent's move.";
        }

        // Create a popup stage for the assignment message
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Player Assignment");

        Label popupLabel = new Label(message);
        popupLabel.setStyle("-fx-font-size: 16; -fx-padding: 20;");

        StackPane popupRoot = new StackPane(popupLabel);
        Scene popupScene = new Scene(popupRoot, 350, 100);
        popupStage.setScene(popupScene);
        popupStage.show();

        // Close the popup automatically after 5 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(e -> popupStage.close());
        delay.play();
    }

    // Handler for the exit button
    @FXML
    private void handleExit(ActionEvent event) {
        PageNavigator.navigateTo("startgame_tictactoe");
    }

    // Handler for the send button. Appends text from the text field to the chat area.
    @FXML
    private void handleSend(ActionEvent event) {
        String message = chatTextField.getText();
        if (message != null && !message.trim().isEmpty()) {
            chatArea.appendText("Me: " + message + "\n");
            chatTextField.clear();
        }
    }

    // Handler for clicking on a cell in the tic tac toe board.
    @FXML
    private void handleCellClick(ActionEvent event) {
        // Retrieve the clicked button from the event source.
        Button clickedButton = (Button) event.getSource();

        // For testing, mark the clicked cell with the player's symbol.
        // In a complete implementation, you'll add logic to handle a move, check game status, etc.
        if (clickedButton.getText().isEmpty()) {
            clickedButton.setText(playerSymbol);
            // After the move, change turn. (In a full game, you would wait for the opponent's move.)
            if ("X".equals(playerSymbol)) {
                switchTurn(false); // After player X moves, it's opponent's turn.
            } else {
                switchTurn(false); // For player O, similar logic applies.
            }

            // Simulate opponent's move delay and switch back (for testing).
            // In a real game, this would occur after receiving the opponent's move.
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> switchTurn(true));
            pause.play();
        }
    }

    // Utility method to update the turn label.
    @FXML
    private void switchTurn(boolean isPlayerTurn) {
        if (isPlayerTurn) {
            turnLabel.setText("YOUR TURN");
        } else {
            turnLabel.setText("OPPONENT'S TURN");
        }
    }
}
