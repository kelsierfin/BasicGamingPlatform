package ca.ucalgary.seng.p3.client.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LandingController {

    @FXML
    public Label titleLabel;

    @FXML
    private ImageView imageLeft, imageRight, imageBottom;

    @FXML
    private void initialize() {
        animateImage(imageLeft, -300, 0);
        animateImage(imageRight, 300, 0);
        animateImage(imageBottom, 0, 300);
    }


    // AI Assistance Declaration: The following animation logic was developed with AI(ChatGPT) guidance.
    // AI provided recommendations on using JavaFX TranslateTransition and FadeTransition
    // to achieve smoother animations.
    private void animateImage(ImageView image, double fromX, double fromY) {
        image.setOpacity(0);
        image.setTranslateX(fromX);
        image.setTranslateY(fromY);

        TranslateTransition move = new TranslateTransition(Duration.seconds(1.5), image);
        move.setToX(0);
        move.setToY(0);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), image);
        fade.setToValue(1);

        move.play();
        fade.play();
    }

    public void handleSignUpButton() {
        PageNavigator.navigateTo("signUp");
    }

    public void handleLogInButton() {
        PageNavigator.navigateTo("logIn");
    }

    public void handleHomeButton() {
        //stays blank; no navigation to home (already home)
    }

    public void handleAboutButton() {
        PageNavigator.navigateTo("about");
    }

}

