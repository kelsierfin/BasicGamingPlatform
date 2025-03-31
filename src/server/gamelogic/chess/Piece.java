package server.gamelogic.chess;

import java.util.List;

public abstract class Piece implements IPiece{
//    ChessGame game;
    final Player player;
    final boolean isWhite;
    final int value;
    Square position;
    PinnerPiece pinner;
    List<Square> moveOptions;
    List<Square> attackedSquares;

    public Piece(Player player, int value) {
        this.player = player;
        isWhite = player.playsForWhite;
        this.value = value;
    }

    public Piece(Player player, int value, Square position) {
        this(player, value);
        this.position = position;
    }
}
