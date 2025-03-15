package server.gamelogic.chess;

public class ChessGame {
final Square[][] board;
final Player whitePlayer;
final Player blackPlayer;
public Player activePlayer;
public Player inactivePlayer;

    public ChessGame() {
        board = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i+1,j+1, findSquareName(i,j));
            }
        }
        whitePlayer = new Player(this,true);
        blackPlayer = new Player(this,false);
        activePlayer = whitePlayer;
        inactivePlayer = blackPlayer;
    }

    void move(Piece piece, Square destination) {

    }

    private static String findSquareName(int i, int j) {
        char[] fileList = {'a','b','c','d','e','f','g','h'};
        return String.valueOf(fileList[j]) + String.valueOf(i+1);
    }
}
