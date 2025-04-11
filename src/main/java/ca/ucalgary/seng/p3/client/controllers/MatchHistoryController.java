package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.server.leadmatch.MatchData;
import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.Response;
import ca.ucalgary.seng.p3.network.ClientSocketService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchHistoryController {

    @FXML
    private ComboBox<String> gameFilterComboBox;

//    @FXML
//    private ComboBox<String> timeFilterComboBox;

    @FXML
    private VBox matchHistory;

    @FXML
    private Label gamesPlayed;

    @FXML
    private Label wins;

    @FXML
    private Label winRate;

    List<MatchData> matches;

    Map<String, Object> playerStats;

    @FXML
    private void initialize() throws IOException {
        // Initialize the filter dropdown values
        gameFilterComboBox.getItems().addAll("All Games", "Chess", "Tic Tac Toe", "Connect Four", "Go");
        gameFilterComboBox.setValue("All Games");

//        timeFilterComboBox.getItems().addAll("Last 30 Days", "Last 7 Days", "Today", "All Time");
//        timeFilterComboBox.setValue("Last 30 Days");

        // Add listeners for filter changes
        gameFilterComboBox.setOnAction(e -> updateMatchHistory());
//        timeFilterComboBox.setOnAction(e -> updateMatchHistory());

        // Use network requests instead of directly accessing server components
        ClientSocketService socketService = ClientSocketService.getInstance();

        // Request player stats
        Request statsRequest = new Request("getPlayerStats", LogInController.getCurrentUsername(), "");
        Response statsResponse = socketService.sendRequest(statsRequest);

        // Create a default map for player stats
        playerStats = new HashMap<>();
        playerStats.put("overallGamesPlayed", 0);
        playerStats.put("overallGamesWon", 0);
        playerStats.put("overallWinRate", 0.0);

        // If the request is successful, update the stats
        if (statsResponse.isSuccess()) {
            // In a real implementation, parse JSON data from statsResponse.getExtra()
            // For now, use sample values
            playerStats.put("overallGamesPlayed", 10);
            playerStats.put("overallGamesWon", 5);
            playerStats.put("overallWinRate", 50.0);
        }

        // Update UI with stats
        gamesPlayed.setText("Total Game: " + playerStats.get("overallGamesPlayed"));
        wins.setText("Wins: " + playerStats.get("overallGamesWon"));
        winRate.setText("Win Rate: " + playerStats.get("overallWinRate"));

        // Request match history
        Request matchRequest = new Request("getMatchHistory", LogInController.getCurrentUsername(), "");
        Response matchResponse = socketService.sendRequest(matchRequest);

        // Create empty match list
        matches = new ArrayList<>();

        // If the request is successful, populate with match data
        if (matchResponse.isSuccess()) {
            // In a real implementation, parse JSON from matchResponse.getExtra()
            // For now, use sample match data
            matches.add(new MatchData(LogInController.getCurrentUsername(), "opponent1", "chess", "win", 15));
            matches.add(new MatchData(LogInController.getCurrentUsername(), "opponent2", "go", "loss", 30));
        }

        // Send a sample update request (just for demonstration) - replacing PlayerStats direct call
        Request updateRequest = new Request("updatePlayerStats",
                LogInController.getCurrentUsername(),
                "player98",
                "go,true,20");
        socketService.sendRequest(updateRequest);

        loadMatchHistory(matches);
    }

    public void loadMatchHistory(List<MatchData> matches) {
        matchHistory.getChildren().clear();

        for (MatchData match : matches) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/match_entry.fxml"));
                BorderPane entry = loader.load();

                MatchEntryController controller = loader.getController();
                controller.setMatchData(match.getOpponentUsername(), match.getGameType(), match.getOutcome());

                controller.getRematchButton().setOnAction(e -> {
                    System.out.println("Rematch with " + match.getOpponentUsername());
                    //TODO: REMATCH LOGIC
                });
                matchHistory.getChildren().add(entry);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void updateMatchHistory() {
        // This would filter the match history based on selected values
        System.out.println("Filter changed - Game: " + gameFilterComboBox.getValue());
//                + ", Time: " + timeFilterComboBox.getValue());

        // In a real implementation, this would update the match list
        // For now we're just using static content in the FXML
    }

    @FXML
    private void handleRematchButton() {
        // This method would be connected to the rematch buttons
        System.out.println("Rematch requested");
        // In a real implementation, this would initiate a rematch request
        //TODO: IMPLEMENT MATCHMAKING
    }
}