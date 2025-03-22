package server.gamelogic.chess;

import java.util.List;

public class Queen extends Piece {

    public Queen(Player player) {
        super(player, 9);
        if (isWhite) position = ChessGame.board[0][3];
        else position = ChessGame.board[7][3];
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
