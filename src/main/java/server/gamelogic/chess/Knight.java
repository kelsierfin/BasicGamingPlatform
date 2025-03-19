package server.gamelogic.chess;

import java.util.List;

public class Knight extends Piece {

    public Knight(Player player, boolean isOnKingsSide) {
        super(player,3);
        if (isWhite) {
            if (isOnKingsSide) position = ChessGame.board[0][6];
            else position = ChessGame.board[0][1];
        }
        else {
            if (isOnKingsSide) position = ChessGame.board[7][6];
            else position = ChessGame.board[7][1];
        }
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
