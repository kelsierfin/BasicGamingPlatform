package server.gamelogic.chess;

public abstract class CastledPiece extends Piece{
    protected boolean hasMoved;

    public CastledPiece(Player player, int value) {
        super(player, value);
        hasMoved = false;
    }

    public CastledPiece(Player player, int value, Square position) {
        super(player, value, position);
        hasMoved = false;
    }
}
