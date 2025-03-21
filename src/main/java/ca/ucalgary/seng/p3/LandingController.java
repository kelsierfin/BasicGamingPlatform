package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LandingController {
    @FXML
    private Button myButton;

    @FXML
    private void handleButtonClick() {
        System.out.println("Button clicked!");
    }
}

