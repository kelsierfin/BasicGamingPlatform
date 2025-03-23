package ca.ucalgary.seng.p3;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GameApplication extends Application{
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/landing.fxml"));
        AnchorPane root = new AnchorPane();
        fxmlLoader.setRoot(root);
        fxmlLoader.load();
        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Placeholder Title");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

