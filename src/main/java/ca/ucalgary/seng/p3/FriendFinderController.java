package ca.ucalgary.seng.p3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import java.awt.event.InputMethodEvent;
import java.awt.event.MouseEvent;

public class FriendFinderController {
    @FXML
    private Label other_Player_Name;

    @FXML
    private TextArea player_Chat;

    @FXML
    private TextField player_Chat_Input;

    @FXML
    private Button player_Chat_Send;

    @FXML
    private ListView<String> player_List = new ListView<>();

    @FXML
    private TextField player_Search;

    @FXML
    private Label users_Name;

    @FXML
    private VBox request_People;

    public Label users_name() {
        return users_Name;
    }

    /*@FXML
    public void set_Other_Player_Name() {
        other_Player_Name.setText(other_Player_Name.getName());
    } */

    /* @FXML
    public void set_UserName() {
        users_Name.setText(users_Name.getName()); */

    //}

    ObservableList<String> players = FXCollections.observableArrayList("King_Author", "xXHunterXx", "Bob", "guest233539", "TheONe");

    @FXML
    private void name_on_Click_Pop_out() {
        request_People.setVisible(!request_People.isVisible());
    }

    private void load_Opponent_Name(){
        other_Player_Name.setText(players.toString());
    }

    @FXML
    void change_List(InputMethodEvent event) {

    }

    @FXML
    void open_Request_Menu(MouseEvent event) {

    }

    @FXML
    void send_Text(MouseEvent event) {
        // set player text
        String playerText = player_Chat_Input.getText() ;
        if(event.getSource() == player_Chat_Send){
        }
    }


}
