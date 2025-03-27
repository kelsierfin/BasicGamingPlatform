package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

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

    @FXML
    private void initialize() {

    }

}
