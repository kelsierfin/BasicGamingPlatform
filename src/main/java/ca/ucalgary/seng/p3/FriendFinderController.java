package ca.ucalgary.seng.p3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public class FriendFinderController {

    @FXML
    private TextArea player_Chat;

    @FXML
    private TextField player_Chat_Input;

    @FXML
    private Button player_Chat_Send;

    @FXML
    private ListView<String> player_List = new ListView<>();
    private ObservableList<String> players = FXCollections.observableArrayList("King_Author", "xXHunterXx", "Bob", "guest233539", "TheONe");
    String selected_Player;

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
        request_People.setVisible(false);
        player_List.setItems(players);
    }

    @FXML
    private void name_on_Click_Pop_out() {
        request_People.setVisible(!request_People.isVisible());
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
    private void toggle_Request_People() {
        request_People.setVisible(!request_People.isVisible());
    }

    // Handles the visibility of the request menu when name is pressed
    @FXML
    public void open_Request_Menu() {
        selected_Player = player_List.getSelectionModel().getSelectedItem();
        toggle_Request_People();

    }

    // Read entered text and filter list according to what was written
    public void change_List(InputMethodEvent inputMethodEvent) {
        String player_Name = player_Search.getText();

    }


    @FXML
    void handlePlayerProfileButton() {
        PageNavigator.navigateTo("profile");
    }

    @FXML
    void handleChatPopupButton() {
        PageNavigator.chatWindow("chat_Popup");
    }

    @FXML
    void handleFriendRequestButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    void handleChallangeRequestButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    void handleSelectedPlayerLeaderboardButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    void handleBlockButton() {
        PageNavigator.navigateTo("settings");
    }

    @FXML
    private void open_Chat_Button(){
        PageNavigator.navigateTo("settings");
    }

}