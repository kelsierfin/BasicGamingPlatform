package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ProfilePopUp extends Dialog {
    public ProfilePopUp() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProfilePopUp.class.getResource("/profile_pop_up.fxml"));
        loader.setController(this);

        try {
            DialogPane root = loader.load();
            setDialogPane(root);
            setResizable(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setAvatar (String avatar){
        avaPopUp.setImage(new Image(getClass().getResourceAsStream(avatar)));
    }
    public void setUserName (String userName){
        userNamePopUp.setText(userName);
    }
    public void setBio (String bio){
        bioPopUp.setText(bio);
    }
    public void setMatches (String matches){
        matchesPopUp.setText(matches);
    }
    public void setWinnings (String winnings){
        winningsPopUp.setText(winnings);
    } public void setLosses (String losses){
        lossPopUp.setText(losses);
    }


    @FXML
    private ImageView avaPopUp;
    @FXML
    private Label userNamePopUp;
    @FXML
    private Label bioPopUp;
    @FXML
    private Label matchesPopUp;
    @FXML
    private Label winningsPopUp;
    @FXML
    private Label lossPopUp;

}
