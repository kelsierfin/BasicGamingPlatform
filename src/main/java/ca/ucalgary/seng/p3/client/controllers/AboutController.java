package ca.ucalgary.seng.p3.client.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AboutController {
    @FXML
    private VBox card1, card2, card3;
    @FXML
    public void initialize() {
        // Call the animate method for each card with their start and end positions.
        // Animate card1 with no delay, card2 with a delay, and card3 with a further delay.
        animate(card1, -100, 0, 0.5);
        animate(card2, -100, 0, 0.5);
        animate(card3, -100, 0, 0.5);
    }

    // AI Assistance Declaration: The following animation logic was developed with AI(ChatGPT) guidance.
   // AI provided recommendations on using JavaFX TranslateTransition and FadeTransition
   // to achieve smoother animations.
    private void animate(VBox card, double fromX, double fromY, double delayInSeconds) {
        card.setOpacity(0);  // Make the card initially invisible
        card.setTranslateX(fromX);
        card.setTranslateY(fromY);

        // Create the TranslateTransition to move the card
        TranslateTransition move = new TranslateTransition(Duration.seconds(1.5), card);
        move.setToX(0);
        move.setToY(0);

        // Create the FadeTransition to fade the card in
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), card);
        fade.setToValue(1);

        // Create a PauseTransition to delay the start of the animation
        PauseTransition delay = new PauseTransition(Duration.seconds(delayInSeconds));

        // Play the animations after the delay
        delay.setOnFinished(event -> {
            move.play();
            fade.play();
        });

        // Start the delay
        delay.play();
    }


    //directs user back to the Home Page
    public void handleHomeButton() {
        PageNavigator.navigateTo("landing");
    }

    public void handleAboutButton() {
        PageNavigator.navigateTo("about");
    }

    public void handleLogInButton(){
        PageNavigator.navigateTo("login");
    }

    public void handleSignUpButton(){
        PageNavigator.navigateTo("signUp");
    }
}
