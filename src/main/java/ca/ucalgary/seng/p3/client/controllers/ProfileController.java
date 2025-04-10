package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.server.authentication.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.swing.*;


public class ProfileController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextArea bioArea;

    @FXML
    private PasswordField currentPassField;

    @FXML
    private PasswordField newPassField;

    @FXML
    private Button changeAvatarButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteAccountButton;
    @FXML
    private CheckBox mfaCheckbox;

    private static final String FILE_NAME = "accounts.csv";

    ProfileEditor profileEditor;
    String currentUsername = LogInController.getCurrentUsername();


    /**
     * Loads data from account into the settings GUI by looking at the users session.csv
     */

    @FXML
    private void initialize() throws IOException {
        String[] details = getAccountDetails(LogInController.getCurrentUsername());
        usernameField.setText(LogInController.getCurrentUsername());
        emailField.setText(details[1]);

        boolean isMfaEnabled = UserLogin.isMFAEnabled(LogInController.getCurrentUsername());
        mfaCheckbox.setSelected(isMfaEnabled);
    }

    /**
     * Called when user indicated they want to disable mfa
     * @return true if mfa was disabled
     * @throws IOException
     */
    private boolean handleDisableMfa() throws IOException {
        ProfileEditor profileEditor = new ProfileEditor();
        String email = MultifactorAuthentication.fetchUserEmail(currentUsername);
        String code = MultifactorAuthentication.generateOneTimeCode(6);

        boolean verified = false;
        int attempts = 3;

        while (attempts > 0 && !verified) {
            TextField codeField = new TextField();
            codeField.setPromptText("Enter one-time code");

            VBox content = new VBox(10);
            content.getChildren().add(codeField);

            Alert codeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            codeAlert.setTitle("Disable MFA - Verification");
            codeAlert.setHeaderText("Code sent to: " + email +
                    "\n(For simulation purposes, use code: " + code + ")" +
                    "\nAttempts left: " + attempts);
            codeAlert.getDialogPane().setContent(content);

            ButtonType verify = new ButtonType("Verify Code");
            codeAlert.getButtonTypes().setAll(ButtonType.CANCEL, verify);

            Optional<ButtonType> result = codeAlert.showAndWait();

            if (result.isPresent() && result.get() == verify) {
                String input = codeField.getText().trim();
                verified = input.equals(code);

                if (!verified) attempts--;
            } else {
                return false;
            }
        }

        if (verified) {
            Map<String, String[]> accounts = loadAccounts();
            String[] userDetails = accounts.get(currentUsername);

            if (userDetails.length < 3) {
                userDetails = Arrays.copyOf(userDetails, 3);
            }

            userDetails[2] = "disabled";
            accounts.put(currentUsername, userDetails);
            profileEditor.writeAllAccounts(accounts);
            return true;
        } else {
            Platform.runLater(() -> mfaCheckbox.setSelected(true));
            showAlert(Alert.AlertType.CONFIRMATION, "Error", "Failed to disable MFA.");
            return false;
        }
    }

    /**
     * Called when user indicated they want to enable mfa
     * @return true if mfa was enabled
     * @throws IOException
     */
    private boolean handleEnableMfa() throws IOException {
        ProfileEditor profileEditor = new ProfileEditor();
        String email = MultifactorAuthentication.fetchUserEmail(currentUsername);
        String code = MultifactorAuthentication.generateOneTimeCode(6);

        boolean verified = false;
        int attempts = 3;

        while (attempts > 0 && !verified) {
            TextField codeField = new TextField();
            codeField.setPromptText("Enter one-time code");

            VBox content = new VBox(10);
            content.getChildren().add(codeField);

            Alert codeAlert = new Alert(Alert.AlertType.CONFIRMATION);
            codeAlert.setTitle("Enable MFA - Verification");
            codeAlert.setHeaderText("Code sent to: " + email +
                    "\n(For simulation purposes, use code: " + code + ")" +
                    "\nAttempts left: " + attempts);
            codeAlert.getDialogPane().setContent(content);

            ButtonType verify = new ButtonType("Verify Code");
            codeAlert.getButtonTypes().setAll(ButtonType.CANCEL, verify);

            Optional<ButtonType> result = codeAlert.showAndWait();

            if (result.isPresent() && result.get() == verify) {
                String input = codeField.getText().trim();
                verified = input.equals(code);

                if (!verified) attempts--;
            } else {
                return false;
            }
        }

        if (verified) {
            Map<String, String[]> accounts = loadAccounts();
            String[] userDetails = accounts.get(currentUsername);

            if (userDetails.length < 3) {
                userDetails = Arrays.copyOf(userDetails, 3);
            }

            userDetails[2] = "enabled";
            accounts.put(currentUsername, userDetails);
            profileEditor.writeAllAccounts(accounts);
            return true;
        } else {
            Platform.runLater(() -> mfaCheckbox.setSelected(false));
            showAlert(Alert.AlertType.CONFIRMATION, "Error", "Failed to enable MFA.");
            return false;

        }
    }




    // load accounts from Account.csv
    String[] getAccountDetails(String username) throws IOException {
        Map<String, String[]> accounts = loadAccounts();
        return accounts.get(username);
    }


    @FXML
    private void handleChangeAvatar() {
        System.out.println("Change Avatar button clicked");

    }


    @FXML
    private void handleLogout() {
        LogInController.performLogout();
    }

    @FXML
    private void handleSave() throws IOException {
        profileEditor = new ProfileEditor();
        currentUsername = LogInController.getCurrentUsername();
        StringBuilder errorMessage = new StringBuilder("No changes were applied: ");

        // Validate inputs
        boolean isValid = true;
        String newUsername = usernameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String currentPassword = currentPassField.getText().trim();
        String newPassword = newPassField.getText().trim();

        // Username validation
        if (newUsername.isEmpty()) {
            errorMessage.append("\n\t- Username is empty.");
            isValid = false;
        } else if (loadAccounts().containsKey(newUsername) && !currentUsername.equals(usernameField.getText())) {
            errorMessage.append("\n\t- Username already exists.");
            isValid = false;
        }

        // Email validation
        if (!AccountRegistrationCSV.isValidEmail(newEmail)) {
            errorMessage.append("\n\t- Invalid email address.");
            isValid = false;
        }

        // If user is trying to change password, require current password
        FailedLogin loginManager = FailedLogin.getInstance();
        if (!newPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                errorMessage.append("\n\t- Need to verify current password before changing it.");
                isValid = false;
            } else if (!loginManager.authenticate(currentUsername, currentPassword).isSuccess()) {
                errorMessage.append("\n\t- Wrong current password.");
                isValid = false;
            } else if (!AccountRegistrationCSV.isProperPassword(newPassword)) {
                errorMessage.append("\n\t- New Password is invalid. Password Requirements:\n\t\t- 8+ characters \n\t\t- 1 number \n\t\t- No spaces");
                isValid = false;
            }
        }


        if (isValid) {
            boolean updated = false;
            StringBuilder successMessage = new StringBuilder("Updated:");
            //all new credentials are valid so far. or haven't been changed.

            if(!currentUsername.equals(newUsername)) {
                //1. update username in accounts.csv by calling Profile Editor function
                profileEditor.updateUsername(currentUsername, newUsername);
                //2. update username in sessions.cvs -> delete it and then add the new username
                if (UserLogout.getSessionUsers().contains(currentUsername)) {
                    UserLogout.clearSession(currentUsername);
                    UserLogin.saveSession(usernameField.getText());
                }
                //3.updates currentUsername to new one, resetting it in LogIn Controller
                profileEditor.writeAllStats(currentUsername,newUsername);
                LogInController.setCurrentUsername(newUsername);
                currentUsername = newUsername;
                successMessage.append("\n\t- Username");
                updated = true;
            }

            String oldEmail = loadAccounts().get(currentUsername)[1];
            if (!oldEmail.equals(newEmail)){
                profileEditor.updateEmail(currentUsername, newEmail);
                successMessage.append("\n\t- Email");
                updated = true;
            }

            if (!newPassword.isEmpty() && !currentPassword.equals(newPassword)) {
                profileEditor.updatePassword(currentUsername, newPassword);
                successMessage.append("\n\t- Password");
                updated = true;
            }

            boolean mfaDesiredState = mfaCheckbox.isSelected();
            boolean mfaCurrentState = UserLogin.isMFAEnabled(currentUsername);

            //changes mfa status if user requests
            if (mfaDesiredState != mfaCurrentState) {
                boolean verified = false;

                if (mfaDesiredState) {
                    verified = handleEnableMfa();
                    if (verified) {
                        successMessage.append("\n\t- MFA Enabled");
                        updated = true;
                        mfaCheckbox.setSelected(true);
                    } else {
                        //User canceled or failed verification
                        mfaCheckbox.setSelected(false);
                    }
                } else {
                    verified = handleDisableMfa();
                    if (verified) {
                        successMessage.append("\n\t- MFA Disabled");
                        updated = true;
                        mfaCheckbox.setSelected(false);
                    } else {
                        //User canceled/failed verification
                        mfaCheckbox.setSelected(true);
                    }
                }
            }

            //if anything is updated print confirmation of updates
            if (updated) {
                showAlert(Alert.AlertType.CONFIRMATION, "Success", successMessage.toString());
                currentPassField.clear();
                newPassField.clear();
            }else{
                showAlert(Alert.AlertType.CONFIRMATION, "Success", "No Updates to your credentials have been recorded");
            }

        }else {
        // new input credentials are invalid
            showAlert(Alert.AlertType.ERROR, "Error", errorMessage.toString());
            mfaCheckbox.setSelected(UserLogin.isMFAEnabled(currentUsername));
        }

    }


    @FXML
    private void handleDeleteAccount() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Account");
        confirmAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmAlert.setContentText("This action cannot be undone. " +
                "\nAll your data will be permanently deleted!!");

        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirmButton = new ButtonType("Delete Account", ButtonBar.ButtonData.OK_DONE);
        confirmAlert.getButtonTypes().setAll(cancelButton, confirmButton);

        Button deleteButton = (Button) confirmAlert.getDialogPane().lookupButton(confirmButton);
        deleteButton.setStyle("-fx-background-color: #ff0f0f; -fx-text-fill: white;");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {

            int attemptsLeft = 3;
            boolean success = false;

            while (attemptsLeft > 0 && !success) {

                Dialog<String> passwordDialog = new Dialog<>();
                passwordDialog.setTitle("Confirm with Password");
                passwordDialog.setHeaderText("Enter your password (" + attemptsLeft + " attempt" + (attemptsLeft > 1 ? "s" : "") + " left)");

                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("Your password");

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.add(new Label("Password:"), 0, 0);
                grid.add(passwordField, 1, 0);

                passwordDialog.getDialogPane().setContent(grid);
                passwordDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                Platform.runLater(passwordField::requestFocus);

                passwordDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        return passwordField.getText();
                    }
                    return null;
                });

                Optional<String> passwordResult = passwordDialog.showAndWait();

                if (passwordResult.isPresent() && !passwordResult.get().trim().isEmpty()) {
                    String inputPassword = passwordResult.get().trim();

                    success = AccountDeletion.deleteAccount(currentUsername, inputPassword);

                    if (success) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Account Deleted");
                        successAlert.setContentText("Your account has been successfully deleted.");
                        successAlert.showAndWait();

                        //Delete session and navigate to landing
                        UserLogout.clearSession(currentUsername);
                        PageNavigator.navigateTo("landing");
                    } else {
                        attemptsLeft--;
                        if (attemptsLeft > 0) {
                            Alert tryAgainAlert = new Alert(Alert.AlertType.WARNING);
                            tryAgainAlert.setTitle("Incorrect Password");
                            tryAgainAlert.setHeaderText(null);
                            tryAgainAlert.setContentText("Incorrect password. You have " + attemptsLeft + " attempt" + (attemptsLeft > 1 ? "s" : "") + " left.");
                            tryAgainAlert.showAndWait();
                        }
                    }
                } else {
                    break;
                }
            }

            if (!success && attemptsLeft == 0) {
                Alert failAlert = new Alert(Alert.AlertType.ERROR);
                failAlert.setTitle("Account Deletion Failed");
                failAlert.setHeaderText(null);
                failAlert.setContentText("Too many incorrect attempts. Account deletion canceled.");
                failAlert.showAndWait();
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Loads user accounts from the CSV file.
     */

    private static  Map<String, String[]> loadAccounts() throws IOException {
        Map<String, String[]> accounts = new HashMap<>();
        File file = new File(FILE_NAME);

        // If file does not exist then treat is as if the server failed to connect
        if (!file.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Server failed to connect");
            alert.setHeaderText("The server is having connection issues. Please try again in a little bit");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    PageNavigator.navigateTo("landing"); // Navigate to landing page
                }
            });
            return accounts;
        }

        // If file exists try finding the username
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String username = parts[0];
                    String[] details = new String[]{
                            parts[1], // password
                            parts[2],  // email
                            parts.length > 3 ? parts[3] : "disabled" // mfa status
                    };
                    accounts.put(username, details);
                }
            }

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error retrieving account information");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    PageNavigator.navigateTo("landing"); // Navigate to landing page
                }
            });
        }

        return accounts;
    }

}