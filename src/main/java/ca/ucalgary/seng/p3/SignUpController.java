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
        private TextField usernameInput, emailInput;

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

        private String password;  //final password sent back to authentication
        private String confirmedPassword; //confirmed password
        private String username;  //final password sent back to authentication
        private String email; //final email sent back to authentication
        private boolean credentialsValid; //final verification before signing up.

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

        @FXML
        private void handleVisibilityToggle1Exit() {
                ImageView eyeClosed = new ImageView(new Image(getClass().getResourceAsStream("/icons/hidePassword.png")));
                // Set icon back to closed eye
                visibilityToggle1.setGraphic(eyeClosed);
                eyeClosed.setFitHeight(20);
                eyeClosed.setFitWidth(20);
                // Hide the visible password and copy back the value to password input
                passwordInput1.setText(passwordVisibleField1.getText());
                passwordVisibleField1.setVisible(false);
                passwordVisibleField1.setManaged(false);
                passwordInput1.setVisible(true);
        }

        //NAVIGATION FUNCTIONS THAT HANDLE PAGE EXIT

        //directs user back to the Home Page
        public void handleBackButton() {
                PageNavigator.navigateTo("landing");
        }

        //directs user to the logIn Page after sign up (confirmation log in)
        public void handleSignUpButton() {
                credentialsValid = true;
                emailConfirmation();
                usernameConfirmation();
                passwordConfirmation();
                if (credentialsValid) {
                        System.out.println("Sign Up Successful");
                        PageNavigator.navigateTo("logIn");
                }
                else{
                        System.out.println("Sign Up Failed");
                }
        }

        //directs user to logInPage by link
        public void handleLogInLink() {
                PageNavigator.navigateTo("logIn");
        }


        //HELPER FUNCTIONS

        private void setUsername() {
                username = usernameInput.getText();
        }

        private void setEmail() {
                email = emailInput.getText();
        }

        private void setPassword() {
                password = passwordInput.getText();
        }

        private void setSecondPassword() {
                confirmedPassword = passwordInput1.getText();
        }

        //CONFIRMATION OF CREDENTIALS

        //TODO: ADD FUNCTIONALITY FROM AUTHENTICATION

        /**
         * Verifies password input.
         * @return boolean credentialsValid (true if passwords match, false if input is empty or doesn't match)
         */
        private boolean passwordConfirmation() {
                setSecondPassword();
                setPassword();

                boolean passwordsMatch = !password.isEmpty() && !confirmedPassword.isEmpty() && password.equals(confirmedPassword);

                if (!passwordsMatch) {
                        if (password.isEmpty()) {
                                highlightError(passwordInput, passwordVisibleField);
                                System.out.println("Password Missing");
                                credentialsValid = false;
                        } else if (confirmedPassword.isEmpty()) {
                                highlightError(passwordInput1, passwordVisibleField1);
                                System.out.println("Confirm Password");
                                credentialsValid = false;
                        } else {
                                highlightError(passwordInput1, passwordVisibleField1);
                                System.out.println("Passwords do not match");
                                credentialsValid = false;
                        }
                } else{
                        resetBorderStyle(passwordInput,passwordInput1, passwordVisibleField, passwordVisibleField1);
                }
                return credentialsValid;
        }

        /**
         * Verifies email input.
         * @return boolean credentialsValid (true if email is valid (contains @ and .), false otherwise)
         */
        private boolean emailConfirmation() {
                setEmail();
                boolean validEmail = !email.isEmpty() && email.contains("@") && email.contains(".");

                if (!validEmail) {
                        if (!email.contains("@") || !email.contains(".")) {
                                highlightError(emailInput);
                                System.out.println("Invalid Email");
                                credentialsValid = false;
                        } else if (email.isEmpty()) {
                                highlightError(emailInput);
                                System.out.println("Missing Email");
                                credentialsValid = false;
                        }
                } else {
                        resetBorderStyle(emailInput);
                        return credentialsValid;
                }

                return credentialsValid;
        }

        /**
         * Verifies username input.
         * @return boolean credentialsValid (true if username is valid (username is unique), false otherwise)
         */
        private boolean usernameConfirmation() {
                setUsername();
                boolean validUsername = !username.isEmpty();

                if (!validUsername) {
                        if(username.isEmpty()){
                                highlightError(usernameInput);
                                System.out.println("Missing Username");
                                credentialsValid = false;
                        }
                } else{
                        resetBorderStyle(usernameInput);
                        return credentialsValid;
                }
                return credentialsValid;
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

}
