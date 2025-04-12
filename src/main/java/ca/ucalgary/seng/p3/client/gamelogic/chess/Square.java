package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class Square {
    final int rankIndex;
    final int fileIndex;
    final String name;      // Is written in instruction file

    protected Square(int rankIndex, int fileIndex, String name) {
        this.rankIndex = rankIndex;
        this.fileIndex = fileIndex;
        this.name = name;
    }
}
