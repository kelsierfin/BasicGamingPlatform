package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

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

            // In a real application, this would connect to a service to update the profile
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account? This cannot be undone.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("Delete Account requested - awaiting integration");
                PageNavigator.navigateTo("landing");
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}