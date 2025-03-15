package server.gamelogic.chess;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(ChessGame game, Player player, boolean isOnKingsSide) {
        super(game, player,3);
        if (isWhite) {
            if (isOnKingsSide) position = game.board[0][5];
            else position = game.board[0][2];
        }
        else {
            if (isOnKingsSide) position = game.board[7][5];
            else position = game.board[7][2];
        }
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
