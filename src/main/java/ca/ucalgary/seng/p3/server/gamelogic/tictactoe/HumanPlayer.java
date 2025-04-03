package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

public class HumanPlayer implements Player {
    private final Input input;
    private final String marker;

    public HumanPlayer(Input input, String marker) {
        this.input = input;
        this.marker = marker;
    }

    @Override
    public String getMarker() {
        return marker;
    }

    @Override
    public int getMove(Game game) {
        return input.validateSelection(game.board.getAvailableSpaces());
    }

    @Override
    public String title() {
        return "Player";
    }
}
