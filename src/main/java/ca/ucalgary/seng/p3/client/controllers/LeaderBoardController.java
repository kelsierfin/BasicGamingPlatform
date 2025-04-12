package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.server.leadmatch.Leaderboard;
import ca.ucalgary.seng.p3.server.leadmatch.LeaderboardData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * Example use to create TicTacToe leaderboard page (the same for other games):
 * LeaderBoardController page = new LeaderBoardController("TicTacToe", "/icons/tictactoe-background.png");
 * Scene scene = new Scene(page.root, 900, 600);
 */
public class LeaderBoardController extends AnchorPane {
    String gameType;

    @FXML
    private GridPane rankList;
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
    private Button profileButton, bellButton, menuButton;
    @FXML
    private Button gameRulesButton, settingsButton, findAPlayerButton, leaderboardButton, dashboardButton;
    @FXML
    private Button logOutButton, editProfileButton;



    public LeaderBoardController(String gameType, String background){
        this.gameType= gameType;

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LeaderBoardController.class.getResource("/leaderboard.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setBackground(new Background(new BackgroundImage
                    (new Image(getClass().getResourceAsStream(background)),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,null,
                            new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true, true, false,true))));
        } catch (IOException e) {

        }
        profileButton.setOnAction(e -> handleProfileButton());
        bellButton.setOnAction(e -> handleBellButton());
        menuButton.setOnAction(e -> handleMenuButton());
        dashboardButton.setOnAction(e -> handleDashboardButton());
        leaderboardButton.setOnAction(e -> handleLeaderboardButton());
        findAPlayerButton.setOnAction(e -> handleFindAPlayerButton());
        settingsButton.setOnAction(e -> handleSettingsButton());
        gameRulesButton.setOnAction(e  -> handleGameRulesButton());
        editProfileButton.setOnAction(e -> handleEditProfileButton());
        logOutButton.setOnAction(e -> handleLogOutButton());
        loadData();
    }

    private void loadData() {
        try {
            LeaderboardData data = Leaderboard.getLeaderboard(gameType);

            // Check if the data is null or empty
            if (data == null) {
                System.err.println("Error: Leaderboard data is null.");
                return;
            }
            System.out.println(data.getPlayerScores().toString());
            System.out.println("Leaderboard data loaded: " + data.getPlayerIds().size() + " players found.");

            // Loop through the player data and display them dynamically on the leaderboard
            for (int i = 0; i < data.getlast(); i++) {
                String name = data.getPlayerIds().get(i);
                int playerScore = data.getPlayerScores().get(i);
                System.out.println(i + " " + name + " " + playerScore);
                putPlayer(name, playerScore, i);
            }

        } catch (IOException e) {
            System.err.println("Error loading leaderboard data.");
            e.printStackTrace();  // Print the stack trace to identify the error
        }
    }


    private void putPlayer(String name, Integer playerScore, int rank) {
        // Manually calculate the row index based on the rank
        int lastRow = rank; // Directly use 'rank' as the row index

        // Add rank label
        Label ranking = new Label(String.valueOf(rank + 1));
        ranking.setStyle("-fx-font-size: 20");
        ranking.setTextFill(new Color(1, 1, 1, 1));
        rankList.add(ranking, 0, lastRow); // Column 0 for the rank

        // Add player name dynamically
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 20");
        nameLabel.setTextFill(new Color(1, 1, 1, 1));
        rankList.add(nameLabel, 1, lastRow); // Column 1 for the player name

        // Add score dynamically
        Label pointsLabel = new Label(String.valueOf(playerScore));
        pointsLabel.setStyle("-fx-font-size: 20");
        pointsLabel.setTextFill(new Color(1, 1, 1, 1));
        rankList.add(pointsLabel, 2, lastRow); // Column 2 for the score

        // Optionally, add medals dynamically based on rank
        addMedalsForRank(rank, lastRow);
    }

    private void addMedalsForRank(int rank, int lastRow) {
        String medalUrl = "";
        if (rank == 0) {
            medalUrl = "/icons/first-medal.png";
        } else if (rank == 1) {
            medalUrl = "/icons/second-medal.png";
        } else if (rank == 2) {
            medalUrl = "/icons/third-medal.png";
        }

        if (!medalUrl.isEmpty()) {
            Image medalImage = new Image(getClass().getResourceAsStream(medalUrl));
            rankList.add(new ImageView(medalImage), 3, lastRow); // Column 3 for medals
        }
    }

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
        Alert logOutVerification = new Alert(Alert.AlertType.CONFIRMATION);
        logOutVerification.setTitle("Log out");
        logOutVerification.setHeaderText("Are you sure you want to log out?");
        ButtonType logOutButton = new ButtonType("Log Out");
        ButtonType cancelButton = ButtonType.CANCEL;

        logOutVerification.getButtonTypes().setAll(cancelButton, logOutButton);
        logOutVerification.showAndWait().ifPresent(response -> {
            if (response == logOutButton) {

                PageNavigator.navigateTo("landing");
                logOutVerification.close();
            }else{
                logOutVerification.close();
            }
        });
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

}
