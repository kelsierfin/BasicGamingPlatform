package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.client.reflection.AuthReflector;
import ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.*;
import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.Response;
import ca.ucalgary.seng.p3.network.ClientSocketService;
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
import java.util.regex.Pattern;

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

    private static final String FILE_NAME = "src/main/resources/database/accounts.csv";
    private static final String CURRENT_SESSION = "src/main/resources/database/session.csv";

    private final AuthReflector authReflector = AuthReflector.getInstance();
    String currentUsername = LogInController.getCurrentUsername();

    // Add email validation method to replace dependency on AccountRegistrationCSV
    boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String[] parts = email.split("@");
        return parts.length == 2 &&
                !parts[0].isEmpty() &&
                !parts[1].isEmpty();
    }

    // Add password validation method to replace dependency on AccountRegistrationCSV
    private boolean isProperPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        return password.length() >= 8 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*") &&
                !password.contains(" ");
    }

    // Add method to generate one-time code to replace dependency on MultifactorAuthentication
    private String generateOneTimeCode(int length) {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            codeBuilder.append(random.nextInt(10));
        }
        return codeBuilder.toString();
    }

    /**
     * Loads data from account into the settings GUI by looking at the users session.csv
     */
    @FXML
    private void initialize() throws IOException {
        // Create directories and session file if they don't exist
        File sessionFile = new File(CURRENT_SESSION);

        // Create parent directories if needed
        if (sessionFile.getParentFile() != null && !sessionFile.getParentFile().exists()) {
            sessionFile.getParentFile().mkdirs();
        }

        // Try to create session file if it doesn't exist
        if (!sessionFile.exists()) {
            try {
                sessionFile.createNewFile();
                System.out.println("Created session file at: " + sessionFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Could not create session file: " + e.getMessage());
            }
        }

        // Set name to current session login
        try {
            CurrentUser currentUser = CurrentUser.getInstance(LogInController.getCurrentUsername());

            // Check if we have a username before trying to get account details
            if (LogInController.getCurrentUsername() == null || LogInController.getCurrentUsername().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not Logged In");
                alert.setHeaderText("You are not currently logged in");
                alert.setContentText("Please log in to view your profile.");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        PageNavigator.navigateTo("logIn");
                    }
                });
                return;
            }

            // Get profile using AuthReflector
            ProfileResult profileResult = authReflector.getUserProfile(LogInController.getCurrentUsername());

            // Add null check for profile data
            if (profileResult.isSuccess()) {
                usernameField.setText(LogInController.getCurrentUsername());
                emailField.setText(profileResult.getEmail());
                mfaCheckbox.setSelected(profileResult.isMfaEnabled());
            } else {
                // Handle the case where user details can't be found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Profile Error");
                alert.setHeaderText("Could not find user profile information");
                alert.setContentText("Your profile information could not be loaded. You may need to log in again.");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        PageNavigator.navigateTo("landing"); // Navigate to landing page
                    }
                });
            }
        } catch (Exception e) {
            // If any error occurs (IOException or otherwise)
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error retrieving account information");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    PageNavigator.navigateTo("landing"); // Navigate to landing page
                }
            });
        }
    }

    @FXML
    private void handleSave() throws IOException {
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
        }

        // Email validation
        if (!isValidEmail(newEmail)) {
            errorMessage.append("\n\t- Invalid email address.");
            isValid = false;
        }

        // If user is trying to change password, require current password
        if (!newPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                errorMessage.append("\n\t- Need to verify current password before changing it.");
                isValid = false;
            } else {
                // Verify current password using AuthReflector
                LoginResult loginResult = authReflector.login(currentUsername, currentPassword);
                if (!loginResult.isSuccess()) {
                    errorMessage.append("\n\t- Wrong current password.");
                    isValid = false;
                } else if (!isProperPassword(newPassword)) {
                    errorMessage.append("\n\t- New Password is invalid. Password Requirements:\n\t\t- 8+ characters \n\t\t- 1 number \n\t\t- No spaces");
                    isValid = false;
                }
            }
        }

        if (isValid) {
            boolean updated = false;
            StringBuilder successMessage = new StringBuilder("Updated:");

            // Update username if changed
            if(!currentUsername.equals(newUsername)) {
                ProfileResult result = authReflector.updateUsername(currentUsername, newUsername);
                if (result.isSuccess()) {
                    LogInController.setCurrentUsername(newUsername);
                    currentUsername = newUsername;
                    successMessage.append("\n\t- Username");
                    updated = true;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update username: " + result.getMessage());
                    return;
                }
            }

            // Update email if changed
            ProfileResult profileResult = authReflector.getUserProfile(currentUsername);
            String oldEmail = profileResult.getEmail();
            if (!oldEmail.equals(newEmail)) {
                ProfileResult result = authReflector.updateEmail(currentUsername, newEmail);
                if (result.isSuccess()) {
                    successMessage.append("\n\t- Email");
                    updated = true;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update email: " + result.getMessage());
                    return;
                }
            }

            // Update password if changed
            if (!newPassword.isEmpty() && !currentPassword.equals(newPassword)) {
                ProfileResult result = authReflector.updatePassword(currentUsername, newPassword);
                if (result.isSuccess()) {
                    successMessage.append("\n\t- Password");
                    updated = true;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to update password: " + result.getMessage());
                    return;
                }
            }

            // Update MFA status if changed
            boolean mfaDesiredState = mfaCheckbox.isSelected();
            profileResult = authReflector.getUserProfile(currentUsername);
            boolean mfaCurrentState = profileResult.isMfaEnabled();

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
            } else {
                showAlert(Alert.AlertType.CONFIRMATION, "Success", "No Updates to your credentials have been recorded");
            }

        } else {
            // new input credentials are invalid
            showAlert(Alert.AlertType.ERROR, "Error", errorMessage.toString());
            // Reset MFA checkbox to actual state
            ProfileResult profileResult = authReflector.getUserProfile(currentUsername);
            mfaCheckbox.setSelected(profileResult.isMfaEnabled());
        }
    }

    private boolean handleDisableMfa() throws IOException {
        String email = authReflector.getUserProfile(currentUsername).getEmail();
        String code = generateOneTimeCode(6);

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
            // Use AuthReflector to modify MFA status
            ProfileResult result = authReflector.toggleMfa(currentUsername, false);
            return result.isSuccess();
        } else {
            Platform.runLater(() -> mfaCheckbox.setSelected(true));
            showAlert(Alert.AlertType.CONFIRMATION, "Error", "Failed to disable MFA.");
            return false;
        }
    }

    private boolean handleEnableMfa() throws IOException {
        String email = authReflector.getUserProfile(currentUsername).getEmail();
        String code = generateOneTimeCode(6);

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
            // Use AuthReflector to modify MFA status
            ProfileResult result = authReflector.toggleMfa(currentUsername, true);
            return result.isSuccess();
        } else {
            Platform.runLater(() -> mfaCheckbox.setSelected(false));
            showAlert(Alert.AlertType.CONFIRMATION, "Error", "Failed to enable MFA.");
            return false;
        }
    }
    @FXML
    private void handleLogout() {
        // Ask for confirmation
        Alert confirmLogout = new Alert(Alert.AlertType.CONFIRMATION);
        confirmLogout.setTitle("Logout");
        confirmLogout.setHeaderText("Are you sure you want to logout?");
        confirmLogout.setContentText("You will be returned to the login screen.");

        Optional<ButtonType> result = confirmLogout.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Get the current username
                String username = LogInController.getCurrentUsername();

                // Call the logout method through the AuthReflector
                ca.ucalgary.seng.p3.server.authentication.reflection.AuthenticationService.LogoutResult logoutResult =
                        authReflector.logout(username);

                if (logoutResult.isSuccess()) {
                    // Clear current user session
                    LogInController.setCurrentUsername(null);

                    // Navigate to landing page
                    PageNavigator.navigateTo("landing");
                } else {
                    // Show error if logout failed
                    showAlert(Alert.AlertType.ERROR, "Logout Failed", logoutResult.getMessage());
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Logout Error", "An error occurred during logout: " + e.getMessage());
            }
        }
    }
    @FXML
    private void handleChangeAvatar() {
        // Future implementation for avatar changing
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature not available");
        alert.setHeaderText(null);
        alert.setContentText("Avatar changing functionality is not yet implemented.");
        alert.showAndWait();
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

                    // Use AuthReflector to delete account
                    AccountDeletionResult deletionResult = authReflector.deleteAccount(currentUsername, inputPassword);
                    success = deletionResult.isSuccess();

                    if (success) {
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Account Deleted");
                        successAlert.setContentText("Your account has been successfully deleted.");
                        successAlert.showAndWait();

                        // Navigate to landing
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
    private static Map<String, String[]> loadAccounts() throws IOException {
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