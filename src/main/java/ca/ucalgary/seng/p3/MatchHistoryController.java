package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MatchHistoryController {

    @FXML
    private ComboBox<String> gameFilterComboBox;

    @FXML
    private ComboBox<String> timeFilterComboBox;

    @FXML
    private void initialize() {
        // Initialize the filter dropdown values
        gameFilterComboBox.getItems().addAll("All Games", "Chess", "Tic Tac Toe", "Connect Four", "Go");
        gameFilterComboBox.setValue("All Games");

        timeFilterComboBox.getItems().addAll("Last 30 Days", "Last 7 Days", "Today", "All Time");
        timeFilterComboBox.setValue("Last 30 Days");

        // Add listeners for filter changes
        gameFilterComboBox.setOnAction(e -> updateMatchHistory());
        timeFilterComboBox.setOnAction(e -> updateMatchHistory());
    }

    private void updateMatchHistory() {
        // This would filter the match history based on selected values
        System.out.println("Filter changed - Game: " + gameFilterComboBox.getValue()
                + ", Time: " + timeFilterComboBox.getValue());

        // In a real implementation, this would update the match list
        // For now we're just using static content in the FXML
    }

    @FXML
    private void handleRematchButton() {
        // This method would be connected to the rematch buttons
        System.out.println("Rematch requested");
        // Integration team - this should initiate a rematch request
    }

}