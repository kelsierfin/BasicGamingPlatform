package server.gamelogic.chess;

import java.util.List;

public class Rook extends Piece {

    public Rook(Player player, boolean isOnKingsSide) {
        super(player,5);
        if (isWhite) {
            if (isOnKingsSide) position = ChessGame.board[0][7];
            else position = ChessGame.board[0][0];
        }
        else {
            if (isOnKingsSide) position = ChessGame.board[7][7];
            else position = ChessGame.board[7][0];
        }
    }

    @Override
    public List<Square> findMoveOptions() {
        if (moveOptionsUpdated) return moveOptions;
        // Update moveOptions
        return moveOptions;
    }
}
