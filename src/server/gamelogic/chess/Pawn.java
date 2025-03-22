package server.gamelogic.chess;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(Player player, int file) {
        super(player,1);
        if (isWhite) position = ChessGame.board[1][file-1];
        else position = ChessGame.board[7][file-1];
    }

    @Override
    public void updateMoveOptions() {
        // Update moveOptions
    }
}
