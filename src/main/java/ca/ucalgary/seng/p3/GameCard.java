package ca.ucalgary.seng.p3;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class GameCard extends AnchorPane {
    @FXML
    private ImageView background;

    @FXML
    private Button playButton;

    public void setUrl(String url) {
        background.setImage(new Image(url));
        Rectangle rect = new Rectangle();
        rect.setWidth(background.getFitWidth());
        rect.setHeight(background.getFitHeight());
        rect.setArcWidth(20);
        rect.setArcHeight(20);
        background.setClip(rect);
    }

    public String getUrl() {
        return background.getImage().getUrl();
    }

    public GameCard() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LeaderBoardController.class.getResource("/game_card.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {

        }
    }

    public void setOnPlayButton(EventHandler<ActionEvent> e) {
        playButton.setOnAction(e);
    }

    public EventHandler<ActionEvent> getOnPlayButton() {
        return playButton.getOnAction();
    }

    public void setButtonTitle(String title) {
        playButton.setText(title);
    }

    public String getButtonTitle() {
        return playButton.getText();
    }
}
