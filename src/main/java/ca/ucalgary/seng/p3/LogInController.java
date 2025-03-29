package ca.ucalgary.seng.p3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML
    private Button logInButton;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button backButton;

    @FXML
    private TextField guestUsername;

    @FXML
    private Button guestLogInButton;

    @FXML
    private Hyperlink signUpLink;

    public void initialize() {
    }

    public void handleBackButton() {
        PageNavigator.navigateTo("landing");
    }

    public void handleLogInButton() {
        PageNavigator.navigateTo("home");
    }

    public void handleGuestLogInButton() {
        PageNavigator.navigateTo("home");
    }
    public void handleSignUpLink() {
        PageNavigator.navigateTo("signUp");
    }
}

