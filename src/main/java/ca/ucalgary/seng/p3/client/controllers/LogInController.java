package ca.ucalgary.seng.p3.client.controllers;
import ca.ucalgary.seng.p3.server.authentication.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;


public class LogInController {

    //GUI (FXML) ELEMENTS VISIBLE TO THE USER -> used to collect and display info//

    @FXML
    private Text errorMessage; //displays message to indicate invalid input

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput; //stores invisible password input

    @FXML
    private TextField passwordVisibleField = new TextField();  //stores visible password input

    @FXML
    private Button visibilityToggle = new Button();


    private String password;  //final password sent back to authentication

    private static String username;  //currentUser

    //Variables for MFA Process -> same as MFA Class in Authentication
    private String generatedCode;
    private long codeGenerationTime;
    private int mfaAttempts = 0;
    private static final int MAX_ATTEMPTS = 3;
    private static final long CODE_VALIDITY_DURATION_MS = 3600000;

    private static Map<String, String[]> accounts; //stores all accounts (locally in SignUpController)

    @FXML
    private Text mfaMessageLabel;
    @FXML
    private TextField mfaCodeInput;

    @FXML
    private VBox mfaCard;

    @FXML
    private VBox logInCard;

    public void initialize() {
        checkIfUserIsLocked();
        usernameInput.setPromptText("Username");
        passwordInput.setPromptText("Password");
        passwordVisibleField.setPromptText("Password");

        //listeners for all password fields (to update visible and invisible password field with current input)
        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (passwordVisibleField.isVisible()) {
                passwordVisibleField.setText(newValue);
            }
        });

        passwordVisibleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordInput.isVisible()) {
                passwordInput.setText(newValue);
            }
        });

        mfaCard.setVisible(false);

    }

    //FUNCTIONS THAT HANDLE VISIBILITY OF TEXT IN PASSWORD FIELDS
    @FXML
    private void handleVisibilityToggleEnter() {
        // Set icon to open eye
        ImageView eyeOpen = new ImageView(new Image(getClass().getResourceAsStream("/icons/seePassword.png")));
        visibilityToggle.setGraphic(eyeOpen);
        eyeOpen.setFitHeight(20);
        eyeOpen.setFitWidth(20);
        // Set visible password
        passwordVisibleField.setText(passwordInput.getText());
        passwordVisibleField.setVisible(true);
        passwordVisibleField.setManaged(true);
        passwordInput.setVisible(false);
    }

    @FXML
    private void handleVisibilityToggleExit() {
        // Set icon back to closed eye
        ImageView eyeClosed = new ImageView(new Image(getClass().getResourceAsStream("/icons/hidePassword.png")));
        visibilityToggle.setGraphic(eyeClosed);
        eyeClosed.setFitHeight(20);
        eyeClosed.setFitWidth(20);

        // Hide the visible password and copy back the value to password input
        passwordInput.setText(passwordVisibleField.getText());
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);
        passwordInput.setVisible(true);
    }


    //NAVIGATION FUNCTIONS THAT HANDLE PAGE EXIT

    //directs user back to the Home Page
    public void handleHomeButton() {
        PageNavigator.navigateTo("landing");
    }

    public void handleAboutButton() {
        PageNavigator.navigateTo("about");
    }


    public void handleLogInButton() throws IOException {
        setUsername();
        setPassword();

        // Prevent double-login
        if (UserLogout.getSessionUsers().contains(username)) {
            errorMessage.setText("User is already logged in.");
            return;
        }

        FailedLogin loginManager = FailedLogin.getInstance();
        accounts = AccountRegistrationCSV.loadAccounts();

        if (username.isEmpty()) {
            errorMessage.setText("Enter Username.");
        } else if (!accounts.containsKey(username)) {
            errorMessage.setText("Account does not exist.");
        } else {
            // Check lock before proceeding with login attempt
            FailedLogin.AuthResult result = loginManager.authenticate(username, password);

            if (result.isSuccess()) {
                // Login successful
                if (UserLogin.isMFAEnabled(username)) {
                    logInCard.setVisible(false);
                    mfaCard.setVisible(true);
                    startMFAProcess(username);
                } else {
                    UserLogin.saveSession(username);
                    new CurrentUser(username);
                    PageNavigator.navigateTo("home");
                }
            } else {
                // Login failed
                if (result.getMessage().contains("locked")) {
                    errorMessage.setText(result.getMessage());
                } else {
                    // Handle failed login attempts
                    errorMessage.setText(result.getMessage());
                    System.out.println("Login failed: " + result.getMessage());
                }
                highlightError(usernameInput, passwordInput);
            }
        }
    }



    public void checkIfUserIsLocked() {
        String username = usernameInput.getText();
        if (username == null || username.isEmpty()) return;

        FailedLogin loginManager = FailedLogin.getInstance();
        FailedLogin.AuthResult result = loginManager.authenticate(username, ""); // dummy password

        if (!result.isSuccess() && result.getMessage().contains("locked")) {
            errorMessage.setText(result.getMessage());
        }
    }


    public void handleSubmitMfaCode() {
        String userInput = mfaCodeInput.getText().trim();
        mfaAttempts++;

        if (System.currentTimeMillis() - codeGenerationTime > CODE_VALIDITY_DURATION_MS) {
            mfaMessageLabel.setText("Code expired. Please request a new one.");
            return;
        }
        if (userInput.equals(generatedCode)) {
            mfaMessageLabel.setText("MFA successful!");
            UserLogin.saveSession(username);
            new CurrentUser(username);
            PageNavigator.navigateTo("home");
        } else {
            if (mfaAttempts >= MAX_ATTEMPTS) {
                Alert failedMFA = new Alert(Alert.AlertType.CONFIRMATION);
                failedMFA.setTitle("MFA Failed");
                failedMFA.setHeaderText("MFA failed. Too many incorrect attempts.");
                ButtonType exit= new ButtonType("Exit");

                failedMFA.getButtonTypes().setAll(exit);
                failedMFA.showAndWait().ifPresent(response -> {
                    if (response == exit) {
                        PageNavigator.navigateTo("logIn");
                        failedMFA.close();
                    }else{
                        failedMFA.close();
                    }
                });
            } else {
                mfaMessageLabel.setText("Incorrect code. Attempt " + mfaAttempts + " of " + MAX_ATTEMPTS + ".");
            }
        }
    }


    public void handleGuestLogInButton() {
        username = UserLogin.guestLogin();
        // Navigate to the home page
        PageNavigator.navigateTo("home");
    }
    public void handleSignUpLink() {
        PageNavigator.navigateTo("signUp");
    }

    public void handleResetCredentialsLink(){
        PageNavigator.navigateTo("resetCredentials");
    }

    //HELPER FUNCTIONS

    private void setUsername() {
        username = usernameInput.getText();
    }

    private void setPassword() {
        password = passwordInput.getText();
    }

    //CONFIRMATION OF CREDENTIALS

    public void startMFAProcess(String username) {
        mfaCard.setVisible(true);
        logInCard.setVisible(false);
        String email = MultifactorAuthentication.fetchUserEmail(username);
        if (email == null) {
            mfaMessageLabel.setText("No email found for this user.");
            mfaMessageLabel.setVisible(true);
            return;
        }
        generatedCode = MultifactorAuthentication.generateOneTimeCode(6);
        codeGenerationTime = System.currentTimeMillis();
        mfaMessageLabel.setText("MFA code sent to: " + email + "\nCode: " + generatedCode);
    }

    /**
     * Helper method to reset border styles
     * @param fields text fields with invalid input
     */
    private void resetBorderStyle(TextField... fields) {
        for (TextField field : fields) {
            field.setStyle("-fx-border-color: transparent");
        }
    }

    /**
     * Helper method to highlight error fields
     * @param fields text fields with invalid input
     */
    private void highlightError(TextField... fields) {
        for (TextField field : fields) {
            field.setStyle("-fx-border-color: red");
        }
    }

    public static void performLogout() {
        //TODO: Add this to log out button handlers for the all the other pages, before navigating away from page

        if(!UserLogout.getSessionUsers().isEmpty() && UserLogout.getSessionUsers().contains(username)) {
            Alert logOutVerification = new Alert(Alert.AlertType.CONFIRMATION);
            logOutVerification.setTitle("Logging out User: " + username);
            logOutVerification.setHeaderText("Are you sure you want to log out?");
            ButtonType logOutButton = new ButtonType("Log Out");
            ButtonType cancelButton = ButtonType.CANCEL;
            logOutVerification.getButtonTypes().setAll(cancelButton, logOutButton);
            logOutVerification.showAndWait().ifPresent(response -> {
                if (response == logOutButton) {
                    try {
                        String username = getCurrentUsername();
                        UserLogout.clearSession(username); // backend method
                        PageNavigator.navigateTo("landing");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    logOutVerification.close();
                } else {
                    logOutVerification.close();
                }
            });
        }

    }


    public static String getCurrentUsername(){
        return username;
        //TODO: Make it so that this returns the current username of whichever screen is being interacted with.
        // Shouldn't log out the wrong person.
    }
}

