package ca.ucalgary.seng.p3.client.gamelogic.chess;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece implements IPiece{
//    ChessGame game;
    final ChessPlayer player;
    final boolean isWhite;
    final char type;
    final int value;
    Square position;
    List<Square> moveOptions;
    List<Square> attackedSquares;
    String pinLine;     // "R" = rank, "F" = file,
                        // "LH" diagonal going from low rank and file to high rank and file
                        // "RF" diagonal going from high rank and low file to low rank and high file

    public Piece(ChessPlayer player, char type, int value) {
        this.player = player;
        player.pieces.add(this);
        isWhite = player.playsForWhite;
        this.type = type;
        this.value = value;
        moveOptions = new ArrayList<>();
        attackedSquares = new ArrayList<>();
    }

    public Piece(ChessPlayer player, char type, int value, Square position) {
        this(player, type, value);
        this.position = position;
        player.game.boardLayout.put(position, this);
    }
}
