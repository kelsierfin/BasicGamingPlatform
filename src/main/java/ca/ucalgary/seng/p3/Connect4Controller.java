package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

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

    private boolean isLocalPlayerTurn = true;

    @FXML
    private void initialize() {
        if (chatArea != null) {
            chatArea.setEditable(false);
        }
        player1Name.setText("PLAYER 1");
        player2Name.setText("PLAYER 2");
        setupGameBoard();
        updateTurnIndicator();
        timer.setText("10:00");
    }

    private void setupGameBoard() {
        gameBoardGrid.setStyle("-fx-background-color: Grey;");
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
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
        if (!isLocalPlayerTurn) return;
        Circle slot = findLowestEmptySlot(column);
        if (slot != null) {
            slot.setFill(javafx.scene.paint.Color.RED);
            switchTurn(false);
            System.out.println("Column " + column + " clicked - Game logic team to implement");
        }
    }

    private Circle findLowestEmptySlot(int column) {
        for (int row = 5; row >= 0; row--) {
            Circle slot = (Circle) getNodeFromGridPane(gameBoardGrid, column, row);
            if (slot.getFill().equals(javafx.scene.paint.Color.WHITE)) {
                return slot;
            }
        }
        return null;
    }

    private javafx.scene.Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
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
        isLocalPlayerTurn = isLocalTurn;
        updateTurnIndicator();
        if (!isLocalTurn) {
            System.out.println("Opponent's turn - Game logic team to implement");
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

    public void setupMatchedPlayers(String localPlayerName, String remotePlayerName, boolean isPlayerOne) {
        if (isPlayerOne) {
            player1Name.setText(localPlayerName);
            player2Name.setText(remotePlayerName);
        } else {
            player1Name.setText(remotePlayerName);
            player2Name.setText(localPlayerName);
        }
        setupGameBoard();
    }
}