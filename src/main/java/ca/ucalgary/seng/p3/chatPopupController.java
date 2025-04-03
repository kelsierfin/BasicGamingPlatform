package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;

public class chatPopupController {

    @FXML
    private TextArea chat_Display;

    @FXML
    private TextField chat_Input;

    @FXML
    private static Label other_Player_Name;

    @FXML
    private Button send_Button;

    @FXML
    void handleSending() {
        String message = chat_Input.getPromptText();
        if (message.trim().isEmpty()){
            chat_Display.appendText("me:\n" + message + "\n");
            chat_Display.appendText(other_Player_Name + "\n" + "Hi\n");
        }

        chat_Input.clear();
    }

    public static void chatLabelUpdater(String name){
        other_Player_Name.setText(name);
   }


}
