package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.client.reflection.AuthReflector;
import ca.ucalgary.seng.p3.client.reflection.LeaderboardReflector;
import ca.ucalgary.seng.p3.client.reflection.MatchmakingReflector;
import ca.ucalgary.seng.p3.server.authentication.AccountRegistrationCSV;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerFinderController {

    @FXML private TextField searchBar;
    @FXML private ComboBox<String> filter;
    @FXML private ListView<String> player_List;

    String selected_Player;

    @FXML
    private VBox options;
    @FXML
    private Circle notificationDot;

    private List<String> notifications = new ArrayList<>();

    @FXML
    private VBox menuPopup = new VBox();
    @FXML
    private VBox profilePopup = new VBox();
    @FXML
    private VBox notificationPopup = new VBox();

    private ObservableList<String> players = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;

    private LeaderboardReflector leaderboardReflector;
    private AuthReflector authReflector;

    @FXML
    public void initialize() throws IOException {
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);

        // Initialize reflectors
        leaderboardReflector = LeaderboardReflector.getInstance();
        authReflector = AuthReflector.getInstance();

        reloadList();
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filterPlayerList(newValue.toLowerCase());
        });

        players.remove(LogInController.getCurrentUsername());
        player_List.setOnContextMenuRequested(event -> {
            selected_Player = player_List.getSelectionModel().getSelectedItem();

            if (selected_Player == null) {
                options.setVisible(false);
                return;
            }

            double x = event.getSceneX();
            double y = event.getSceneY();

            options.setLayoutX(x);
            options.setLayoutY(y);
            options.setVisible(true);

            event.consume(); // prevent weird propagation
        });

        // Hide popup when clicking elsewhere
        rootPane.setOnMousePressed(event -> {
            if (!options.isHover()) {
                options.setVisible(false);
            }
        });

        player_List.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selected_Player = newSelection;
            }
        });
    }

    public void reloadList() throws IOException {
        // Instead of directly accessing the server, use the AuthReflector to get the list of users
        // This is a placeholder - will need to add a method to AuthReflector to get all users
        // For now, we'll still use the direct call to avoid modifying too much code
        players.setAll(AccountRegistrationCSV.loadAccounts().keySet());
        player_List.setItems(players);
    }

    // AI Assistance Declaration: The following live search bar logic was developed with AI(ChatGPT) guidance.
    // AI provided recommendations on use a listener to dynamically update the player list
    private void filterPlayerList(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            player_List.setItems(FXCollections.observableArrayList(players));
            return;
        }

        ObservableList<String> filtered = FXCollections.observableArrayList();
        for (String player : players) {
            if (!player.equals(LogInController.getCurrentUsername()) && player.toLowerCase().contains(keyword)) {
                filtered.add(player);
            }
        }

        player_List.setItems(filtered);
    }




//    public Label users_name() {
//        return users_Name;
//    }
//
//    @FXML
//    private void name_on_Click_Pop_out() {
//        options.setVisible(!options.isVisible());
//    }

