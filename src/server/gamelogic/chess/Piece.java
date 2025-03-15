package server.gamelogic.chess;

import java.util.List;

abstract class Piece implements IPiece{
    ChessGame game;
    final Player player;
    final boolean isWhite;
    final int value;
    Square position;
    List<Square> moveOptions;
    boolean moveOptionsUpdated;

    public Piece(ChessGame game, Player player, int value) {
        this.game = game;
        this.player = player;
        isWhite = player.playsForWhite;
        this.value = value;
    }

}
