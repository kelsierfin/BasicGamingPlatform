package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.server.leadmatch.Leaderboard;
import ca.ucalgary.seng.p3.server.leadmatch.LeaderboardData;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;

import javax.swing.text.Position;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LeaderBoardHome {

    private static final String CHESS_TITLE = "Chess";
    private static final String CHESS_BCKGRND = "/icons/chess_background.png";
    private static final String GO_TITLE = "Go";
    private static final String GO_BCKGRND = "/icons/go_background.png";
    private static final String CONNECT_TITLE = "Connect4";
    private static final String CONNECT_BCKGRND = "/icons/connect4_background.png";
    private static final String TTT_TITLE = "TicTacToe";
    private static final String TTT_BCKGRND = "/icons/ttt_background.png";

    @FXML
    private AnchorPane tttPane = new AnchorPane();
    @FXML
    private AnchorPane chessPane = new AnchorPane();
    @FXML
    private AnchorPane goPane = new AnchorPane();
    @FXML
    private AnchorPane connect4Pane = new AnchorPane();

    @FXML
    public AnchorPane root;

    //NAVIGATION BAR ELEMENTS - DONT EDIT
    @FXML
    private Circle notificationDot;

    private List<String> notifications = new ArrayList<>();
    @FXML
    private VBox menuPopup = new VBox();
    @FXML
    private VBox profilePopup = new VBox();
    @FXML
    private VBox notificationPopup = new VBox();

    @FXML
    private Button profileButton, bellButton;
    @FXML
    private Tab tttTab;
    @FXML
    private Tab chessTab;
    @FXML
    private Tab goTab;

    @FXML
    private Tab connect4Tab;

    @FXML
    private GridPane tttRankList, chessRankList, goRankList, connect4RankList;


    @FXML
    public void initialize() {
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);

        setBackgroundImage(tttPane, TTT_BCKGRND);
        setBackgroundImage(chessPane, CHESS_BCKGRND);
        setBackgroundImage(goPane, GO_BCKGRND);
        setBackgroundImage(connect4Pane, CONNECT_BCKGRND);
    }

    private void setBackgroundImage(AnchorPane pane, String imagePath) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(getClass().getResource(imagePath).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        pane.setBackground(new Background(backgroundImage));
    }

    @FXML
    private void tictactoe(Event e) {
        if (tttTab.isSelected()) {
            loadLeaderboard(tttRankList, "Tic Tac Toe");
        }
    }

    @FXML
    private void chess(Event e) {
        if (chessTab.isSelected()) {
            loadLeaderboard(chessRankList, "Chess");
        }
    }

    @FXML
    private void go(Event e) {
        if (goTab.isSelected()) {
            loadLeaderboard(goRankList, "Go");
        }
    }

    @FXML
    private void connect4(Event e) {
        if (connect4Tab.isSelected()) {
            loadLeaderboard(connect4RankList, "Connect4");
        }
    }

    private void setLeaderboardHeader(GridPane grid) {
        // Create and style header labels
        Label rankHeader = chnageStyledHeader("Rank");
        Label nameHeader = chnageStyledHeader("Player");
        Label scoreHeader = chnageStyledHeader("ELO Score");

        // Add header labels to the first row
        grid.add(rankHeader, 0, 0);
        grid.add(nameHeader, 1, 0);
        grid.add(scoreHeader, 2, 0);
        GridPane.setHalignment(rankHeader, HPos.CENTER);
        GridPane.setValignment(rankHeader, VPos.CENTER);

        GridPane.setHalignment(nameHeader, HPos.CENTER);
        GridPane.setValignment(nameHeader, VPos.CENTER);

        GridPane.setHalignment(scoreHeader, HPos.CENTER);
        GridPane.setValignment(scoreHeader, VPos.CENTER);

    }

    private Label chnageStyledHeader(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 18; -fx-text-fill: white; -fx-font-weight: bold;");
        return label;
    }

    private void loadLeaderboard(GridPane grid, String gameType) {
        grid.getChildren().clear(); // Clear old leaderboard entries
        setLeaderboardHeader(grid); // Add header first

        grid.setPadding(new Insets(20, 0, 0, 0));
        grid.setAlignment(Pos.TOP_LEFT);
        ColumnConstraints column
                = new ColumnConstraints(200);
        grid.getColumnConstraints().setAll(column);

        grid.setHgap(10);
        grid.setVgap(10);
        try {
            LeaderboardData data = Leaderboard.getLeaderboard(gameType);

            if (data == null) {
                System.out.println("No leaderboard data found for " + gameType);
                return;
            }
            System.out.println("Player IDs: " + data.getPlayerIds());
            System.out.println("Player Scores: " + data.getPlayerScores());

            // Check if we have player data
            if (data.getPlayerIds().isEmpty() || data.getPlayerScores().isEmpty()) {
                System.out.println("No players or scores found for " + gameType);
                return;
            }

            // Add player data to grid
            for (int i = 0; i < data.getlast(); i++) {
                String name = data.getPlayerIds().get(i);
                int score = data.getPlayerScores().get(i);
                putPlayer(grid, name, score, i); // Start from rank 1
            }
        } catch (IOException e) {
            System.err.println("Failed to load leaderboard for " + gameType);
            e.printStackTrace();
        }
    }

    private void putPlayer(GridPane grid, String name, int score, int rank) {
        int currentRowCount = grid.getRowCount();

        if (rank == 0) {
            ImageView medal = new ImageView(new Image(getClass().getResourceAsStream("/icons/first-medal.png")));
            medal.setFitWidth(32);
            medal.setFitHeight(32);
            grid.add(medal, 0, currentRowCount);
            GridPane.setHalignment(medal, HPos.CENTER);
            GridPane.setValignment(medal, VPos.CENTER);
        } else if (rank == 1) {
            ImageView medal = new ImageView(new Image(getClass().getResourceAsStream("/icons/second-medal.png")));
            medal.setFitWidth(32);
            medal.setFitHeight(32);
            grid.add(medal, 0, currentRowCount);
            GridPane.setHalignment(medal, HPos.CENTER);
            GridPane.setValignment(medal, VPos.CENTER);
        } else if (rank == 2) {
            ImageView medal = new ImageView(new Image(getClass().getResourceAsStream("/icons/third-medal.png")));
            medal.setFitWidth(32);
            medal.setFitHeight(32);
            grid.add(medal, 0, currentRowCount);
            GridPane.setHalignment(medal, HPos.CENTER);
            GridPane.setValignment(medal, VPos.CENTER);
        } else {
            Label rankLabel = setTextStyle(String.valueOf(rank + 1));
            grid.add(rankLabel, 0, currentRowCount);
            GridPane.setHalignment(rankLabel, HPos.CENTER);
            GridPane.setValignment(rankLabel, VPos.CENTER);
        }

        Label nameLabel = setTextStyle(name);
        Label scoreLabel = setTextStyle(String.valueOf(score));

        grid.add(nameLabel, 1, currentRowCount);
        grid.add(scoreLabel, 2, currentRowCount);

        GridPane.setHalignment(nameLabel, HPos.CENTER);
        GridPane.setValignment(nameLabel, VPos.CENTER);

        GridPane.setHalignment(scoreLabel, HPos.CENTER);
        GridPane.setValignment(scoreLabel, VPos.CENTER);

    }

    private Label setTextStyle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 20; -fx-text-fill: white;");
        return label;
    }

    //NAVIGATION BAR FUNCTIONALITY

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
