package ca.ucalgary.seng.p3.client.controllers;

import ca.ucalgary.seng.p3.client.game_to_gui_commands.chess.*;
import ca.ucalgary.seng.p3.client.gamelogic.chess.ChessGame;
import ca.ucalgary.seng.p3.client.gamelogic.chess.ChessPlayer;
import ca.ucalgary.seng.p3.client.gamelogic.chess.PlayerPair;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.Node;
import java.util.Timer;

public class ChessController implements GameController {

    @FXML
    private Label pawnsTaken2;
    @FXML
    private Label pawnsTaken1;
    @FXML
    private Label castlesTaken2;
    @FXML
    private Label castlesTaken1;
    @FXML
    private Label knightsTaken2;
    @FXML
    private Label knightsTaken1;
    @FXML
    private Label bishopsTaken2;
    @FXML
    private Label bishopsTaken1;
    @FXML
    private Label queensTaken2;
    @FXML
    private Label queensTaken1;
    @FXML
    private Label kingTaken2;
    @FXML
    private Label kingTaken1;
    @FXML
    private Button promoteCastle;
    @FXML
    private ImageView promoteCastlePic;
    @FXML
    private Button promoteKnight;
    @FXML
    private ImageView promoteKnightPic;
    @FXML
    private ImageView promotePawnPic;
    @FXML
    private Button promoteQueen;
    @FXML
    private ImageView promoteQueenPic;
    @FXML
    private Button promoteBishop;
    @FXML
    private ImageView promoteBishopPic;
    @FXML
    public GridPane board;
    @FXML
    private TextArea chat_Area;
    @FXML
    private Pane promotionVisability;
    @FXML
    private TextField chat_Type_Field;
    @FXML
    private Button exitButton1;
    @FXML
    private Label opponent_Name;
    @FXML
    private ImageView opponent_Profile_pic;
    @FXML
    private Button send_Button;
    @FXML
    private Label timer;
    @FXML
    private Label mPointAdv;
    @FXML
    private Label turnLabel;

    ChessGame game;
    ChessPlayer player1;
    ChessPlayer player2;
    private static final int ROWS = 8;
    private static final int COLUMNS = 8;

    boolean turnIndicator = true;

    // field to store the currently selected tile
    private Button selectedTile = null;

    // Create a timer for the game
    private Timer time = new Timer();

    private static String findSquareName(int i, int j) {
        char[] fileList = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        return fileList[j] + String.valueOf(i + 1);
    }

    @FXML
    public void initialize() {
        startingBoard();
        promotionVisability.setVisible(!promotionVisability.isVisible());
        board.setGridLinesVisible(true);
        if (chat_Area != null) {
            chat_Area.setEditable(false);
        }

//        opponent_Name.setText("PLAYER 2");
        timer.setText("10:00");
    }

    public ChessGame getGame() {
        return game;
    }

    // Tracks who turn it is
    private void updateTurnIndicator() {
        if (!turnIndicator) {
            turnIndicator = true;
            // Change this to the usernames
            turnLabel.setText(player1 + " TURN");
        } else {
            turnIndicator = false;
            turnLabel.setText(player2 + " TURN");
        }
    }

