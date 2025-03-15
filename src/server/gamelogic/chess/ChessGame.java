package server.gamelogic.chess;

public class ChessGame {
final Square[][] board;

    public ChessGame() {
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i+1,j+1);
            }
        }
    }

    public void processInput(String input) {

    }
}
