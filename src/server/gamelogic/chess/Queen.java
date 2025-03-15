package server.gamelogic.chess;

import java.util.List;

public class Queen extends Piece {

    public Queen(ChessGame game, Player player) {
        super(game, player, 9);
        if (isWhite) position = game.board[0][3];
        else position = game.board[7][3];
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
