package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.awt.event.ActionEvent;

public class ChessController {
    @FXML
    private TextField chat_Type_Field;

    @FXML
    private Button exitButton;

    @FXML
    private Label opponent_Name;

    @FXML
    private ImageView opponent_Profile_pic;

    @FXML
    private Label turnLabel;

    @FXML
    public void initialize() {}

    @FXML
    void handleExit(ActionEvent event) {

    }

    @FXML
    void input_Into_Chat(KeyEvent event) {

    }


    public void handleExit(javafx.event.ActionEvent actionEvent) {
        PageNavigator.navigateTo("startgame_chess");
    }
}
