package server.gamelogic.chess;

public class Knight extends Piece {

    public Knight(Player player, boolean isOnKingsSide) {
        super(player,3);
        if (isWhite) {
            if (isOnKingsSide) position = ChessGame.BOARD[0][6];
            else position = ChessGame.BOARD[0][1];
        }
        else {
            if (isOnKingsSide) position = ChessGame.BOARD[7][6];
            else position = ChessGame.BOARD[7][1];
        }
    }

    public Knight(Player player, Square position) {
        super(player,3, position);
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }

    @Override
    public void updateAttackedSquares() {

    }
}
