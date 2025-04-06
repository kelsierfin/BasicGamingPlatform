package ca.ucalgary.seng.p3.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class ResetCredentialsController {


    @FXML
    public TextField emailInput;
    String email;

    String password;
    String username;

    @FXML
    public void initialize() {

        emailInput.setPromptText("Enter your email address");
    }

    public void handleRequestButton() {
        email = emailInput.getText();
        boolean validEmail = true; //email is in database or not(false)
        if (validEmail) {
            //TODO: RETRIEVE PASSWORD
            Alert credentials = new Alert(Alert.AlertType.CONFIRMATION);
            credentials.setTitle("Credentials");
            credentials.setHeaderText("These are your account details, save them before exiting.");
            credentials.setContentText("Username: " + username + " Password: "+ password);
            ButtonType logInButton = new ButtonType("Log In");
            ButtonType cancelButton = ButtonType.CANCEL;

            credentials.getButtonTypes().setAll(cancelButton, logInButton);
            credentials.showAndWait().ifPresent(response -> {
                if (response == logInButton) {

                    PageNavigator.navigateTo("logIn");
                    credentials.close();
                } else {
                    credentials.close();
                }
            });
        }else{
            Alert credentials = new Alert(Alert.AlertType.CONFIRMATION);
            credentials.setTitle("Credentials");
            credentials.setHeaderText("Seems like you don't have an account yet.");
            ButtonType signUp = new ButtonType("Create Account");
            ButtonType cancelButton = ButtonType.CANCEL;

            credentials.getButtonTypes().setAll(cancelButton, signUp);
            credentials.showAndWait().ifPresent(response -> {
                if (response == signUp) {

                    PageNavigator.navigateTo("SignUp");
                    credentials.close();
                } else {
                    credentials.close();
                }
            });
        }
    }


    //directs user back to the Home Page
    public void handleHomeButton() {
        PageNavigator.navigateTo("landing");
    }

    public void handleAboutButton() {
        PageNavigator.navigateTo("about");
    }

    public void handleLogInButton(){
        PageNavigator.navigateTo("login");
    }

    public void handleSignUpButton(){
        PageNavigator.navigateTo("signUp");
    }
}
