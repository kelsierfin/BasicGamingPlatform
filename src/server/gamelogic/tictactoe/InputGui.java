import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * InputGui is a GUI-specific implementation of the Input class.
 * It allows GUI events (e.g. button clicks) to communicate the selected move
 * to the game logic in a thread-safe way.
 *
 * This class replaces terminal-based input used in CLI with button-click-driven
 * input for JavaFX-based UIs.
 */
public class InputGui extends Input {
    // AtomicInteger ensures thread-safe reading/writing of the selected space
    private final AtomicInteger selectedMove = new AtomicInteger(-1);

    /**
     * Constructor for GUI input.
     * Since GUI doesn't use Scanner or Print, both arguments are passed as null.
     */
    public InputGui() {
        super(null, null);
    }

    /**
     * Sets the selected move from a GUI event (e.g. a button click).
     * This is how the GUI "sends" the selected space to the game.
     *
     * @param move The selected cell (1 to 9)
     */
    public void setSelectedMove(int move) {
        selectedMove.set(move); // Called from GUI when a button is clicked **
    }

    /**
     * Called by game logic (usually via HumanPlayer) to fetch a valid move.
     * Blocks until a move is selected and confirmed to be in the list of valid options.
     *
     * @param validOptions List of valid moves (free board positions)
     * @return The selected move
     */
    @Override
    public int validateSelection(List<Integer> validOptions) {
        // Wait until a valid move is set by the GUI
        while (!validOptions.contains(selectedMove.get())) {
            Thread.onSpinWait(); // Wait until a move is available
        }
        // Get and consume the move, then reset for next turn
        int move = selectedMove.get();
        selectedMove.set(-1); // Reset for the next turn
        return move;
    }

    /**
     * Optional fallback method.
     * If called, returns the last selected move (not recommended for GUI flow).
     */
    @Override
    public int getInteger() {
        return selectedMove.get();
    }


}
