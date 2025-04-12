package ca.ucalgary.seng.p3.client.game_client;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.gamelogic.*;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;

public class GameClient {
    private final ClientNetworkManager network;
    private boolean playerOneLocal;
    private GeneralPlayer<?> remotePlayer;
    private GameInterface game;
    private String username;
    private String gameType;
    // Flag to indicate whether a match has been made.
    private volatile boolean matchFound = false;

    public GameClient() {
        this.network = new ClientNetworkManager();
    }

    public void start(String username, String gameType) {
        try {
            // Connect to the server.
            network.connect("localhost", 8080, this::handleServerMessage);
            System.out.println("Connected to server.");

            // Send a MATCH_REQUEST message.
            JSONObject matchRequest = new JSONObject();
            matchRequest.put("type", "MATCH_REQUEST");
            matchRequest.put("username", username);
            matchRequest.put("gameType", gameType);
            network.sendMessage(matchRequest);
            System.out.println("Match request sent. Waiting for a match...");

            // Wait until a match is found.
            while (!matchFound) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // Handle interruption if needed.
                }
            }
            System.out.println("Match found! Starting game.");

            // Create a game instance and start the game loop.
            game = GameFactory.createGame(gameType);
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            network.disconnect();
        }
    }

    private void sendGameUpdate(JSONObject move, boolean isGameOver) {
        try {
            if (!isGameOver) {
                JSONObject request = new JSONObject();
                request.put("type", "GAME_UPDATE");
                request.put("username", username);
                request.put("move", move);
                network.sendMessage(request);
                System.out.println("Sent game update: " + request.toString());
            } else {
                sendFinalStats(move);
                System.out.println("Game over");
            }

        } catch (Exception e) {
            System.out.println("Invalid move: " + e.getMessage());
        }
    }

    private void sendFinalStats(JSONObject move) {
        try {
            JSONObject finalStats = new JSONObject();
            finalStats.put("type", "GAME_OVER");
            finalStats.put("username", username);
            finalStats.put("move", move);

            //Extra info for the server.
            finalStats.put("winner", game.getWinner());

            finalStats.put("playerOneTurns", game.getPlayerOneTurns());
            finalStats.put("playerTwoTurns", game.getPlayerTwoTurns());
            finalStats.put("gameType", gameType);
            network.sendMessage(finalStats);
            System.out.println("Game over. Final stats sent: " + finalStats.toString());
        } catch (IOException ex) {
            System.err.println("Error sending final stats: " + ex.getMessage());
        }
    }

    /**
     * Handles incoming messages from the server.
     */
    private void handleServerMessage(JSONObject message) {
        String type = message.optString("type", "");
        if (type.equals("MATCH_FOUND")) {
            matchFound = true;
            // Trigger callback if set.
            if (onMatchFoundCallback != null) {
                onMatchFoundCallback.run();
            }

            // Retrieve opponent info and alert
            // the user.
            String opponent = message.optString("opponent", "Unknown");
            System.out.println("Match found! Your opponent is: " + opponent);
            try {
                playerOneLocal = message.getBoolean("playerOneLocal");
            } catch (JSONException e) {
                // handle invalid value type
            }

            // game = GuiFactory.createGUI(gameType, playerOneLocal, network).getGame();
            if (playerOneLocal) remotePlayer = game.getPlayerTwo();
            else remotePlayer = game.getPlayerOne();
            System.out.println("Game started");
        } else if (type.equals("GAME_UPDATE")) {
            remotePlayer.updateGame(message.getJSONObject("move"));
            System.out.println("Received game update: " + message.toString());
        } else if (type.equals("GAME_OVER")) {
            remotePlayer.updateGame(message.getJSONObject("move"));
            System.out.println("Received game update: " + message.toString() + "\nOpponent's game over!");
        } else {
            System.out.println("Received unknown message: " + message.toString());
        }
    }

    private Runnable onMatchFoundCallback;

    public void setOnMatchFoundCallback(Runnable callback) {
        this.onMatchFoundCallback = callback;
    }
}
