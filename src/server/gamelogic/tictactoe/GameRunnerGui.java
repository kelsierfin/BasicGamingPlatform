import java.util.List;

/**
 * GameRunnerGui serves as the GUI-compatible engine controller for Tic Tac Toe.
 * It replicates the responsibilities of the CLI-based GameRunner class but is
 * event-driven and designed to work with JavaFX button clicks.
 *
 * It holds the Game instance, handles board state updates, and provides
 * methods for the GUI to call when making moves or querying game status.
 */
public class GameRunnerGui {
    private final Game game;
    private final InputGui input;

    /**
     * Initializes a new 3x3 Tic Tac Toe game for the GUI, with the player’s assigned marker.
     *
     * @param localMarker The marker ("x" or "o") assigned to this GUI client
     */
    public GameRunnerGui(String localMarker) {
        Board board = new Board(3); // fixed 3x3 board

        this.input = new InputGui(); // GUI-driven input handler

        Player player1, player2;
        if (localMarker.equals("x")) {
            player1 = new HumanPlayer(input, "x");
            player2 = new HumanPlayer(input, "o");
        } else {
            player1 = new HumanPlayer(input, "o");
            player2 = new HumanPlayer(input, "x");
        }

        // Create the game using selected board and players
        this.game = new Game(board, player1, player2);
    }

    /**
     * Called when the player clicks a space on the board.
     * Registers the selected move, then executes the turn through the game engine.
     *
     * @param space The space number clicked (1 to 9 for 3x3 board)
     * @return true if the move was valid and accepted, false otherwise
     */
    public boolean makeMove(int space) {
        if (game.board.isMoveAllowed(space)) {
            input.setSelectedMove(space);
            game.takeTurn();
        }
        return false;
    }

    /**
     * Returns the current board state as a 2D array of strings.
     * Used by the GUI to render the board with current values.
     *
     * @return 2D array representing the board (e.g., [["x", "o", "3"], ...])
     */
    public String[][] getBoard() {
        List<String> flat = game.board.getSpaces();
        int size = (int) Math.sqrt(flat.size());
        String[][] grid = new String[size][size];
        for (int i = 0; i < flat.size(); i++) {
            grid[i / size][i % size] = flat.get(i);
        }
        return grid;
    }

    /**
     * Returns the marker ("x" or "o") of the current player.
     * Can be used by the GUI to update turn labels.
     *
     * @return The marker of the current player
     */
    public String getCurrentPlayerMarker() {
        return game.getCurrentPlayer1().getMarker();
    }

    /**
     * Checks if the game is over either by win or draw.
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameOver() {
        return game.isGameOver();
    }

    /**
     * Gets the winner’s marker if the game was won.
     * Returns null if the game ended in a draw.
     *
     * @return "x", "o", or null
     */
    public String getWinnerMarker() {
        return game.getWinner() !=null ? game.getWinner().getMarker() : null;
    }
}
