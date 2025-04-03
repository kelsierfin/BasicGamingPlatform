public interface GameType {
    Game execute(Board board);
    String title();
}
