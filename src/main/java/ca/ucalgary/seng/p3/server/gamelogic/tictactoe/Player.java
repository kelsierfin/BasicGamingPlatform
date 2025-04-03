package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

public interface Player extends Menu {
    String getMarker();

    int getMove(Game game);

    String title();
}
