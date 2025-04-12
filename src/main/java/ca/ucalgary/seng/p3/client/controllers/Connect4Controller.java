package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.client.gamelogic.connect4.ChatBox;
import ca.ucalgary.seng.p3.client.gamelogic.connect4.Connect4_Board;
import ca.ucalgary.seng.p3.client.gamelogic.connect4.Disc;
import ca.ucalgary.seng.p3.client.gamelogic.connect4.Player;
import ca.ucalgary.seng.p3.server.leadmatch.PlayerStats;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;

public class Connect4Controller {

    @FXML private Label turnIndicator;
    @FXML private Button exitButton;
    @FXML private Button playAgainButton;
    @FXML private TextField chatTextField;
    @FXML private TextArea chatArea;
    @FXML private GridPane gameBoardGrid;
    @FXML private Circle player1Icon;
    @FXML private Circle player2Icon;
    @FXML private Label player1Name;
    @FXML private Label player2Name;

    private boolean isLocalPlayerTurn = true;
    private Connect4_Board gameBoard;
    private Player player1;
    private Player player2;
    private ChatBox chatBox;
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private static int turns = 0;

    @FXML
    private void initialize() {
        gameBoard = new Connect4_Board();
        player1 = new Player(LogInController.getCurrentUsername(), "R"); // Red for Player 1
        player2 = new Player("student", "Y"); // Yellow for Player 2
        chatBox = new ChatBox(player1, player2);

        chatArea.setEditable(false);
        player1Name.setText(LogInController.getCurrentUsername());
        player2Name.setText("student");
        turns = 0;
        setupGameBoard();
        updateTurnIndicator();
    }

    private void setupGameBoard() {
        gameBoardGrid.getChildren().clear(); // To clear existing slots
        gameBoardGrid.setStyle("-fx-background-color: Grey;");
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle slot = new Circle(30, Color.WHITE);
                slot.setStroke(Color.BLACK);
                gameBoardGrid.add(slot, col, row);
                final int column = col;
                slot.setOnMouseClicked(e -> {
                    try {
                        handleColumnClick(column);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
        }
    }

    @FXML
    private void handleColumnClick(int column) throws IOException {
        Player currentPlayer = isLocalPlayerTurn ? player1 : player2;
        Color discColor = isLocalPlayerTurn ? Color.RED : Color.YELLOW;

        if (!gameBoard.validMove(column)) {
            showAlert(Alert.AlertType.WARNING, "Invalid Move", "Column " + (column + 1) + " is Full.");
            return;
        }

        gameBoard.placeDisc(column, new Disc(currentPlayer.getSelectedSymbol()));
        updateGUIBoard();

        if (gameBoard.checkWin(currentPlayer.getSelectedSymbol())) {
            if (currentPlayer.getName().equals(LogInController.getCurrentUsername())) {
                showAlert(Alert.AlertType.INFORMATION, "Game Over", "Congratulations " + LogInController.getCurrentUsername() + "! You have won this game!");
                //TODO: Connect to leaderboard
//                player1.getName();
//                player2.getName();
                PlayerStats.updatePlayerStats(player1.getName(), player2.getName(), true, "connect4", turns);
                disableBoard();
            }
            else{
                showAlert(Alert.AlertType.INFORMATION, "Game Over", LogInController.getCurrentUsername()+ "! You have lost this game!");
                //TODO: Connect to leaderboard
                PlayerStats.updatePlayerStats(player2.getName(), player1.getName(), true, "connect4", turns);
//                System.out.println(player1.getName(),player2.getName());
                disableBoard();
            }
            return;
        }

        if (isBoardFull()) {
            showAlert(Alert.AlertType.INFORMATION, "Game Over", "It's a draw!");
            disableBoard();
            return;
        }
        switchTurn(!isLocalPlayerTurn);
    }

    private void updateGUIBoard() {
        String[][] boardState = gameBoard.getBoard();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Circle slot = (Circle) getNodeFromGridPane(gameBoardGrid, col, row);
                String cell = boardState[row][col].trim();
                if (cell.equals("[R]") || cell.equals("[R]\n")) {
                    slot.setFill(Color.RED);
                } else if (cell.equals("[Y]") || cell.equals("[Y]\n")) {
                    slot.setFill(Color.YELLOW);
                } else {
                    slot.setFill(Color.WHITE);
                }
            }
        }
    }

    private void disableBoard() {
        gameBoardGrid.setDisable(true);
    }

    private javafx.scene.Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private void switchTurn(boolean isLocalTurn) {
        isLocalPlayerTurn = isLocalTurn;
        updateTurnIndicator();
    }

    private void updateTurnIndicator() {
        Player currentPlayer = isLocalPlayerTurn ? player1 : player2;
        turnIndicator.setText(currentPlayer.getName() + "'s Turn");
        turns++;
        if (isLocalPlayerTurn) {
            player1Icon.setStroke(Color.BLACK);
            player2Icon.setStroke(null);
        } else {
            player2Icon.setStroke(Color.BLACK);
            player1Icon.setStroke(null);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setupMatchedPlayers(String localPlayerName, String remotePlayerName, boolean isPlayerOne) {
        player1 = new Player(localPlayerName, "R");
        player2 = new Player(remotePlayerName, "Y");
        player1Name.setText(player1.getName());
        player2Name.setText(player2.getName());
        setupGameBoard();
        isLocalPlayerTurn = isPlayerOne;
        updateTurnIndicator();
    }
    private boolean isBoardFull() {
        for (int col = 0; col < COLUMNS; col++) {
            if (gameBoard.validMove(col)) {
                return false;
            }
        }
        return true;
    }

    @FXML
    private void handleExit() {
        PageNavigator.navigateTo("startgame_connect4");
    }

    @FXML
    private void handlePlayAgain() {
        gameBoard = new Connect4_Board();
        isLocalPlayerTurn = true;
        gameBoardGrid.setDisable(false);
        setupGameBoard();
        updateGUIBoard();
        updateTurnIndicator();
    }

    @FXML
    private void handleSend() {
        String message = chatTextField.getText();
        if (message != null && !message.trim().isEmpty()) {
            Player currentPlayer = isLocalPlayerTurn ? player1 : player2;
            String formattedMessage = currentPlayer.getName() + ": " + message + "\n";
            chatBox.sendMessage(formattedMessage);
            chatArea.appendText(formattedMessage);
            chatTextField.clear();
        }
    }
}