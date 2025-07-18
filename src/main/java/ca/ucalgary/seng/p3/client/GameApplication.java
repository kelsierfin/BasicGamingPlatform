package ca.ucalgary.seng.p3.client;
import ca.ucalgary.seng.p3.client.reflection.AuthReflector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application{
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load first window (Player 1)
        AuthReflector.getInstance();

        FXMLLoader fxmlLoader1 = new FXMLLoader(GameApplication.class.getResource("/landing.fxml"));
        Scene scene1 = new Scene(fxmlLoader1.load(), 900, 600);
        Stage stage1 = new Stage();
        stage1.setTitle("Player 1 - Board Games");
        stage1.setScene(scene1);
        stage1.setFullScreen(false);
        stage1.setResizable(false);
        stage1.show();

        //logs user out automatically when they exit screen
        stage1.setOnCloseRequest(event -> {
            try {
                ca.ucalgary.seng.p3.client.controllers.LogInController.performLogout();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}

