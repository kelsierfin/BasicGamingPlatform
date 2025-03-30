package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class LeaderBoardHome {
    private static final String CHESS_TITLE = "Chess Leaderboard";
    private static final String CHESS_BCKGRND = "/icons/chess-background.png";
    private static final String GO_TITLE = "Go Leaderboard";
    private static final String GO_BCKGRND = "/icons/go-background.png";
    private static final String CONNECT_TITLE = "Connect 4 Leaderboard";
    private static final String CONNECT_BCKGRND = "/icons/connect4-background.png";
    private static final String TTT_TITLE = "TicTacToe Leaderboard";
    private static final String TTT_BCKGRND = "/icons/tictactoe-background.png";

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button btnPrev, btnNext;

    private final double scrollStep = 0.5; // Adjust sliding speed

    @FXML
    private NavigationBar navBar;

    @FXML
    public void initialize() {
        btnPrev.setOnAction(event -> scrollLeft());
        btnNext.setOnAction(event -> scrollRight());
    }

    @FXML
    private void chess() {
        toChessLeaderBoard();
    }

    @FXML
    private void go() {
        toGoLeaderBoard();
    }

    @FXML
    private void tictactoe() {
        toTTTLeaderBoard();
    }

    @FXML
    private void connect4() {
        toConnect4LeaderBoard();
    }

    private void scrollLeft() {
        double currentScroll = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.max(currentScroll - scrollStep, 0)); // Move left
    }

    private void scrollRight() {
        double currentScroll = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.min(currentScroll + scrollStep, 1)); // Move right
    }

    public static void toChessLeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(CHESS_TITLE, CHESS_BCKGRND), CHESS_TITLE);
    }

    public static void toGoLeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(GO_TITLE, GO_BCKGRND), GO_TITLE);
    }

    public static void toTTTLeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(TTT_TITLE, TTT_BCKGRND), TTT_TITLE);
    }

    public static void toConnect4LeaderBoard() {
        PageNavigator.navigateTo(new LeaderBoardController(CONNECT_TITLE, CONNECT_BCKGRND), CONNECT_TITLE);
    }
}
