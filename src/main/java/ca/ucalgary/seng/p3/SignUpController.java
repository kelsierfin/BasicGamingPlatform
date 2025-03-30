package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SignUpController {

        @FXML
        private Button signUpButton;

        @FXML
        private TextField usernameInput;

        @FXML
        private TextField emailInput;

        @FXML
        private PasswordField passwordInput, passwordInput1;

        @FXML
        private TextField passwordVisibleField = new TextField();

        @FXML
        private TextField passwordVisibleField1 = new TextField();

        @FXML
        private Button visibilityToggle = new Button();

        @FXML
        private Button visibilityToggle1 = new Button();

        @FXML
        private Button backButton;

        @FXML
        private Hyperlink logInLink;

        public void initialize() {
                // Sets prompt text of all input fields
                usernameInput.setPromptText("Username");
                emailInput.setPromptText("Email");
                passwordInput.setPromptText("Password");
                passwordInput1.setPromptText("Confirm Password");
                passwordVisibleField.setPromptText("Password");
                passwordVisibleField1.setPromptText("Confirm Password");

                //listeners for all password fields (to update visible and invisible password field with current input)
                passwordInput.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (passwordVisibleField.isVisible()) {
                                passwordVisibleField.setText(newValue);
                        }
                });
                passwordInput1.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (passwordVisibleField1.isVisible()) {
                                passwordVisibleField1.setText(newValue);
                        }
                });
                passwordVisibleField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!passwordInput.isVisible()) {
                                passwordInput.setText(newValue);
                        }
                });
                passwordVisibleField1.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!passwordInput1.isVisible()) {
                                passwordInput1.setText(newValue);
                        }
                });
        }

        //These methods handle the visibility toggle features for both password filed
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

        // This method will handle the visibility toggle for the first password field when mouse exits
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

        // This method will handle the visibility toggle for the second password field
        @FXML
        private void handleVisibilityToggle1Enter() {
                // Set icon to open eye
                ImageView eyeOpen = new ImageView(new Image(getClass().getResourceAsStream("/icons/seePassword.png")));
                visibilityToggle1.setGraphic(eyeOpen);
                eyeOpen.setFitHeight(20);
                eyeOpen.setFitWidth(20);
                // Set visible password
                passwordVisibleField1.setText(passwordInput1.getText());
                passwordVisibleField1.setVisible(true);
                passwordVisibleField1.setManaged(true);
                passwordInput1.setVisible(false);
        }

        // This method will handle the visibility toggle for the second password field when mouse exits
        @FXML
        private void handleVisibilityToggle1Exit() {
                // Set icon back to closed eye
                ImageView eyeClosed = new ImageView(new Image(getClass().getResourceAsStream("/icons/hidePassword.png")));
                visibilityToggle1.setGraphic(eyeClosed);
                eyeClosed.setFitHeight(20);
                eyeClosed.setFitWidth(20);
                // Hide the visible password and copy back the value to password input
                passwordInput1.setText(passwordVisibleField1.getText());
                passwordVisibleField1.setVisible(false);
                passwordVisibleField1.setManaged(false);
                passwordInput1.setVisible(true);
        }

        //directs user back to the Home Page
        public void handleBackButton() {
                PageNavigator.navigateTo("landing");
        }

        //directs user to the logIn Page after sign up (confirmation log in)
        public void handleSignUpButton() {
                PageNavigator.navigateTo("logIn");
        }

        //directs user to logInPage by link
        public void handleLogInLink() {
                PageNavigator.navigateTo("logIn");
        }
}
