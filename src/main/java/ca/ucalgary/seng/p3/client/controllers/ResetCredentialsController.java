package ca.ucalgary.seng.p3.client.controllers;
import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.Response;
import ca.ucalgary.seng.p3.network.ClientSocketService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.Random;

public class ResetCredentialsController {

    @FXML
    public TextField emailInput;
    String email;

    @FXML
    public void initialize() {
        emailInput.setPromptText("Enter your email address");
    }

    public void handleResetButton() {
        email = emailInput.getText();

        // Create a request to check if the email exists
        Request checkEmailRequest = new Request("checkEmail", "", "", email);
        Response response = ClientSocketService.getInstance().sendRequest(checkEmailRequest);

        boolean validEmail = response.isSuccess();

        if (validEmail) {
            String oneTimeCode = generateOneTimeCode(5);
            boolean verified = false;
            int attempts = 3;

            while (attempts > 0 && !verified) {
                TextField codeField = new TextField();
                codeField.setPromptText("Enter one-time code");

                VBox content = new VBox(10);
                content.getChildren().addAll(codeField);

                Alert codeAlert = new Alert(Alert.AlertType.CONFIRMATION);
                codeAlert.setTitle("Verifying credentials reset");
                codeAlert.setHeaderText("Code sent to: " + email + "\n(For simulation purposes, use code: " + oneTimeCode + ")\nAttempts left: " + attempts);
                codeAlert.getDialogPane().setContent(content);

                ButtonType verify = new ButtonType("Verify Code");
                codeAlert.getButtonTypes().setAll(ButtonType.CANCEL, verify);

                Optional<ButtonType> result = codeAlert.showAndWait();
                if (result.isPresent() && result.get() == verify) {
                    String input = codeField.getText().trim();
                    verified = input.equals(oneTimeCode);

                    if (!verified) {
                        attempts--;
                    }
                } else {
                    return;
                }
            }

            if (verified) {
                allowReset();
            } else {
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setTitle("Verification Failed");
                fail.setHeaderText("All verification attempts used. Please try again later.");
                fail.showAndWait();
            }
        } else {
            Alert accountMissing = new Alert(Alert.AlertType.CONFIRMATION);
            accountMissing.setTitle("Credentials");
            accountMissing.setHeaderText("No account found with this email.");
            ButtonType signUp = new ButtonType("Create Account");
            accountMissing.getButtonTypes().setAll(ButtonType.CANCEL, signUp);
            accountMissing.showAndWait().ifPresent(buttonResponse -> {
                if (buttonResponse == signUp) {
                    PageNavigator.navigateTo("SignUp");
                }
            });
        }
    }

    public void allowReset() {
        boolean success = false;

        while (!success) {
            Alert resetAlert = new Alert(Alert.AlertType.CONFIRMATION);
            resetAlert.setTitle("Reset Credentials");
            resetAlert.setHeaderText("Enter new username and/or password");

            TextField usernameField = new TextField();
            usernameField.setPromptText("New Username");

            TextField passwordField = new TextField();
            passwordField.setPromptText("New Password");

            VBox content = new VBox(10);
            content.getChildren().addAll(usernameField, passwordField);
            resetAlert.getDialogPane().setContent(content);

            ButtonType resetButton = new ButtonType("Reset");
            resetAlert.getButtonTypes().setAll(ButtonType.CANCEL, resetButton);

            Optional<ButtonType> dialogResponse = resetAlert.showAndWait();
            if (dialogResponse.isPresent() && dialogResponse.get() == resetButton) {
                String newUsername = usernameField.getText().trim();
                String newPassword = passwordField.getText().trim();

                if (newUsername.isEmpty() && newPassword.isEmpty()) {
                    showWarning("Please enter a new username or password to reset.");
                    continue; // retry
                }

                // Create a request to reset credentials
                Request resetRequest = new Request(
                        "resetCredentials",
                        newUsername.isEmpty() ? null : newUsername,
                        newPassword.isEmpty() ? null : newPassword,
                        email
                );

                Response response = ClientSocketService.getInstance().sendRequest(resetRequest);

                if (response.isSuccess()) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Credentials updated successfully!");
                    successAlert.showAndWait();

                    success = true;
                } else {
                    if (response.getMessage().contains("username")) {
                        showWarning("Username already taken.");
                    } else if (response.getMessage().contains("password")) {
                        showWarning("Password must be at least 8 characters, include an uppercase letter and a digit.");
                    } else {
                        showWarning(response.getMessage());
                    }
                }
            } else {
                // User cancelled reset
                break;
            }
        }
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    // Helper method to generate one-time code (moved from MultifactorAuthentication to avoid direct dependency)
    private String generateOneTimeCode(int length) {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        return codeBuilder.toString();
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