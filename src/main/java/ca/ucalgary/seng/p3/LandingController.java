package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.io.IOException;

public class LandingController {
    @FXML
    private Button signUpButton;
    @FXML
    private Button loginButton;
    @FXML
    private Text about;
    @FXML
    private Text faq;
    @FXML
    private Text rules;


    @FXML
    private void initialize() {
        about.setText("ABOUT");
        faq.setText("FAQ");
        rules.setText("RULES");
    }


}

