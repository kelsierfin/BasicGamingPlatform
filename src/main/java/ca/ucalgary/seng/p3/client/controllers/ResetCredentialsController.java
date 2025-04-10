package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import ca.ucalgary.seng.p3.server.authentication.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ResetCredentialsController {


    @FXML
    public TextField emailInput;
    String email;


    @FXML
    public void initialize() {
        emailInput.setPromptText("Enter your email address");
    }

    public void handleResetButton() throws IOException {
        email = emailInput.getText();
        String[] credentials = ResetCredentials.findAccountByEmail(ResetCredentials.loadAccounts(), email);
        boolean validEmail = credentials != null;

        if (validEmail) {
            String oneTimeCode = ResetCredentials.generateOneTimeCode(5);
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
            accountMissing.showAndWait().ifPresent(response -> {
                if (response == signUp) {
                    PageNavigator.navigateTo("SignUp");
                }
            });
        }
    }


    public void allowReset() throws IOException {
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

            Optional<ButtonType> response = resetAlert.showAndWait();
            if (response.isPresent() && response.get() == resetButton) {
                String newUsername = usernameField.getText().trim();
                String newPassword = passwordField.getText().trim();

                if (newUsername.isEmpty() && newPassword.isEmpty()) {
                    showWarning("Please enter a new username or password to reset.");
                    continue; // retry
                }

                List<String[]> accounts = ResetCredentials.loadAccounts();
                String[] account = ResetCredentials.findAccountByEmail(accounts, email);

                if (!newUsername.isEmpty()) {
                    if (ResetCredentials.isUsernameTaken(accounts, newUsername)) {
                        showWarning("Username already taken.");
                        continue; // retry
                    }
                    ProfileEditor profileEditor = new ProfileEditor();
                    profileEditor.writeAllStats(account[0],newUsername);
                    account[0] = newUsername;
                }

                if (!newPassword.isEmpty()) {
                    if (!ResetCredentials.isValidPassword(newPassword)) {
                        showWarning("Password must be at least 8 characters, include an uppercase letter and a digit.");
                        continue; // retry
                    }
                    account[1] = PasswordHasher.generateStorablePassword(newPassword);
                }

                ResetCredentials.saveAccounts(accounts);

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Credentials updated successfully!");
                successAlert.showAndWait();

                success = true;
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
