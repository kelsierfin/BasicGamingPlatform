import java.util.ArrayList;
/**
 * The Game class encapsulates the logic for a complete Tic-Tac-Toe match.
 * It tracks the board, manages player turns, and evaluates win or tie conditions.
 *
 * This class serves as the core logic engine for both CLI and GUI interfaces.
 */
public class Game {
    public final Board board;
    final Player player1;
    final Player player2;
    Player currentPlayer;
    private Player winner;

    /**
     * Returns the current player whose turn it is.
     *
     * @return the current player
     */
    public Player getCurrentPlayer1() {
        return currentPlayer;
    }

    /**
     * Constructs a Game object with a given board and two players.
     *
     * @param board the game board
     * @param player1 the first player
     * @param player2 the second player
     */
    public Game(Board board, Player player1, Player player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    /**
     * Executes a single turn in the game:
     * - Retrieves a move from the current player
     * - Places the marker on the board
     * - Switches to the next player
     */
    void takeTurn() {
        board.placeMarker(currentPlayer.getMove(this), currentPlayer.getMarker());
        changeCurrentPlayer();
    }

    /**
     * Switches the turn to the other player.
     */
    public void changeCurrentPlayer() {
        currentPlayer = (currentPlayer.equals(player1)) ? (currentPlayer = player2) : (currentPlayer = player1);
    }

    /**
     * Determines if the game is over, either due to a win or a tie.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameWonBy(player1) || isGameWonBy(player2) || isGameTied();
    }
    /**
     * Gets the winner of the game, if one has been determined.
     *
     * @return the winning player, or null if no winner
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Checks whether the specified player has won the game.
     *
     * @param player the player to evaluate
     * @return true if the player has a winning combination, false otherwise
     */
    private boolean isGameWonBy(Player player) {
        for (ArrayList<String> line : winningCombination()) {
            if (line.stream().allMatch(space -> space.equals(player.getMarker()))) {
                winner = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the game has ended in a tie (i.e., no more spaces and no winner).
     *
     * @return true if the game is a tie, false otherwise
     */
    public boolean isGameTied() {
        return board.getAvailableSpaces().isEmpty() && !isGameWonBy(player1) && !isGameWonBy(player2);
    }

    /**
     * Collects all possible winning line combinations from the board:
     * - Rows
     * - Columns
     * - Left diagonal
     * - Right diagonal
     *
     * @return a list of all potential win paths on the board
     */
    private ArrayList<ArrayList<String>> winningCombination() {
        ArrayList<ArrayList<String>> winningCombinations = new ArrayList<>();
        winningCombinations.addAll(board.getRows());
        winningCombinations.addAll(board.getColumns());
        winningCombinations.add(board.getLeftDiagonal());
        winningCombinations.add(board.getRightDiagonal());
        return winningCombinations;
    }
}
