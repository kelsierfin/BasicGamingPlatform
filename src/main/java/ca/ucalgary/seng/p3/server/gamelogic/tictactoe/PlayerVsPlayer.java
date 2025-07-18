package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

public class PlayerVsPlayer implements GameType{
    private final Input input;
    public PlayerVsPlayer(Input input) {
        this.input = input;
    }

    @Override
    public Game execute(Board board) {
        Player player1 = new HumanPlayer(input, "x");
        Player player2 = new HumanPlayer(input, "o");
        return new Game(board, player1, player2);
    }

    @Override
    public String title() {
        return "Player Vs Player";
    }
}
