package ca.ucalgary.seng.p3.client.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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

    private static final String FILE_NAME = "accounts.csv";
    private static final String CURRENT_SESSION = "session.csv";

    /**
     * Loads data from account into the settings GUI by looking at the users session.csv
     */

    @FXML
    private void initialize(){
        String username;
        File file = new File(CURRENT_SESSION);

        //Set name to current session login
        try (BufferedReader name_Read = new BufferedReader(new FileReader(file))){
            username = name_Read.readLine();
            String[] details = getAccountDetails(username);
            usernameField.setText(username);
            emailField.setText(details[1]);
            currentPassField.setText(details[2]);

        } catch (IOException e){
            // If the profile doesn't exist
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                PageNavigator.navigateTo("landing"); // Navigate to landing page
            }
        });
    }

    @FXML
    private void handleSave() {

        // Validate inputs
        boolean isValid = true;
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String currentPassword = currentPassField.getText();
        String newPassword = newPassField.getText();

        // Basic validation
        if (username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username cannot be empty.");
            isValid = false;
        }

        if (email.isEmpty() || !email.contains("@")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid email address.");
            isValid = false;
        }

        // If user is trying to change password, require current password
        if (!newPassword.isEmpty() && currentPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Current password is required to set a new password.");
            isValid = false;
        }

        if (isValid) {
            System.out.println("Saving profile changes: Email=" + email +
                    ", Bio=" + bioArea.getText() +
                    ", New Password=" + (newPassword.isEmpty() ? "unchanged" : "changed"));

            // Integration team, this should connect to something to update the profile
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");

            // Clear the password fields after successful update
            currentPassField.clear();
            newPassField.clear();
        }
        System.out.println("Saving profile changes: Email=" + emailField.getText() +
                ", Bio=" + bioArea.getText() +
                ", New Password=" + (newPassField.getText().isEmpty() ? "unchanged" : "changed"));
    }

    @FXML
    private void handleDeleteAccount() {

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Account");
        confirmAlert.setHeaderText("Are you sure you want to delete your account?");
        confirmAlert.setContentText("This action cannot be undone. All your data will be permanently deleted !!.");

        //custom buttons for the alert
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirmButton = new ButtonType("Delete Account", ButtonBar.ButtonData.OK_DONE);
        confirmAlert.getButtonTypes().setAll(cancelButton, confirmButton);

        // Style the delete button to have a red background
        Button deleteButton = (Button) confirmAlert.getDialogPane().lookupButton(confirmButton);
        deleteButton.setStyle("-fx-background-color: #ff0f0f; -fx-text-fill: white;");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            // Second confirmation with password for security
            TextInputDialog passwordDialog = new TextInputDialog();
            passwordDialog.setTitle("Confirm with Password");
            passwordDialog.setHeaderText("Please enter your password to confirm account deletion");
            passwordDialog.setContentText("Password:");

            // Configure the password field to hide input
            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Your password: ");
            passwordDialog.getEditor().setVisible(false);
            GridPane grid = new GridPane();
            grid.add(new Label("Password: "), 0, 0);
            grid.add(passwordField, 1, 0);
            passwordDialog.getDialogPane().setContent(grid);

            // Focus the password field when dialog opens
            Platform.runLater(passwordField::requestFocus);

            Optional<String> passwordResult = passwordDialog.showAndWait();

            if (passwordResult.isPresent() && !passwordField.getText().trim().isEmpty()) {
                // Authentication team - Here you have to verify the password against the user's actual password

                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Account Deleted");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your account has been successfully deleted.");
                successAlert.showAndWait();

                // Navigate back to landing page
                PageNavigator.navigateTo("landing");
            } else {
                // If they didn't enter a password or canceled
                Alert cancelAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelAlert.setTitle("Try again");
                cancelAlert.setHeaderText(null);
                cancelAlert.setContentText("Could not delete account! Check if you're entering the correct password!");
                cancelAlert.showAndWait();
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