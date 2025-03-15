package server.gamelogic.chess;

import java.util.List;

public class King extends Piece {

    public King(ChessGame game, Player player) {
        super(game, player, 0);
        if (isWhite) position = game.board[0][4];
        else position = game.board[7][4];
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
