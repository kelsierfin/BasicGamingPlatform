package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.server.authentication.AccountRegistrationCSV;
import ca.ucalgary.seng.p3.server.authentication.ViewPlayerProfile;
import ca.ucalgary.seng.p3.server.leadmatch.MatchmakingLogic;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
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

    @FXML
    public void initialize() throws IOException {
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);
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
    public static String getFormattedStats(String playerName) throws IOException {
        Map<String, Object> playerStats = ViewPlayerProfile.getPlayerStats(playerName);
        Map<String, Map<String, Integer>> leaderboardPositions = ViewPlayerProfile.getLeaderboardPositions(playerName);

        StringBuilder sb = new StringBuilder();

        sb.append("📊 Overall Statistics\n");
        sb.append("────────────────────────────\n");
        sb.append("Games Played: ").append(playerStats.get("overallGamesPlayed")).append("\n");
        sb.append("Games Won   : ").append(playerStats.get("overallGamesWon")).append("\n");
        sb.append(String.format("Win Rate    : %.2f%%\n\n", playerStats.get("overallWinRate")));

        sb.append("🎯 Game-Specific Statistics\n");
        sb.append("────────────────────────────\n");
        sb.append(printGameStats("Chess", (Map<String, Integer>) playerStats.get("chessStats")));
        sb.append(printGameStats("Connect 4", (Map<String, Integer>) playerStats.get("connect4Stats")));
        sb.append(printGameStats("Go", (Map<String, Integer>) playerStats.get("goStats")));
        sb.append(printGameStats("Tic-Tac-Toe", (Map<String, Integer>) playerStats.get("tictactoeStats")));
        sb.append("\n");

        sb.append("📅 Match History\n");
        sb.append("────────────────────────────\n");
        sb.append(ViewPlayerProfile.getFormattedMatchHistory(playerName)).append("\n\n");

        sb.append("🏆 Leaderboard Positions\n");
        sb.append("────────────────────────────\n");
        for (String game : leaderboardPositions.keySet()) {
            Map<String, Integer> data = leaderboardPositions.get(game);
            sb.append(String.format("%-13s: Rank %d (ELO: %d)\n", game, data.get("rank"), data.get("elo")));
        }

        return sb.toString();
    }


    private static String printGameStats(String gameName, Map<String, Integer> stats) {
        return String.format("%-13s - Played: %-3d Won: %-3d Lost: %-3d\n",
                gameName,
                stats.getOrDefault("played", 0),
                stats.getOrDefault("won", 0),
                stats.getOrDefault("lost", 0));
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

        // Game buttons
        Button chessButton = new Button("Chess");
        Button goButton = new Button("Go");
        Button connect4Button = new Button("Connect 4");
        Button tttButton = new Button("Tic-Tac-Toe");

        // Button actions
        chessButton.setOnAction(e -> {
            //TODO: Insert MATCH LOGIC (Match between selectedPlayer and current Player (LoginController.getCurrentUsername()))
            MatchmakingLogic matchmakingLogic = new MatchmakingLogic("Chess");
            try {
                matchmakingLogic.matchPlayers(selected_Player, LogInController.getCurrentUsername());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            PageNavigator.navigateTo("chess");

            dialog.close();
        });

        goButton.setOnAction(e -> {
            //TODO: Insert MATCH LOGIC (Match between selectedPlayer and current Player (LoginController.getCurrentUsername()))
            MatchmakingLogic matchmakingLogic = new MatchmakingLogic("Go");
            try {
                matchmakingLogic.matchPlayers(selected_Player, LogInController.getCurrentUsername());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            PageNavigator.navigateTo("go");

            dialog.close();
        });

        connect4Button.setOnAction(e -> {
            //TODO: Insert MATCH LOGIC (Match between selectedPlayer and current Player (LoginController.getCurrentUsername()))
            MatchmakingLogic matchmakingLogic = new MatchmakingLogic("Connect4");
            try {
                matchmakingLogic.matchPlayers(selected_Player, LogInController.getCurrentUsername());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            PageNavigator.navigateTo("connect4");
            dialog.close();
        });

        tttButton.setOnAction(e -> {
            //TODO: Insert MATCH LOGIC (Match between selectedPlayer and current Player (LoginController.getCurrentUsername()))
            MatchmakingLogic matchmakingLogic = new MatchmakingLogic("Tic-Tac-Toe");
            try {
                matchmakingLogic.matchPlayers(selected_Player, LogInController.getCurrentUsername());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            PageNavigator.navigateTo("tictactoe");
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