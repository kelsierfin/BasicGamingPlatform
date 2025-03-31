package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class LogInController {

    @FXML
    Text errorMessage;

    @FXML
    private TextField usernameInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private TextField passwordVisibleField = new TextField();

    @FXML
    private Button visibilityToggle = new Button();

    private String password;  //final password sent back to authentication
    private String username;  //final password sent back to authentication

    private boolean credentialsValid;
    StringBuilder errorText;


    public void initialize() {
        usernameInput.setPromptText("Username");
        passwordInput.setPromptText("Password");
        passwordVisibleField.setPromptText("Password");

        //listeners for all password fields (to update visible and invisible password field with current input)
        passwordInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (passwordVisibleField.isVisible()) {
                passwordVisibleField.setText(newValue);
            }
        });

        passwordVisibleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!passwordInput.isVisible()) {
                passwordInput.setText(newValue);
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


    //NAVIGATION FUNCTIONS THAT HANDLE PAGE EXIT

    //directs user back to the Home Page
    public void handleHomeButton() {
        PageNavigator.navigateTo("landing");
    }

    public void handleAboutButton() {
        PageNavigator.navigateTo("about");
    }


    public void handleLogInButton() {
        errorText =  new StringBuilder();
        credentialsValid = true;
        usernameConfirmation();
        passwordConfirmation();
        if (credentialsValid) {
            System.out.println("Log In Successful");
            PageNavigator.navigateTo("home");
        }
        else{
            errorMessage.setText(errorText.toString());
            System.out.println("Log In Failed");
        }
    }

    public void handleGuestLogInButton() {
        PageNavigator.navigateTo("home");
    }
    public void handleSignUpLink() {
        PageNavigator.navigateTo("signUp");
    }

    public void handleResetCredentialsLink(){
        PageNavigator.navigateTo("resetCredentials");
    }

    //HELPER FUNCTIONS

    private void setUsername() {
        username = usernameInput.getText();
    }

    private void setPassword() {
        password = passwordInput.getText();
    }

    //CONFIRMATION OF CREDENTIALS

    //TODO: ADD FUNCTIONALITY FROM AUTHENTICATION

    /**
     * Verifies password input.
     * @return boolean credentialsValid (true if passwords match, false if input is empty or doesn't match)
     */
    private boolean passwordConfirmation() {
        setPassword();

        boolean passwordsValid = !password.isEmpty(); //add other conditions later

        if (!passwordsValid) {
            if (password.isEmpty()) {
                highlightError(passwordInput, passwordVisibleField);
                System.out.println("Missing Password.");
                errorText.append(" Missing Password.");
                credentialsValid = false;
            }
        } else{
            resetBorderStyle(passwordInput, passwordVisibleField);
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
                System.out.println("Missing Username.");
                errorText.append(" Missing Username.");
                credentialsValid = false;
            }
        } else{
            resetBorderStyle(usernameInput);
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

