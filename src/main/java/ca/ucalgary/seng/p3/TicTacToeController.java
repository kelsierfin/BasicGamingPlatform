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
    private Button btn00, btn01, btn02, btn10, btn11, btn12, btn20, btn21, btn22;
    
    
    @FXML
    private void initialize() {
        if (chatArea != null) {
            chatArea.setEditable(false);
        }
	//Set default text for buttons to indicate they are active
        btn00.setText("");
        btn01.setText("");
        btn02.setText("");
        btn10.setText("");
        btn11.setText("");
        btn12.setText("");
        btn20.setText("");
        btn21.setText("");
        btn22.setText("");
    }

    // Handler for the exit button
    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    
    // Handler for the send button. Appends text from the text field to the chat area
    @FXML
    private void handleSend() {
        String message = chatTextField.getText();
        if (message != null && !message.trim().isEmpty()) {
            chatArea.appendText("Me: " + message + "\n");
            chatTextField.clear();
        }
    }

    @FXML
    private void handleCellClick() {
        // Placeholder for future game logic
    }
}
