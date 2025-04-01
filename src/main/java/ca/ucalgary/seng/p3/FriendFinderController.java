package ca.ucalgary.seng.p3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
    @FXML
    private Circle notificationDot;

    private List<String> notifications = new ArrayList<>();
    @FXML
    private VBox menuPopup = new VBox();
    @FXML
    private VBox profilePopup = new VBox();
    @FXML
    private VBox notificationPopup = new VBox();


    public Label users_name() {
        return users_Name;
    }

    @FXML
    public void initialize() {
        menuPopup.setVisible(false);
        profilePopup.setVisible(false);
        notificationPopup.setVisible(false);
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

    //NAVIGATION BAR FUNCTIONALITY - DON'T EDIT
    // Called when the menu button is clicked
    @FXML
    private void handleMenuButton() {
        // Toggle visibility of the popup
        menuPopup.setVisible(!menuPopup.isVisible());
        if(notificationPopup.isVisible()) {
            notificationPopup.setVisible(false);
        }
        if(profilePopup.isVisible()) {
            profilePopup.setVisible(false);
        }
    }

    // More event handlers for other buttons, unfinished:
    @FXML
    private void handleProfileButton() {
        // Toggle visibility of the popup
        profilePopup.setVisible(!profilePopup.isVisible());
        if(notificationPopup.isVisible()) {
            notificationPopup.setVisible(false);
        }
        if(menuPopup.isVisible()) {
            menuPopup.setVisible(false);
        }
    }

    @FXML
    private void handleBellButton() {
        // Toggle visibility of the popup
        updateNotificationDot();

        for(String notification: notifications) {
            Button btn = new Button(notification);
            btn.getStyleClass().add("notification-button");
            notificationPopup.getChildren().add(btn);
        }

        notificationPopup.setVisible(!notificationPopup.isVisible());
        if (profilePopup.isVisible()) {
            profilePopup.setVisible(false);
        }
        if (menuPopup.isVisible()) {
            menuPopup.setVisible(false);
        }

        notifications = new ArrayList<>();

    }

    private void updateNotificationDot() {
        notificationDot.setVisible(!notifications.isEmpty());
    }

    @FXML
    private void handleEditProfileButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void handleLogOutButton() {
        Alert logOutVerification = new Alert(Alert.AlertType.CONFIRMATION);
        logOutVerification.setTitle("Log out");
        logOutVerification.setHeaderText("Are you sure you want to log out?");
        ButtonType logOutButton = new ButtonType("Log Out");
        ButtonType cancelButton = ButtonType.CANCEL;

        logOutVerification.getButtonTypes().setAll(cancelButton, logOutButton);
        logOutVerification.showAndWait().ifPresent(response -> {
            if (response == logOutButton) {

                PageNavigator.navigateTo("landing");
                logOutVerification.close();
            }else{
                logOutVerification.close();
            }
        });
    }

    @FXML
    private void handleDashboardButton() {
        PageNavigator.navigateTo("home");
    }

    @FXML
    private void handleLeaderboardButton() {
        PageNavigator.navigateTo("leaderboard_home");
    }

    @FXML
    private void handleFindAPlayerButton() {
        PageNavigator.navigateTo("player_Finder");
    }

    @FXML
    private void handleSettingsButton() {
        PageNavigator.navigateTo("settings");
    }
    @FXML
    private void handleGameRulesButton() {
        //game rules page
    }
    //END OF NAVIGATION BAR FUNCTIONALITY

    @FXML
    void open_Request_Menu(MouseEvent event) {

    }

    @FXML
    void send_Text(MouseEvent event) {

    }

    public void change_List(InputMethodEvent inputMethodEvent) {

    }
}
