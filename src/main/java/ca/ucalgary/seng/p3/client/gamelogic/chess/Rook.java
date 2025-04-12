package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class Rook extends PinnerPiece {
    final Square castleDest;
    final Square kingCastleDest;
    boolean hasMoved;
    boolean captured;

    public Rook(ChessPlayer player, boolean isOnKingsSide) {
        super(player,'R',5);
        hasMoved = false;
        captured = false;

        int rankIndex;
        int fileIndex;
        int castleDestIndex;
        int kingCastleDestIndex;

        if (isWhite) rankIndex = 0;
        else rankIndex = 7;

        if (isOnKingsSide) {
            fileIndex = 7;
            castleDestIndex = 5;
            kingCastleDestIndex = 6;
            player.kingSideRook = this;
        } else {
            fileIndex = 0;
            castleDestIndex = 3;
            kingCastleDestIndex = 2;
            player.queenSideRook = this;
        }

        position = ChessGame.BOARD[rankIndex][fileIndex];
        player.game.boardLayout.put(position, this);

        castleDest = ChessGame.BOARD[rankIndex][castleDestIndex];
        kingCastleDest = ChessGame.BOARD[rankIndex][kingCastleDestIndex];
    }

    public Rook(ChessPlayer player, Square position) {
        super(player,'R',3, position);
        castleDest = null;
        kingCastleDest = null;
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();
        addGridWiseMoveOptions();
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();
        addGridWiseAttackLines();
    }
}
