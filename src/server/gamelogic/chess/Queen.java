package server.gamelogic.chess;

public class Queen extends Piece {

    public Queen(Player player) {
        super(player, 9);
        if (isWhite) position = ChessGame.BOARD[0][3];
        else position = ChessGame.BOARD[7][3];
    }
     public Queen(Player player, Square position) {
        super(player, 9);
        this.position = position;
     }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
