package ca.ucalgary.seng.p3;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class DashboardController {

    @FXML
    private ImageView chessIcon;
    @FXML
    private ImageView goIcon;

    @FXML
    private ImageView tttIcon;

    @FXML
    private ImageView connect4Icon;

    @FXML
    public void initialize() {

        List<ImageView> icons = List.of(chessIcon, goIcon, tttIcon, connect4Icon);
        for (ImageView icon : icons) {
            Rectangle rect = new Rectangle();
            rect.setWidth(icon.getFitWidth());
            rect.setHeight(icon.getFitHeight());
            rect.setArcWidth(20);
            rect.setArcHeight(20);

            icon.setClip(rect);
        }
    }
}
