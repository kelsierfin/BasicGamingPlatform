package server.gamelogic.chess;

public class Rook extends PinnerPiece {
    final Square castleDest;
    final Square castleKingDest;
    boolean hasMoved;

    public Rook(Player player, boolean isOnKingsSide) {
        super(player,5);
        hasMoved = false;

        int rankIndex;
        int fileIndex;
        int castleDestIndex;
        int castleKingDestIndex;

        if (isWhite) rankIndex = 0;
        else rankIndex = 7;

        if (isOnKingsSide) {
            fileIndex = 7;
            castleDestIndex = 5;
            castleKingDestIndex = 6;
            player.kingSideRook = this;
        } else {
            fileIndex = 0;
            castleDestIndex = 3;
            castleKingDestIndex = 2;
            player.queenSideRook = this;
        }
        position = ChessGame.BOARD[rankIndex][fileIndex];
        castleDest = ChessGame.BOARD[rankIndex][castleDestIndex];
        castleKingDest = ChessGame.BOARD[rankIndex][castleKingDestIndex];
    }

    public Rook(Player player, Square position) {
        super(player,3, position);
        castleDest = null;
        castleKingDest = null;
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }

    @Override
    public void updateAttackedSquares() {

    }
}
