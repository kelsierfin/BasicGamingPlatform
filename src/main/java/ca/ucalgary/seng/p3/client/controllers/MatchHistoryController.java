package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.client.reflection.LeaderboardReflector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
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
    LeaderboardReflector leaderboardReflector;

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

        // Get leaderboard reflector instance
        leaderboardReflector = LeaderboardReflector.getInstance();

        // Request player stats
        LeaderboardReflector.PlayerStatsResult statsResult = leaderboardReflector.getPlayerStats(LogInController.getCurrentUsername());

        // If the request is successful, update the stats
        if (statsResult.isSuccess()) {
            // Update UI with stats
            gamesPlayed.setText("Total Game: " + statsResult.getOverallGamesPlayed());
            wins.setText("Wins: " + statsResult.getOverallGamesWon());
            winRate.setText("Win Rate: " + statsResult.getOverallWinRate() + "%");
        } else {
            // Handle error case
            gamesPlayed.setText("Total Game: 0");
            wins.setText("Wins: 0");
            winRate.setText("Win Rate: 0%");
            System.err.println("Error getting player stats: " + statsResult.getMessage());
        }

        // Request match history
        LeaderboardReflector.MatchHistoryResult matchResult = leaderboardReflector.getMatchHistory(LogInController.getCurrentUsername());

        // Create empty match list
        matches = new ArrayList<>();

        // If the request is successful, populate with match data
        if (matchResult.isSuccess()) {
            // In a real implementation, parse JSON from matchResponse.getExtra()
            // For now, use sample match data
            matches = matchResult.getMatches();
        } else {
            System.err.println("Error getting match history: " + matchResult.getMessage());
        }

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
        // Filter the match history based on selected game type
        String gameFilter = gameFilterComboBox.getValue();
        if (gameFilter.equals("All Games")) {
            loadMatchHistory(matches);
            return;
        }

        // Convert the filter name to match the game type in the data
        String gameType = convertFilterNameToGameType(gameFilter);

        // Filter matches by game type
        List<MatchData> filteredMatches = new ArrayList<>();
        for (MatchData match : matches) {
            if (match.getGameType().equalsIgnoreCase(gameType)) {
                filteredMatches.add(match);
            }
        }

        loadMatchHistory(filteredMatches);
        // In a real implementation, this would update the match list
        // For now we're just using static content in the FXML

    }


    private String convertFilterNameToGameType(String filterName) {
        switch (filterName) {
            case "Tic Tac Toe":
                return "tictactoe";
            case "Connect Four":
                return "connect4";
            default:
                return filterName.toLowerCase();
        }
    }

    @FXML
    private void handleRematchButton() {
        // This method would be connected to the rematch buttons
        System.out.println("Rematch requested");
        // In a real implementation, this would initiate a rematch request
        //TODO: IMPLEMENT MATCHMAKING
    }
}