package ca.ucalgary.seng.p3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.awt.*;

public class GoController {

    @FXML
    GridPane board = new GridPane();
    @FXML
    public void initialize() {
        board.setGridLinesVisible(true);
        for(int i = 0; i < board.getRowCount(); i++){
            for(int j = 0; j < board.getColumnCount(); j++){
                Button b = new Button();
                b.setMaxWidth(Double.MAX_VALUE);
                b.setMaxHeight(Double.MAX_VALUE);
                b.setStyle("-fx-background-color:rgba(0,0,0,0); ");
                int finalI = i;
                int finalJ = j;
                b.setOnAction((ActionEvent e) -> {
                    System.out.println(finalI +","+ finalJ);    //prints coordinates of the square selected
                });
                board.add(b, i,j);
            }
        }
    }

    public void handleCellClick(ActionEvent actionEvent) {
    }

    public void handleExit(ActionEvent actionEvent) {
    }

    public void handleSend(ActionEvent actionEvent) {
    }
}
