package ca.ucalgary.seng.p3.client.controllers;
import ca.ucalgary.seng.p3.server.authentication.AccountRegistrationCSV;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Map;


public class SignUpController {

    //GUI (FXML) ELEMENTS VISIBLE TO THE USER -> used to collect and display info//

    @FXML
    private Text errorMessage; //displays message to indicate invalid input

    @FXML
    private TextField usernameInput, emailInput;

    @FXML
    private PasswordField passwordInput, passwordInput1;

    @FXML
    private TextField passwordVisibleField = new TextField();  //stores visible password input

    @FXML
    private TextField passwordVisibleField1 = new TextField();   //stores visible confirmed password input
    @FXML
    private Button visibilityToggle = new Button();    //used to toggle visibility of first password field

    @FXML
    private Button visibilityToggle1 = new Button();  //used to toggle visibility of second password field

    //VARIABLES USED TO STORE & AUTHENTICATE USER INPUT//

    private String password;  //used to store current input in password field (call setPassword(), to get current input, then use)

    private String confirmedPassword; //used to store current input in second password field (call setSecondPassword(), to get current input, then use)

    private String username;  //used to store current input in username field (call setUsername(), to get current input, then use)

    private String email; //used to store current input in email field (call setEmail(), to get current input, then use)

    private boolean credentialsValid; //true if credentials passed verification, false if not

    StringBuilder errorText;   //specific error (depending on input) appended

    private static Map<String, String[]> accounts; //stores all accounts (locally in SignUpController)


    /**Called everytime user navigates to sign up page
     * Sets prompt text in input fields
     */
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

    //FUNCTIONS THAT HANDLE VISIBILITY OF TEXT IN PASSWORD FIELD//

    /** Makes password visible (changes password field to text field), everytime user moves mouse
     * to the eye button.
     * **/
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

    /** Makes password invisible (changes text field back to password field), everytime user moves mouse
     * away from the eye button.
     * **/
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

    /** Makes password in second password field visible (changes password field to text field), everytime user moves mouse
     * to the eye button.
     * **/
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

    /** Makes password in second password field visible (changes password field to text field), everytime user moves mouse
     * away from the eye button.
     * **/
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

    //FUNCTIONS THAT HANDLE BUTTON/LINK CLICKS//

    /**Directs user back to the Landing Page*
     * */
    public void handleHomeButton() {
        PageNavigator.navigateTo("landing");
    }

    /**Directs user to the 'About' Page*
     * */
    public void handleAboutButton() {
        PageNavigator.navigateTo("about");
    }

    /**ACCOUNT VERIFICATION AND CREATION: INITIALIZED AT "SIGN UP" BUTTON CLICK
     * 1. loads accounts
     * 2. confirmation of email, username, password and password  (error messages appended to stringbuilder)
     * 3. after conformation of credentials account is saved
     * 4. user is directed to log In Screen, where they can log in with new credentials
     * **/
    public void handleSignUpButton() throws IOException {
        errorText = new StringBuilder();
        credentialsValid = true;

        accounts = AccountRegistrationCSV.loadAccounts();

        emailConfirmation();   //changes credentialsValid to false if email is invalid or empty
        usernameConfirmation();    //changes credentialsValid to false if username is invalid or empty
        passwordConfirmation();      //changes credentialsValid to false if password is invalid

        //if credentials are valid, account is saved, and user is directed to log In
        if (credentialsValid) {
            AccountRegistrationCSV.saveAccount(username, password, email);
            System.out.println("Sign Up Successful");
            PageNavigator.navigateTo("logIn");
        } else {
            //if credentials are in valid, error message prints user instructions
            errorMessage.setText(errorText.toString());
            System.out.println("Sign Up Failed");
        }
    }

    /**Directs user to the 'LogIn' Page*
     * */
    public void handleLogInLink() {
        PageNavigator.navigateTo("logIn");
    }


    //HELPER FUNCTIONS - CONFIRMATION OF CREDENTIALS

    /**
     * Verifies password input.
     * @return boolean credentialsValid (true if passwords match, false if input is empty or doesn't match)
     */
    private boolean passwordConfirmation() {
        setSecondPassword();
        setPassword();

        boolean validPassword = AccountRegistrationCSV.isProperPassword(password);
        boolean passwordsMatch = password.equals(confirmedPassword);

        if (validPassword && !passwordsMatch) {
            highlightError(passwordInput1, passwordVisibleField1);
            System.out.println("Passwords do not match.");
            errorText.append(" Passwords do not match.");
            credentialsValid = false;
        } else if (!validPassword) {
            highlightError(passwordInput, passwordVisibleField);
            System.out.println("Password must include at least 8 characters, 1 number, 1 capital letter, and no spaces");
            errorText.append(" Password must include at least 8 characters, 1 number, 1 capital letter, and no spaces");
            credentialsValid = false;
        }
        else {
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
        boolean validEmail = AccountRegistrationCSV.isValidEmail(email);

        if (email.isEmpty()) {
            highlightError(emailInput);
            System.out.println("Missing Email.");
            errorText.append(" Missing Email.");
            credentialsValid = false;
        } else if (!validEmail) {
            highlightError(emailInput);
            System.out.println("Invalid Email.");
            errorText.append(" Invalid Email.");
            credentialsValid = false;
        } else {
            resetBorderStyle(emailInput);
        }

        return credentialsValid;
    }

    /**
     * Verifies username input.
     * @return boolean credentialsValid (true if username is valid (username is unique), false otherwise)
     */
    private boolean usernameConfirmation() {
        setUsername();
        boolean validUsername = !accounts.containsKey(username);

        if(username.isEmpty()){
            highlightError(usernameInput);
            System.out.println("Missing Username.");
            errorText.append(" Missing Username.");
            credentialsValid = false;
        } else if (!validUsername) {
            highlightError(usernameInput);
            System.out.println("Username already exists.");
            errorText.append("Username already exists");
            credentialsValid = false;
        } else{
            resetBorderStyle(usernameInput);
        }
        return credentialsValid;
    }

    //HELPER FUNCTIONS - SETTERS & BORDER STYLE CONTROLLERS

    /** Sets username variable to input currently in the filed
     * **/
    private void setUsername() {
        username = usernameInput.getText();
    }

    /** Sets email variable to input currently in the filed
     * **/
    private void setEmail() {
        email = emailInput.getText();
    }

    /** Sets password variable to input currently in the filed
     * **/
    private void setPassword() {
        password = passwordInput.getText();
    }

    /** Sets confirmedPassword variable to input currently in the filed
     * **/
    private void setSecondPassword() {
        confirmedPassword = passwordInput1.getText();
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
