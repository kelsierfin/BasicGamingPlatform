package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;

/***
 * Example use to create TicTacToe leaderboard page (the same for other games):
 * LeaderBoardController page = new LeaderBoardController("TicTacToe", "/icons/tictactoe-background.png");
 * Scene scene = new Scene(page.root, 900, 600);
 */
public class LeaderBoardController extends StackPane {

    public LeaderBoardController(String label, String background){

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LeaderBoardController.class.getResource("/leaderboard.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
            setBackground(new Background(new BackgroundImage
                    (new Image(getClass().getResourceAsStream(background)),
                            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,null,
                            new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true, true, false,true))));
        } catch (IOException e) {

        }

//        btnBack.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/icons/back-arrow.png"))));
//        btnBack.setOnAction(e->{
//            System.out.println("Back");
//        });

        //first medal
        rankList.add(new ImageView(new Image(getClass().getResourceAsStream("/icons/first-medal.png"))), 0,1);
        ImageView avatar = new ImageView(new Image(getClass().getResourceAsStream("/icons/avatar.png")));
//        Button button = new Button();
//        button.setGraphic(avatar);
//        button.setBackground(new Background(new BackgroundFill()));
        rankList.add(avatar, 1,1);
        //add name, i: column 2, i1: row 1
        Label nameLabel = new Label("Pie");
        nameLabel.setStyle("-fx-font-size: 20");
        nameLabel.setTextFill(new Color(1,1,1,1));
        rankList.add(nameLabel,2,1);

        //add points
        Label pointsLabel = new Label("1000pts");
        pointsLabel.setStyle("-fx-font-size: 20" );
        pointsLabel.setTextFill(new Color(1,1,1,1));
        rankList.add(pointsLabel, 3, 1);
        //add winning rate
        Label winRateLabel = new Label("87%");
        winRateLabel.setStyle("-fx-font-size: 20");
        winRateLabel.setTextFill(new Color(1,1,1,1));
        rankList.add(winRateLabel, 4, 1);

        //set pop up
        avatar.setOnMouseClicked(e->{

            ProfilePopUp popup = new ProfilePopUp();
            popup.setTitle("Profile");
            popup.setUserName("Pie");
            popup.setBio("\"I love choco pie \"");
            popup.setAvatar("/icons/avatar.png");

            popup.setMatches("150");
            popup.setWinnings("100");
            popup.setLosses("50");
            popup.show();
        });
    }
    @FXML
    private Button btnUserProfile;
    @FXML
    private GridPane rankList;

    @FXML
    private void initialize(){

    }
}
