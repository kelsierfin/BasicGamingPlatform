package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class LandingController {
    @FXML
    private Button signUpButton;
    @FXML
    private Button logInButton;
    @FXML
    private Button about;
    @FXML
    private Button faq;
    @FXML
    private Button rules;

    @FXML
    private void initialize() {
        about.setText("ABOUT");
        faq.setText("FAQ");
        rules.setText("RULES");
    }

    public void handleSignUpButton() {
        PageNavigator.navigateTo("signUp");
    }

    public void handleLogInButton() {
        PageNavigator.navigateTo("logIn");
    }

    public void handleAboutButton() {
    }

    public void handleRulesButton() {
    }

    public void handleFaqButton() {
    }
}

