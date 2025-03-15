package server.gamelogic.chess;

import java.util.List;

public class Pawn extends Piece {

    public Pawn(ChessGame game, Player player, int file) {
        super(game, player,1);
        if (isWhite) position = game.board[1][file-1];
        else position = game.board[7][file-1];
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
