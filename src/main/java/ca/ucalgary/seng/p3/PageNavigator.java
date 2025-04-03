package ca.ucalgary.seng.p3;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PageNavigator {
    public static void navigateTo(String pageName) {
        try {
            Stage stage = new Stage();
            String fxmlPath = "/" + pageName + ".fxml";
            FXMLLoader loader = new FXMLLoader(PageNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Close the current stage if any
            if (Stage.getWindows().size() > 0) {
                Stage currentStage = (Stage) Stage.getWindows().get(0);
                currentStage.close();
            }

            stage.setScene(scene);
            stage.setTitle(pageName.substring(0, 1).toUpperCase() + pageName.substring(1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chatWindow(String pageName){
        try {
            Stage stage = new Stage();
            String fxmlPath = "/" + pageName + ".fxml";
            FXMLLoader loader = new FXMLLoader(PageNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(pageName.substring(0, 1).toUpperCase() + pageName.substring(1));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}