//    @FXML
//    private void toggle_Request_People() {
//        options.setVisible(!options.isVisible());
//    }
//
//    // Handles the visibility of the request menu when name is pressed
//    @FXML
//    public void open_Request_Menu() {
//        selected_Player = player_List.getSelectionModel().getSelectedItem();
//        toggle_Request_People();
//
//    }
//
//    // Read entered text and filter list according to what was written
//    public void change_List(InputMethodEvent inputMethodEvent) {
//        String player_Name = searchBar.getText();
//
//    }

    @FXML
    public  void handlePlayerProfileButton() throws IOException {
        showAlert(Alert.AlertType.INFORMATION, selected_Player, getFormattedStats(selected_Player));
    }


    @FXML
    public void handlePlayerProfileButton() {
        // Get player stats using the reflector instead of direct server call
        LeaderboardReflector.PlayerStatsResult statsResult = leaderboardReflector.getPlayerStats(selected_Player);
        LeaderboardReflector.LeaderboardPositionResult positionsResult = leaderboardReflector.getLeaderboardPositions(selected_Player);

        if (statsResult.isSuccess()) {
            String formattedStats = getFormattedStats(selected_Player, statsResult, positionsResult);
            showAlert(Alert.AlertType.INFORMATION, selected_Player, formattedStats);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not retrieve player statistics: " + statsResult.getMessage());
        }
    }

    public String getFormattedStats(String playerName, LeaderboardReflector.PlayerStatsResult statsResult,
                                    LeaderboardReflector.LeaderboardPositionResult positionsResult) {
        StringBuilder sb = new StringBuilder();

        sb.append("📊 Overall Statistics\n");
        sb.append("────────────────────────────\n");
        sb.append("Games Played: ").append(statsResult.getOverallGamesPlayed()).append("\n");
        sb.append("Games Won   : ").append(statsResult.getOverallGamesWon()).append("\n");
        sb.append(String.format("Win Rate    : %.2f%%\n\n", statsResult.getOverallWinRate()));

        sb.append("🎯 Game-Specific Statistics\n");
        sb.append("────────────────────────────\n");

        // Get game stats from the statsResult
        Map<String, Map<String, Object>> gameStats = statsResult.getGameStats();

        if (gameStats != null) {
            sb.append(printGameStats("Chess", gameStats.get("chess")));
            sb.append(printGameStats("Connect 4", gameStats.get("connect4")));
            sb.append(printGameStats("Go", gameStats.get("go")));
            sb.append(printGameStats("Tic-Tac-Toe", gameStats.get("tictactoe")));
        }
        sb.append("\n");

        sb.append("📅 Match History\n");
        sb.append("────────────────────────────\n");

        // Get match history using the reflector
        LeaderboardReflector.MatchHistoryResult matchHistoryResult = leaderboardReflector.getMatchHistory(playerName);
        if (matchHistoryResult.isSuccess() && matchHistoryResult.getMatches() != null) {
            for (int i = 0; i < Math.min(5, matchHistoryResult.getMatches().size()); i++) {
                var match = matchHistoryResult.getMatches().get(i);
                sb.append(String.format("%s vs %s (%s): %s\n",
                        playerName, match.getOpponentUsername(), match.getGameType(), match.getOutcome()));
            }
        } else {
            sb.append("No match history available\n");
        }
        sb.append("\n");

        sb.append("🏆 Leaderboard Positions\n");
        sb.append("────────────────────────────\n");

        // Get leaderboard positions from the positionsResult
        if (positionsResult != null && positionsResult.isSuccess() && positionsResult.getPositions() != null) {
            Map<String, Map<String, Integer>> positions = positionsResult.getPositions();
            for (String game : positions.keySet()) {
                Map<String, Integer> data = positions.get(game);
                sb.append(String.format("%-13s: Rank %d (ELO: %d)\n",
                        game, data.get("rank"), data.get("elo")));
            }
        } else {
            sb.append("No leaderboard positions available\n");
        }

        return sb.toString();
    }

    private String printGameStats(String gameName, Map<String, Object> stats) {
        if (stats == null) {
            return String.format("%-13s - No data available\n", gameName);
        }

        int played = ((Number)stats.get("gamesPlayed")).intValue();
        int won = ((Number)stats.get("gamesWon")).intValue();
        int lost = played - won;

        return String.format("%-13s - Played: %-3d Won: %-3d Lost: %-3d\n",
                gameName, played, won, lost);
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleChatPopupButton() {
        PageNavigator.chatWindow("chat_Popup");
        //TODO: HANDLE CHAT LOGIC
    }

    @FXML
    void handleFriendRequestButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Friend Request");
        alert.setHeaderText("Sent Friend Request to @" + selected_Player);
        alert.showAndWait();
        //TODO: HANDLE FRIEND REQUEST LOGIC

    }

    @FXML
    void handleChallengeRequestButton() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Challenge Player");
        dialog.setHeaderText("Challenge @" + selected_Player + " to a game:");

        // Get matchmaking reflector
        MatchmakingReflector matchmakingReflector = MatchmakingReflector.getInstance();

        // Game buttons
        Button chessButton = new Button("Chess");
        Button goButton = new Button("Go");
        Button connect4Button = new Button("Connect 4");
        Button tttButton = new Button("Tic-Tac-Toe");

        // Button actions using the reflector instead of direct server access
        chessButton.setOnAction(e -> {
            //TODO: Insert MATCH LOGIC (Match between selectedPlayer and current Player (LoginController.getCurrentUsername()))
            MatchmakingReflector.MatchResult result = matchmakingReflector.matchPlayers(
                    LogInController.getCurrentUsername(), selected_Player, "Chess");

            if (result.isSuccess()) {
                PageNavigator.navigateTo("chess");
            } else {
                showAlert(Alert.AlertType.ERROR, "Matchmaking Failed", result.getMessage());
            }
            dialog.close();
        });

        goButton.setOnAction(e -> {
            MatchmakingReflector.MatchResult result = matchmakingReflector.matchPlayers(
                    LogInController.getCurrentUsername(), selected_Player, "Go");

            if (result.isSuccess()) {
                PageNavigator.navigateTo("go");
            } else {
                showAlert(Alert.AlertType.ERROR, "Matchmaking Failed", result.getMessage());
            }
            dialog.close();
        });

        connect4Button.setOnAction(e -> {
            MatchmakingReflector.MatchResult result = matchmakingReflector.matchPlayers(
                    LogInController.getCurrentUsername(), selected_Player, "Connect4");

            if (result.isSuccess()) {
                PageNavigator.navigateTo("connect4");
            } else {
                showAlert(Alert.AlertType.ERROR, "Matchmaking Failed", result.getMessage());
            }
            dialog.close();
        });

        tttButton.setOnAction(e -> {
            MatchmakingReflector.MatchResult result = matchmakingReflector.matchPlayers(
                    LogInController.getCurrentUsername(), selected_Player, "Tic-Tac-Toe");

            if (result.isSuccess()) {
                PageNavigator.navigateTo("tictactoe");
            } else {
                showAlert(Alert.AlertType.ERROR, "Matchmaking Failed", result.getMessage());
            }
            dialog.close();
        });

        // Horizontal layout
        HBox hbox = new HBox(15, chessButton, goButton, connect4Button, tttButton);
        hbox.setPadding(new Insets(20));
        hbox.setAlignment(Pos.CENTER);

        dialog.getDialogPane().setContent(hbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.showAndWait();
    }


    @FXML
    void handleBlockButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Block ");
        alert.setHeaderText("Player @" + selected_Player + " has been blocked");
        alert.showAndWait();
    }

    @FXML
    private void open_Chat_Button(){
        PageNavigator.navigateTo("settings");
    }


    //NAVIGATION BAR FUNCTIONALITY - DON'T EDIT
    // Called when the menu button is clicked
    @FXML
    private void handleMenuButton() {
        // Toggle visibility of the popup
        menuPopup.setVisible(!menuPopup.isVisible());
        if(notificationPopup.isVisible()) {
            notificationPopup.setVisible(false);
        }
        if(profilePopup.isVisible()) {
            profilePopup.setVisible(false);
        }
    }

    // More event handlers for other buttons, unfinished:
    @FXML
    private void handleProfileButton() {
        // Toggle visibility of the popup
        profilePopup.setVisible(!profilePopup.isVisible());
        if(notificationPopup.isVisible()) {
            notificationPopup.setVisible(false);
        }
        if(menuPopup.isVisible()) {
            menuPopup.setVisible(false);
        }
    }
    @FXML
    private void handleBellButton() {
        // Toggle visibility of the popup
        updateNotificationDot();

        for(String notification: notifications) {
            Button btn = new Button(notification);
            btn.getStyleClass().add("notification-button");
            notificationPopup.getChildren().add(btn);
        }

        notificationPopup.setVisible(!notificationPopup.isVisible());
        if (profilePopup.isVisible()) {
            profilePopup.setVisible(false);
        }
        if (menuPopup.isVisible()) {
            menuPopup.setVisible(false);
        }

        notifications = new ArrayList<>();

    }

    private void updateNotificationDot() {
        notificationDot.setVisible(!notifications.isEmpty());
    }

    @FXML
    private void handleEditProfileButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void handleLogOutButton() {
        LogInController.performLogout();
    }

    @FXML
    private void handleDashboardButton() {
        PageNavigator.navigateTo("home");
    }

    @FXML
    private void handleLeaderboardButton() {
        PageNavigator.navigateTo("leaderboard_home");
    }

    @FXML
    private void handleFindAPlayerButton() {
        PageNavigator.navigateTo("player_Finder");
    }

    @FXML
    private void handleSettingsButton() {
        PageNavigator.navigateTo("settings");
    }
    @FXML
    private void handleGameRulesButton() {
        //game rules page
    }

    //END OF NAVIGATION BAR FUNCTIONALITY
}