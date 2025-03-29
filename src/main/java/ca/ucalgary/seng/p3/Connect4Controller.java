package ca.ucalgary.seng.p3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Connect4Controller {

    @FXML private Label timer;
    @FXML private Button exitButton;
    @FXML private Button sendButton;
    @FXML private TextField chatTextField;
    @FXML private TextArea chatArea;
    @FXML private GridPane gameBoardGrid;
    @FXML private Circle player1Icon;
    @FXML private Circle player2Icon;
    @FXML private Label player1Name;
    @FXML private Label player2Name;

    private String[][] board; // 6x7 game board
    private boolean isLocalPlayerTurn = true;
    private boolean gameEnd = false;
    private Timeline gameTimer;
    private int secondsRemaining = 600;
    private final int ROWS = 6;
    private final int COLUMNS = 7;

    @FXML
    private void initialize() {
        if (chatArea != null) {
            chatArea.setEditable(false);
        }
        player1Name.setText("PLAYER 1");
        player2Name.setText("PLAYER 2");
        initializeGame();
        startTimer();
    }

    private void initializeGame() {
        // Initialize the board as a 6x7 grid of empty slots
        board = new String[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                board[row][col] = " "; // Empty slot
            }
        }
        setupGameBoard();
        updateTurnIndicator();
    }

    private void setupGameBoard() {
        gameBoardGrid.setStyle("-fx-background-color: Grey;");
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle slot = new Circle(30, javafx.scene.paint.Color.WHITE);
                slot.setStroke(javafx.scene.paint.Color.BLACK);
                gameBoardGrid.add(slot, col, row);
                final int column = col;
                slot.setOnMouseClicked(e -> handleColumnClick(column));
            }
        }
    }

    @FXML
    private void handleColumnClick(int column) {
        if (!isLocalPlayerTurn || gameEnd) return;

        if (placeDisc(column, "R")) { // "R" for Player 1
            updateBoardUI();
            if (checkWin("R")) { // Placeholder for win check
                gameEnd = true;
                handleGameOver(player1Name.getText());
            } else {
                switchTurn(false); // Switch to opponent
            }
        }
    }

    private boolean placeDisc(int column, String symbol) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][column].equals(" ")) {
                board[row][column] = symbol;
                return true;
            }
        }
        return false; // Column is full
    }

    private void updateBoardUI() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle slot = (Circle) gameBoardGrid.getChildren().get(row * COLUMNS + col);
                if (board[row][col].equals("R")) {
                    slot.setFill(javafx.scene.paint.Color.RED);
                } else if (board[row][col].equals("Y")) {
                    slot.setFill(javafx.scene.paint.Color.YELLOW);
                } else {
                    slot.setFill(javafx.scene.paint.Color.WHITE);
                }
            }
        }
    }

    private boolean checkWin(String symbol) {
        // Placeholder: Add win checking logic later when merging with Connect4_Board
        // For now, return false to keep the game running
        return false;
    }

    @FXML
    private void handleExit() {
        if (gameTimer != null) gameTimer.stop();
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close(); // Or use PageNavigator.navigateTo("dashboard")
    }

    @FXML
    private void handleSend() {
        String message = chatTextField.getText();
        if (message != null && !message.trim().isEmpty()) {
            chatArea.appendText("Me: " + message + "\n");
            chatTextField.clear();
        }
    }

    private void switchTurn(boolean isLocalTurn) {
        this.isLocalPlayerTurn = isLocalTurn;
        updateTurnIndicator();
        if (!isLocalTurn && !gameEnd) {
            simulateOpponentMove();
        }
    }

    private void updateTurnIndicator() {
        if (isLocalPlayerTurn) {
            player1Icon.setStroke(javafx.scene.paint.Color.BLACK);
            player2Icon.setStroke(null);
        } else {
            player2Icon.setStroke(javafx.scene.paint.Color.BLACK);
            player1Icon.setStroke(null);
        }
    }

    private void simulateOpponentMove() {
        for (int col = 0; col < COLUMNS; col++) {
            if (placeDisc(col, "Y")) { // "Y" for Player 2
                updateBoardUI();
                if (checkWin("Y")) { // Placeholder for win check
                    gameEnd = true;
                    handleGameOver(player2Name.getText());
                } else {
                    switchTurn(true); // Switch back to player
                }
                break;
            }
        }
    }

    private void startTimer() {
        if (gameTimer != null) gameTimer.stop();
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

    private void handleGameOver(String winnerName) {
        gameTimer.stop();
        String message = winnerName != null ? winnerName + " wins!" : "Game ended in a draw!";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setupMatchedPlayers(String localPlayerName, String remotePlayerName, boolean isPlayerOne) {
        if (isPlayerOne) {
            player1Name.setText(localPlayerName);
            player2Name.setText(remotePlayerName);
        } else {
            player1Name.setText(remotePlayerName);
            player2Name.setText(localPlayerName);
        }
        initializeGame();
    }
}