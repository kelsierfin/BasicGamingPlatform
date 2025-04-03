package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

public interface GameType {
    Game execute(Board board);
    String title();
}
