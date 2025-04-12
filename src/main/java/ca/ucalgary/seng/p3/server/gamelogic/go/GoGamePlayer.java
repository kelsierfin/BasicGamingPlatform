package ca.ucalgary.seng.p3.server.gamelogic.go;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.GameController;
import ca.ucalgary.seng.p3.server.gamelogic.GeneralPlayer;
import org.json.JSONObject;

public class GoGamePlayer implements GeneralPlayer {
    GoGame game;
    ClientNetworkManager network;
    GameController go;
    boolean playsForWhite;
    GoGamePlayer opponent;
    int color; // 1=black, 2=white
    boolean hasMoved = false;
    int numTurns;

    public GoGamePlayer(GoGame game, boolean isWhite) {
        this.game = game;
        this.playsForWhite = isWhite;
        this.color = isWhite ? 2 : 1;
    }

    public void setOpponent(GoGamePlayer opponent) {
        this.opponent = opponent;
    }

    @Override
    public void setNetwork(ClientNetworkManager network) {
        this.network = network;
    }

    @Override
    public void setGUI(GameController<?> gui) {
        if (gui instanceof GameController go) {
            this.go = gui;
        }
    }

    /**
     * Expected JSON fields:
     * {
     *   "action": "place" or "pass" or "surrender",
     *   "x": <int>,  // if action=place
     *   "y": <int>   // if action=place
     * }
     *
     * Returns true if this move ends the game (e.g. winner decided or surrender).
     */
    @Override
    public boolean updateGame(JSONObject move) {
        String action = move.optString("action", "unknown");
        switch (action) {
            case "place":
                return handlePlace(move);
            case "pass":
                return handlePass();
            case "surrender":
                return handleSurrender();
            default:
                System.out.println("Unrecognized action: " + action);
                return false;
        }
    }

    private boolean handlePlace(JSONObject move) {
        boolean success = game.playMove(move, playsForWhite);
        if (!success) {
            System.out.println("Invalid place move");
        }
        // Check if there's a winner after placing
        int winner = game.getWinner();
        return winner != 0;
    }

    private boolean handlePass() {
        // passTurn returns updated state
        int newState = game.getBoard().passTurn();
        // If state=1 or 2 => winner is decided
        return newState == 1 || newState == 2;
    }

    private boolean handleSurrender() {
        int surrenderingTeam = playsForWhite ? 2 : 1;
        int result = game.getBoard().surrender(surrenderingTeam);
        // result=winner
        // game state becomes 3 (surrender)
        return true; // surrender ends the game
    }
}

