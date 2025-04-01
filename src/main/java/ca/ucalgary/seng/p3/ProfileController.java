package ca.ucalgary.seng.p3;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

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
        System.out.println("Save button clicked");

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
                System.out.println("Delete Account confirmed with password");

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
}