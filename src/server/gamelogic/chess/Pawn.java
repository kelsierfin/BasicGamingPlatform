package server.gamelogic.chess;

public class Pawn extends Piece {

    public Pawn(Player player, int file) {
        super(player,1);
        if (isWhite) position = ChessGame.BOARD[1][file-1];
        else position = ChessGame.BOARD[7][file-1];
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
