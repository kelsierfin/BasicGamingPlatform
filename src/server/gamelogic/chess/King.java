package server.gamelogic.chess;

public class King extends Piece {

    public King(Player player) {
        super(player, 0);
        if (isWhite) position = ChessGame.BOARD[0][4];
        else position = ChessGame.BOARD[7][4];
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
