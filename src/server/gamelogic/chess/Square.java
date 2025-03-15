package server.gamelogic.chess;

import java.util.List;

public class Square {
    final int rank;
    final int file;
    final String name;      // Is written in instruction file
    Piece piece;

    protected Square(int rank, int file, String name) {
        this.rank = rank;
        this.file = file;
        this.name = name;
    }
}
