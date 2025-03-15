package server.gamelogic.chess;

import java.util.List;

public class Square {
    final int rank;
    final int file;
    Piece piece;
    List<Square> moveOptions;
    boolean moveOptionsUpdated;

    protected Square(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }
}
