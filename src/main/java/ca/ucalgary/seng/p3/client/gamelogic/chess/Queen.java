package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class Queen extends PinnerPiece {

    public Queen(ChessPlayer player) {
        super(player,'Q',9);

        int rankIndex;
        if (isWhite) rankIndex = 0;
        else rankIndex = 7;

        position = ChessGame.BOARD[rankIndex][3];
        player.game.boardLayout.put(position, this);
    }

    public Queen(ChessPlayer player, Square position) {
        super(player,'Q',9, position);
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();
        addGridWiseMoveOptions();
        addDiagonalMoveOptions();
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();
        addGridWiseAttackLines();
        addDiagonalAttackLines();
    }
}