    /**
     * Initializes a board with buttons named a1 - h8
     */
    private void startingBoard() {
        ChessGame playBoard = new ChessGame();
        turnLabel.setText(player1 + " TURN");

        // Initialize the board
        Boolean colourFlipper = true;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                // Set all buttons with the same functions and put the starting pieces in their place
                Button tile = new Button();
                tile.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                String tilePosition = findSquareName(row, col);
                tile.setId(tilePosition);

                // Flip the colour of the background to make a chess pattern
                if (row % 2 == 0) {
                    if (colourFlipper.equals(false)) {
                        tile.setBackground(Background.fill(Color.BLACK));
                        colourFlipper = true;
                    } else {
                        tile.setBackground(Background.fill(Color.WHITESMOKE));
                        colourFlipper = false;
                    }
                } else {
                    if (colourFlipper.equals(true)) {
                        tile.setBackground(Background.fill(Color.BLACK));
                        colourFlipper = false;
                    } else {
                        tile.setBackground(Background.fill(Color.WHITESMOKE));
                        colourFlipper = true;
                    }
                }

                // Set up each side with their respective images
                ImageView chessPiece = getImageViewStart(row, col);
                tile.setGraphic(chessPiece);

                // set the tile's piece type using UserData
                // player 2's board (top)
                if (row == 0 && (col == 0 || col == 7)) {
                    tile.setUserData("R");
                } else if (row == 0 && (col == 1 || col == 6)) {
                    tile.setUserData("N");
                } else if (row == 0 && (col == 2 || col == 5)) {
                    tile.setUserData("B");
                } else if (row == 0 && col == 3) {
                    tile.setUserData("Q");
                } else if (row == 0 && col == 4) {
                    tile.setUserData("K");
                } else if (row == 1) {
                    tile.setUserData("P");
                }
                // player 1's board (bottom)
                else if (row == 7 && (col == 0 || col == 7)) {
                    tile.setUserData("R");
                } else if (row == 7 && (col == 1 || col == 6)) {
                    tile.setUserData("N");
                } else if (row == 7 && (col == 2 || col == 5)) {
                    tile.setUserData("B");
                } else if (row == 7 && col == 3) {
                    tile.setUserData("Q");
                } else if (row == 7 && col == 4) {
                    tile.setUserData("K");
                } else if (row == 6) {
                    tile.setUserData("P");
                } else {
                    // all other tiles are empty
                    tile.setUserData("clear");
                }

                // Give each tile an action when pressed
                final int column = col;
                final int row1 = row;
                tile.setOnAction(e -> handleTileSelection(column, row1));

                board.add(tile, col, row);

            }
        }
    }

    // Set up each side with their respective images
    private static ImageView getImageViewStart(int row, int col) {
        // set every tile as a clear image
        ImageView chessPiece = new ImageView("Game Assets/Clear tile.png");
        //player 2's board
        if (row == 0 && (col == 0 || col == 7)) {
            chessPiece = new ImageView("Game Assets/Chess Castle 2.png");
        }
        if (row == 0 && (col == 1 || col == 6)) {
            chessPiece = new ImageView("Game Assets/Chess Horse 2.png");
        }
        if (row == 0 && (col == 2 || col == 5)) {
            chessPiece = new ImageView("Game Assets/Chess Bishop 2.png");
        }
        if (row == 0 && col == 3) {
            chessPiece = new ImageView("Game Assets/Chess Queen 2.png");
        }
        if (row == 0 && col == 4) {
            chessPiece = new ImageView("Game Assets/Chess king 2.png");
        }
        if (row == 1) {
            chessPiece = new ImageView("Game Assets/Chess Pawn 2.png");
        }

        //player 1's board
        if (row == 7 && (col == 0 || col == 7)) {
            chessPiece = new ImageView("Game Assets/Chess Castle.png");
        }
        if (row == 7 && (col == 1 || col == 6)) {
            chessPiece = new ImageView("Game Assets/Chess Horse.png");
        }
        if (row == 7 && (col == 2 || col == 5)) {
            chessPiece = new ImageView("Game Assets/Chess Bishop.png");
        }
        if (row == 7 && col == 3) {
            chessPiece = new ImageView("Game Assets/Chess Queen.png");
        }
        if (row == 7 && col == 4) {
            chessPiece = new ImageView("Game Assets/Chess king.png");
        }
        if (row == 6) {
            chessPiece = new ImageView("Game Assets/Chess Pawn.png");
        }
        return chessPiece;
    }

    // looks at user data and updates when the piece is captured
    private void piecesCaptured(String tile) {
        int targetCaptured;
        System.out.println(tile);
        if (!tile.equals("clear")) {
            // If the tile is not clear keep an index of what pieces are captured
            //Player 1 pieces black
            if (tile.equals("P")) {
                targetCaptured = Integer.parseInt(pawnsTaken1.getText());
                targetCaptured += 1;
                pawnsTaken1.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("castle1")) {
                targetCaptured = Integer.parseInt(castlesTaken1.getText());
                targetCaptured += 1;
                castlesTaken1.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("knight1")) {
                targetCaptured = Integer.parseInt(knightsTaken1.getText());
                targetCaptured += 1;
                knightsTaken1.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("bishop1")) {
                targetCaptured = Integer.parseInt(bishopsTaken1.getText());
                targetCaptured += 1;
                bishopsTaken1.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("queen1")) {
                targetCaptured = Integer.parseInt(queensTaken1.getText());
                targetCaptured += 1;
                queensTaken1.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("king1")) {
                targetCaptured = Integer.parseInt(kingTaken1.getText());
                targetCaptured += 1;
                kingTaken1.setText(Integer.toString(targetCaptured));
            }

            //Player 2 pieces white
            if (tile.equals("P")) {
                targetCaptured = Integer.parseInt(pawnsTaken2.getText());
                targetCaptured += 1;
                pawnsTaken2.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("castle2")) {
                targetCaptured = Integer.parseInt(castlesTaken2.getText());
                targetCaptured += 1;
                castlesTaken2.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("knight2")) {
                targetCaptured = Integer.parseInt(knightsTaken2.getText());
                targetCaptured += 1;
                knightsTaken2.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("bishop2")) {
                targetCaptured = Integer.parseInt(bishopsTaken2.getText());
                targetCaptured += 1;
                bishopsTaken2.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("queen2")) {
                targetCaptured = Integer.parseInt(queensTaken2.getText());
                targetCaptured += 1;
                queensTaken2.setText(Integer.toString(targetCaptured));
            }
            if (tile.equals("king2")) {
                targetCaptured = Integer.parseInt(kingTaken2.getText());
                targetCaptured += 1;
                kingTaken2.setText(Integer.toString(targetCaptured));
            }
        }
    }

    // Determines whether the button's graphic is the default clear tile image
    private boolean isClearTile(Button tile) {
        Object data = tile.getUserData();
        return data != null && data.equals("clear");
    }


    @FXML
    private void handleTileSelection(int col, int row) {
        // if no piece is selected and the clicked tile has a piece already mark it as selected,
        // if a piece is already selected move the selected piece's graphic to the clicked destination tile
        // set source tile back to a clear tile, remove any highlighting, update the turn indicator

        // turnIndicator commented out for testing purposes so you can move all the pieces around the screen to ensure they work
        // if (!turnIndicator) return;

        Button clickedTile = (Button) getTile(board, col, row);

        // case 1: no piece is selected yet
        if (selectedTile == null) {
            // only allow selection if this tile isn't an empty tile
            if (!isClearTile(clickedTile)) {
                selectedTile = clickedTile;
                System.out.println(selectedTile.getId());
                System.out.println(clickedTile.getUserData());
                // a visual indicator to show selection
                selectedTile.setStyle("-fx-border-color: red; -fx-border-width: 2;");
            }
        }
        // case 2: a piece is already selected
        else {
            // if the same tile is clicked, deselect it
            if (clickedTile == selectedTile) {
                selectedTile.setStyle(null);
                selectedTile = null;
                return;
            }

            String selectedPieceData = selectedTile.getUserData().toString();
            String clickPieceData = clickedTile.getUserData().toString();
            String clickTileId = clickedTile.getId();
            // prevent moves onto a tile that already contains a piece from your side
            if ((selectedPieceData.contains("1") && clickPieceData.contains("1")) && vaildMove(clickedTile) ||
                    (selectedPieceData.contains("2") && clickPieceData.contains("2") && vaildMove(clickedTile))) {
                Alert invalidMove = new Alert(Alert.AlertType.WARNING);
                invalidMove.setHeaderText("Invalid Move");
                invalidMove.setContentText("Your piece cannot move to the selected tile");
                invalidMove.show();
                selectedTile.setStyle(null);
                selectedTile = null;
                return;
            }

            // instead of transferring the same ImageView, create a new one bc of problems that occurred with moving the same ImageView
            ImageView selectedImageView = (ImageView) selectedTile.getGraphic();
            ImageView newImageView = new ImageView(selectedImageView.getImage());
            clickedTile.setGraphic(newImageView);
            clickedTile.setUserData(selectedTile.getUserData());

            //Update captured pieces
            piecesCaptured(clickPieceData);
            //Check which piece is in checkmate
            checkMate(clickPieceData);
            //Set stuff
            setmPointAdv();
            promotePawn(selectedPieceData, clickTileId, clickedTile);
            // replace original tile's graphic with a clear tile
            selectedTile.setGraphic(new ImageView("Game Assets/Clear tile.png"));
            selectedTile.setUserData("clear");
            // remove the selection highlight
            selectedTile.setStyle(null);
            selectedTile = null;
            // update the board or turn indicator as needed
            updateTurnIndicator();
        }
    }

    private void promotePawn(String pawnName, String button_Position, Button tile) {
        boolean pawnP2Side = button_Position.contains("Button_0");
        boolean pawnP1Side = button_Position.contains("Button_7");
        
        // toggle visibility
        // Player 1 black pawn promotion
        if (!promotionVisability.isVisible()) {
            if (pawnName.equals("P") && pawnP2Side) {
                // Set images
                ImageView pawn = new ImageView("Game Assets/Chess Pawn Selected.png");
                ImageView castle = new ImageView("Game Assets/Chess Castle.png");
                ImageView knight = new ImageView("Game Assets/Chess Horse.png");
                ImageView bishop = new ImageView("Game Assets/Chess Bishop.png");
                ImageView queen = new ImageView("Game Assets/Chess Queen.png");

                // Toggle Promote screen Visibility
                promotionVisability.setVisible(true);
                promotePawnPic.setImage(pawn.getImage());
                promoteCastlePic.setImage(castle.getImage());
                promoteKnightPic.setImage(knight.getImage());
                promoteBishopPic.setImage(bishop.getImage());
                promoteQueenPic.setImage(queen.getImage());

                //Upon promotion set an action for every button
                promoteCastle.setOnAction(actionEvent -> {
                    tile.setUserData("R");
                    tile.setGraphic(castle);
                    promotionVisability.setVisible(false);
                });
                promoteKnight.setOnAction(actionEvent -> {
                    tile.setUserData("N");
                    tile.setGraphic(knight);
                    promotionVisability.setVisible(false);
                });
                promoteBishop.setOnAction(actionEvent -> {
                    tile.setUserData("B");
                    tile.setGraphic(bishop);
                    promotionVisability.setVisible(false);
                });
                promoteQueen.setOnAction(actionEvent -> {
                    tile.setUserData("Q");
                    tile.setGraphic(queen);
                    promotionVisability.setVisible(false);
                });

                // Player 2 white pawn promotion
            } else if (pawnName.equals("P") && pawnP1Side) {
                // Set images
                ImageView pawn = new ImageView("Game Assets/Chess Pawn 2 Selected.png");
                ImageView castle = new ImageView("Game Assets/Chess Castle 2.png");
                ImageView knight = new ImageView("Game Assets/Chess Horse 2.png");
                ImageView bishop = new ImageView("Game Assets/Chess Bishop 2.png");
                ImageView queen = new ImageView("Game Assets/Chess Queen 2.png");

                // Toggle Promote screen Visibility
                promotionVisability.setVisible(true);
                promotePawnPic.setImage(pawn.getImage());
                promoteCastlePic.setImage(castle.getImage());
                promoteKnightPic.setImage(knight.getImage());
                promoteBishopPic.setImage(bishop.getImage());
                promoteQueenPic.setImage(queen.getImage());

                //Upon promotion set an action for every button
                promoteCastle.setOnAction(actionEvent -> {
                    tile.setUserData("R");
                    tile.setGraphic(castle);
                    promotionVisability.setVisible(false);
                });
                promoteKnight.setOnAction(actionEvent -> {
                    tile.setUserData("N");
                    tile.setGraphic(knight);
                    promotionVisability.setVisible(false);
                });
                promoteBishop.setOnAction(actionEvent -> {
                    tile.setUserData("B");
                    tile.setGraphic(bishop);
                    promotionVisability.setVisible(false);
                });
                promoteQueen.setOnAction(actionEvent -> {
                    tile.setUserData("Q");
                    tile.setGraphic(queen);
                    promotionVisability.setVisible(false);
                });
            }
        }
    }

    void handlePromotion() {
    }


    private javafx.scene.Node getTile(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            if (nodeCol != null && nodeRow != null) {
                if (nodeCol == col && nodeRow == row) {
                    return node;
                }
            }
        }
        return null;
    }

    private Boolean vaildMove(Button tile) {
        // Take the selected tile and calculate it proper movement
        // Implement it here
        if (!tile.getId().equals("A button with a valid move except without the not at the front")) {
            return true;
        }
        return false;
    }

    // See who captured at the moment and
    private Boolean checkMate(String tile) {
        if (tile.equals("king1")) {
            Alert gameEnd = new Alert(Alert.AlertType.CONFIRMATION);
            gameEnd.setHeaderText("PLAYER 2 WON");
            gameEnd.setContentText(player1 + " lost, Challenge them and try again");
            gameEnd.show();
            return true;
        }
        if (tile.equals("king2")) {
            Alert gameEnd = new Alert(Alert.AlertType.CONFIRMATION);
            gameEnd.setHeaderText(player1 + " WON");
            gameEnd.setContentText("Player 2 lost, Challenge them and try again");
            gameEnd.show();
            return true;
        }

        return false;
    }

    // Based on How many pieces are captured will calculate the material point advantage
    private void setmPointAdv() {
        // Calculate the material point Advantage is and update the label after
        Integer player1Mpoint = 0;
        Integer player2Mpoint = 0;
        mPointAdv.setText(player1Mpoint.toString() + "/" + player2Mpoint.toString());
    }

    @FXML
    void input_Into_Chat(KeyEvent event) {

    }

    @FXML
    void handleExit() {
        PageNavigator.navigateTo("startgame_chess");
    }

}
