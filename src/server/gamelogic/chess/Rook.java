package server.gamelogic.chess;

import java.util.List;

public class Rook extends Piece {

    public Rook(ChessGame game, Player player, boolean isOnKingsSide) {
        super(game, player,3);
        if (isWhite) {
            if (isOnKingsSide) position = game.board[0][7];
            else position = game.board[0][0];
        }
        else {
            if (isOnKingsSide) position = game.board[7][7];
            else position = game.board[7][0];
        }
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
