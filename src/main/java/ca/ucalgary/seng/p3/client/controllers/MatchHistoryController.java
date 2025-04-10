package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.server.authentication.Match_History;
import ca.ucalgary.seng.p3.server.authentication.ViewPlayerProfile;
import ca.ucalgary.seng.p3.server.leadmatch.MatchData;
import ca.ucalgary.seng.p3.server.leadmatch.PlayerStatData;
import ca.ucalgary.seng.p3.server.leadmatch.PlayerStats;
import javafx.css.Match;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
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



        playerStats = ViewPlayerProfile.getPlayerStats(LogInController.getCurrentUsername());
        gamesPlayed.setText("Total Game: " + playerStats.get("overallGamesPlayed"));
        wins.setText("Wins: " + playerStats.get("overallGamesWon"));
        winRate.setText("Win Rate: " + playerStats.get("overallWinRate"));

        matches = Match_History.getMatchHistory(LogInController.getCurrentUsername());
        PlayerStats.updatePlayerStats(LogInController.getCurrentUsername(),"player98",true,"go", 20);

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