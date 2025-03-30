package ca.ucalgary.seng.p3;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;

import javax.swing.text.Style;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeController {

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

    private void scrollLeft() {
        double currentScroll = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.max(currentScroll - scrollStep, 0)); // Move left
    }

    private void scrollRight() {
        double currentScroll = scrollPane.getHvalue();
        scrollPane.setHvalue(Math.min(currentScroll + scrollStep, 1)); // Move right
    }

    @FXML
    private void handlePlayChessButton() {
        PageNavigator.navigateTo("startgame_chess");
    }

    @FXML
    private void handlePlayGoButton() {
        PageNavigator.navigateTo("startgame_go");
    }

    @FXML
    private void handlePlayTttButton() {
        PageNavigator.navigateTo("startgame_tictactoe");
    }

    @FXML
    private void handlePlayConnectButton() {
        PageNavigator.navigateTo("startgame_connect4");
    }

}
