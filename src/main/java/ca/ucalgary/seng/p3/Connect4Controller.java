package ca.ucalgary.seng.p3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Connect4Controller {

    @FXML
    private Circle player1Icon;

    @FXML
    private Circle player2Icon;

    @FXML
    private Label player1Name;

    @FXML
    private Label player2Name;

    @FXML
    private Label timer;

    @FXML
    private Rectangle gameBackground;

    @FXML
    private StackPane gameBoardContainer;

    @FXML
    private Button abortGameButton;

    @FXML
    private Button newGameButton;

    @FXML
    private Button chatButton;

    // Track if this is the local player's turn
    private boolean isLocalPlayerTurn = false;
    private Timeline gameTimer;
    private int secondsRemaining = 600; // 10 minutes initial time

    @FXML
    private void initialize() {
        System.out.println("Connect4 game screen initialized");

        // Set default values
        player1Name.setText("Player 1");
        player2Name.setText("Player 2");
        startTimer();
    }

    /**
     * This method will be called by the matchmaking team when a match is found
     * @param localPlayerName The local player's username
     * @param remotePlayerName The remote player's username
     * @param isPlayerOne Whether the local player is Player 1 (red)
     */
    public void setupMatchedPlayers(String localPlayerName, String remotePlayerName, boolean isPlayerOne) {
        if (isPlayerOne) {
            // Local user is Player 1 (red)
            player1Name.setText(localPlayerName);
            player2Name.setText(remotePlayerName);
        } else {
            // Local user is Player 2 (green)
            player1Name.setText(remotePlayerName);
            player2Name.setText(localPlayerName);
        }
    }

    /**
     * Updates the game timer display
     */
    private void startTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        secondsRemaining = 600;
        timer.setText(formatTime(secondsRemaining));
        gameTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsRemaining--;
            timer.setText(formatTime(secondsRemaining));
            if (secondsRemaining <= 0) {
                gameTimer.stop();
                handleGameOver(null);
            }
        }));
        gameTimer.setCycleCount(Timeline.INDEFINITE);
        gameTimer.play();
    }
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }

    /**
     * This method will be called by the game logic team to set whose turn it is
     * @param isLocalTurn true if it's the local player's turn, false otherwise
     */
    public void setPlayerTurn(boolean isLocalTurn) {
        this.isLocalPlayerTurn = isLocalTurn;
        // Update UI to indicate whose turn it is (can be implemented later)
    }

    /**
     * This method will be called by the game logic team when a game ends
     * @param winnerName name of the player who won, or null for a draw
     */
    public void handleGameOver(String winnerName) {
        // Display game over message and update UI
        // This will be implemented by UI team after game logic is done
    }

    @FXML
    private void handleAbortGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        PageNavigator.navigateTo("dashboard"); // Navigate to dashboard
    }

    @FXML
    private void handleNewGame() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Game");
        alert.setHeaderText("Starting a new game will end the current one.");
        alert.setContentText("You will lose this game. Are you sure?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Reset game with current players
                String localPlayer = player1Name.getText();
                String remotePlayer = player2Name.getText();
                setupMatchedPlayers(localPlayer, remotePlayer, true); // Assuming local is Player 1
                startTimer(); // Restart timer
                System.out.println("New game started with " + localPlayer + " vs " + remotePlayer);
                // Game logic team to reset board here
            }
        });
    }

    /**
     * This method will be called by the matchmaking team to clean up resources
     * when navigating away from this screen
     */
    public void cleanup() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        System.out.println("Cleaning up Connect4 game resources");
    }

    @FXML
    private void handleChat() {
        Dialog<String> chatDialog = new Dialog<>();
        chatDialog.setTitle("Chat");
        chatDialog.setHeaderText("Chat with " + player2Name.getText());

        // Set up dialog content
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setPrefHeight(200);
        TextField messageField = new TextField();
        messageField.setPromptText("Type your message...");
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            String message = messageField.getText();
            if (!message.trim().isEmpty()) {
                chatArea.appendText("Me: " + message + "\n");
                messageField.clear();
                // Chat team to add networking logic here
            }
        });

        VBox content = new VBox(10, chatArea, messageField, sendButton);
        chatDialog.getDialogPane().setContent(content);

        // Add OK button to close dialog
        chatDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        chatDialog.show();
    }
}