package ca.ucalgary.seng.p3.server.gamelogic.go;

import ca.ucalgary.seng.p3.server.gamelogic.GameInterface;
import ca.ucalgary.seng.p3.server.gamelogic.GeneralPlayer;
import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.GameController;
import org.json.JSONObject;

public class GoGame implements GameInterface {
    private GoGamePlayer playerOne;
    private GoGamePlayer playerTwo;
    private Board board;
    private int playerOneTurns;
    private int playerTwoTurns;

    /**
     * Default constructor that initializes the Go board and two players.
     * playerOne = black, playerTwo = white.
     */
    public GoGame() {
        this.board = new Board();
        board.initialize(board);
        this.playerOne = new GoGamePlayer(this, false); // black
        this.playerTwo = new GoGamePlayer(this, true);  // white
        playerOne.setOpponent(playerTwo);
        playerTwo.setOpponent(playerOne);
        this.playerOneTurns = 0;
        this.playerTwoTurns = 0;
    }

    /**
     * Returns the first (black) player.
     */
    @Override
    public GeneralPlayer getPlayerOne() {
        return playerOne;
    }

    /**
     * Returns the second (white) player.
     */
    @Override
    public GeneralPlayer getPlayerTwo() {
        return playerTwo;
    }

    /**
     * Checks if the game has a winner.
     * 1 = black, 2 = white, 0 = no winner.
     */
    @Override
    public int getWinner() {
        return board.getState() == 1 ? 1 : board.getState() == 2 ? 2 : 0;
    }

    /**
     * Returns how many turns player one (black) has taken.
     */
    @Override
    public int getPlayerOneTurns() {
        return playerOneTurns;
    }

    /**
     * Returns how many turns player two (white) has taken.
     */
    @Override
    public int getPlayerTwoTurns() {
        return playerTwoTurns;
    }

    /**
     * Returns the Go board instance.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Increments the turn counter for the player who made a move.
     * If isWhite = true, increments white's turn count.
     * Otherwise increments black's turn count.
     */
    public void incrementTurns(boolean isWhite) {
        if (isWhite) {
            playerTwoTurns++;
        } else {
            playerOneTurns++;
        }
    }

    /**
     * Sets the network connections for both players.
     */
    public void setNetworkForPlayers(ClientNetworkManager net1, ClientNetworkManager net2) {
        playerOne.setNetwork(net1);
        playerTwo.setNetwork(net2);
    }

    /**
     * Sets the GUI controllers for both players.
     */
    public void setGUIControllers(GameController<?> gui1, GameController<?> gui2) {
        playerOne.setGUI(gui1);
        playerTwo.setGUI(gui2);
    }

    /**
     * Reads a JSON object describing a move and tries to execute it.
     * Example JSON: { "x": 3, "y": 4 }
     * If successful, increments the turn count.
     * @param move The JSON containing coordinates.
     * @param isWhite Whether it's the white player's move.
     * @return true if the move was successful, false otherwise.
     */
    public boolean playMove(JSONObject move, boolean isWhite) {
        int x = move.optInt("x", -1);
        int y = move.optInt("y", -1);
        if (x == -1 || y == -1) {
            return false;
        }
        Board.MoveResult result = board.placeStone(x, y, isWhite ? 2 : 1);
        if (result.success) {
            incrementTurns(isWhite);
        }
        return result.success;
    }

    /**
     * Informs the system that a move has been performed, capturing MoveResult details.
     * This method can be used to broadcast or log the move.
     * @param result The MoveResult containing success, color, coordinates, and captured stones.
     */
    public void updateMove(Board.MoveResult result) {
        // TODO: implement how to propagate or store the move result.
        System.out.println("[updateMove] Player color=" + result.color
                + ", placed stone at (" + result.coord[0] + "," + result.coord[1]
                + "), cleared=" + result.cleared.size() + " stones.");
    }

    /**
     * Informs the system that the specified color player has passed their turn.
     * @param color 1=black, 2=white.
     */
    public void updatePassTurn(int color) {
        // TODO: implement how to propagate or store the pass turn event.
        System.out.println("[updatePassTurn] Player color=" + color + " has passed.");
    }

    /**
     * Informs the system that the specified color player has surrendered.
     * @param color 1=black, 2=white.
     */
    public void updateSurrender(int color) {
        // TODO: implement how to propagate or store the surrender event.
        System.out.println("[updateSurrender] Player color=" + color + " has surrendered.");
    }

    /**
     * Informs the system that the game has ended, specifying the winning color.
     * @param color 1=black, 2=white.
     */
    public void updateFinishGame(int color) {
        // TODO: implement how to propagate or store the finish event.
        String winner = (color == 1) ? "Black" : "White";
        System.out.println("[updateFinishGame] Game finished, winner=" + winner);
    }
}
