package server.gamelogic.chess;

import java.util.List;

public class Square {
    final int rankIndex;
    final int fileIndex;
    final String name;      // Is written in instruction file
//    Piece piece;

    protected Square(int rankIndex, int fileIndex, String name) {
        this.rankIndex = rankIndex;
        this.fileIndex = fileIndex;
        this.name = name;
    }
}
