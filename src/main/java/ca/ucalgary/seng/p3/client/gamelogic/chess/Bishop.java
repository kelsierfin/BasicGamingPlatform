package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class Bishop extends PinnerPiece {

    public Bishop(ChessPlayer player, boolean isOnKingsSide) {
        super(player,'B',3);

        int rankIndex;
        int fileIndex;

        if (isWhite) rankIndex = 0;
        else rankIndex = 7;

        if (isOnKingsSide) fileIndex = 5;
        else fileIndex = 2;

        position = ChessGame.BOARD[rankIndex][fileIndex];
        player.game.boardLayout.put(position, this);
    }

    public Bishop(ChessPlayer player, Square position) {
        super(player,'B',3, position);
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();
        addDiagonalMoveOptions();
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();
        addDiagonalAttackLines();
    }
}
