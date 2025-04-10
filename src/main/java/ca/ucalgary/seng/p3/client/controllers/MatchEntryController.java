package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class MatchEntryController {
        @FXML
        private Label opponent;
        @FXML
        private Label game;
        @FXML
        private Label result;
        @FXML
        private Button rematchButton;

        public void setMatchData(String opponentText, String gameText, String resultText) {
            opponent.setText("vs. " + opponentText);
            game.setText(gameText);
            result.setText(resultText);
        }

        public Button getRematchButton() {
            return rematchButton;
        }
    }

