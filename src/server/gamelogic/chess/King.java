package server.gamelogic.chess;

import java.util.List;

public class King extends Piece {

    public King(Player player) {
        super(player, 0);
        if (isWhite) position = ChessGame.board[0][4];
        else position = ChessGame.board[7][4];
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
