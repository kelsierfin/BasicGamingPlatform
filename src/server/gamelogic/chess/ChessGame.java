package server.gamelogic.chess;

import java.util.HashMap;

public class ChessGame {
    public static final Square[][] BOARD = new Square[8][8];
    public static final HashMap<String, Square> NAME_TO_SQUARE = new HashMap<>();
    public static final HashMap<Character, Class<? extends Piece>> SYMBOL_TO_PIECE = new HashMap<>();
    final Player whitePlayer;
    final Player blackPlayer;
    public Player activePlayer;
    public HashMap<Square, Piece> boardLayout;

    static {
        // Define BOARD and NAME_TO_SQUARE
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                BOARD[i][j] = new Square(i, j, findSquareName(i, j));
                NAME_TO_SQUARE.put(BOARD[i][j].name, BOARD[i][j]);
            }
        }
        // Define pieceTypeMap
        SYMBOL_TO_PIECE.put('K', King.class);
        SYMBOL_TO_PIECE.put('Q', Queen.class);
        SYMBOL_TO_PIECE.put('R', Rook.class);
        SYMBOL_TO_PIECE.put('B', Bishop.class);
        SYMBOL_TO_PIECE.put('N', Knight.class);
        SYMBOL_TO_PIECE.put('P', Pawn.class);
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
