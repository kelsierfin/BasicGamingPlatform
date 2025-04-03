public interface Player extends Menu {
    String getMarker();

    int getMove(Game game);

    String title();
}
