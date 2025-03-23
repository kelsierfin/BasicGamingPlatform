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
    private Button backButton;

    @FXML
    private void handleBack() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/settings.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
    }

    @FXML
    private void handleChangeAvatar() {
        // Placeholder for avatar change functionality
        System.out.println("Change Avatar button clicked");
    }

    @FXML
    private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Notify the Authentication team to handle actual logout logic
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                stage.close();
            }
        });
    }
}