package server.gamelogic.chess;

public class Rook extends Piece {

    public Rook(Player player, boolean isOnKingsSide) {
        super(player,5);
        if (isWhite) {
            if (isOnKingsSide) position = ChessGame.BOARD[0][7];
            else position = ChessGame.BOARD[0][0];
        }
        else {
            if (isOnKingsSide) position = ChessGame.BOARD[7][7];
            else position = ChessGame.BOARD[7][0];
        }
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
