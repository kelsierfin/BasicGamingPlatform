public class PlayerVsPlayer {
    private final Input input;
    public PlayerVsPlayer(Input input) {
        this.input = input;
    }

    @Override
    public Game execute(Board board) {
        Player player1 = new Player(input, "x");
        Player player2 = new Player(input, "o");
        return new Game(board, player1, player2);
    }

    @Override
    public String title() {
        return "Player Vs Player";
    }
}
