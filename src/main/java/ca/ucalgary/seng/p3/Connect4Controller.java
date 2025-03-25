package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

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

    @FXML
    private void initialize() {
        // Initialize UI components
        System.out.println("Connect4 game screen initialized");

        // Set default values
        player1Name.setText("Player 1");
        player2Name.setText("Player 2");
        timer.setText("03:58");
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
     * @param timeString The time to display (format: "MM:SS")
     */
    public void updateTimer(String timeString) {
        timer.setText(timeString);
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
        System.out.println("Abort game button clicked");
        // Will be implemented by game logic team
    }

    @FXML
    private void handleNewGame() {
        System.out.println("New game button clicked");
        // Will be implemented by game logic team
    }

    @FXML
    private void handleChat() {
        System.out.println("Chat button clicked");
        // Will open the chat interface
    }

    /**
     * This method will be called by the matchmaking team to clean up resources
     * when navigating away from this screen
     */
    public void cleanup() {
        // Any cleanup logic needed when the game ends
        System.out.println("Cleaning up Connect4 game resources");
    }
}