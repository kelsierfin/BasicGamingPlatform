package server.gamelogic.chess;

import java.util.HashMap;

public class ChessGame {
    public static final Square[][] board;
    public static final HashMap<String, Square> nameToSquare;
    final Player whitePlayer;
    final Player blackPlayer;
    public Player activePlayer;
    public HashMap<Square, Piece> squareToPiece;

    static {
        board = new Square[8][8];
        nameToSquare = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Square(i, j, findSquareName(i, j));
                nameToSquare.put(board[i][j].name, board[i][j]);
            }
        }
    }

    public ChessGame() {
        whitePlayer = new Player(this, true);
        blackPlayer = new Player(this, false);
        whitePlayer.opponent = blackPlayer;
        blackPlayer.opponent = whitePlayer;
        activePlayer = whitePlayer;
    }

    private static String findSquareName(int i, int j) {
        char[] fileList = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        return fileList[j] + String.valueOf(i + 1);
    }
}
