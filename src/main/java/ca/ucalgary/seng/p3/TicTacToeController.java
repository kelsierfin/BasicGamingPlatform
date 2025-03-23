package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TicTacToeController {

    @FXML
    private Button exitButton;
    
    @FXML
    private Button sendButton;
    
    @FXML
    private TextField chatTextField;
    
    @FXML
    private TextArea chatArea;
    
    @FXML
    private void initialize() {
        // Set the chat area as non-editable.
        if (chatArea != null) {
            chatArea.setEditable(false);
        }
    }
    
    // Handler for the exit button. Closes the current stage.
    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    // Handler for the send button. Appends text from the text field to the chat area.
    @FXML
    private void handleSend() {
        String message = chatTextField.getText();
        if (message != null && !message.trim().isEmpty()) {
            chatArea.appendText("Me: " + message + "\n");
            chatTextField.clear();
        }
    }
}
