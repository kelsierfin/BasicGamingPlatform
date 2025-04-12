package ca.ucalgary.seng.p3.server;

import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import ca.ucalgary.seng.p3.server.leadmatch.MatchmakingLogic;

public class GameServer {

    // Pre-create matchmaking logic instances per supported game type.
    private static final Map<String, MatchmakingLogic> matchmakingInstances = new HashMap<>();

    static {
        matchmakingInstances.put("chess", new MatchmakingLogic("chess"));
        matchmakingInstances.put("connect4", new MatchmakingLogic("connect4"));
        matchmakingInstances.put("go", new MatchmakingLogic("go"));
        matchmakingInstances.put("tictactoe", new MatchmakingLogic("tictactoe"));
    }

    public static void main(String[] args) {
        new GameServer().start();
    }

    public void start() {
        ServerNetworkManager server = new ServerNetworkManager();
        try {
            server.start(8080, wrapper -> {
                Socket clientSocket = (Socket) wrapper.get("socket");
                JSONObject data = wrapper.getJSONObject("data");
                System.out.println("Received from " + clientSocket.getRemoteSocketAddress() + ": " + data);

                String type = data.optString("type", "");
                switch (type) {
                    case "MATCH_REQUEST":
                        handleMatchRequest(clientSocket, data);
                        break;
                    case "GAME_OVER":
                        handleGameOver(data);
                        break;
                    case "GAME_UPDATE":
                        handleGameUpdate(data);
                        break;
                    default:
                        System.out.println("Unknown message type received: " + type);
                }
            });

            System.out.println("Server with matchmaking relay running...");
            System.in.read(); // Keep the server running until input is received.
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            server.stop();
        }
    }

    private void handleMatchRequest(Socket clientSocket, JSONObject data) {
        try {
            String username = data.getString("username");
            String gameType = data.getString("gameType");
            // Register the client's socket for future communications.
            MatchManager.addUser(username, clientSocket);
            // Retrieve the matchmaking instance for the specified game and enqueue the user.
            MatchmakingLogic mm = matchmakingInstances.get(gameType.toLowerCase());
            if (mm != null) {
                mm.playerQueuesForGame(username);
            } else {
                System.err.println("No matchmaking instance for game type: " + gameType);
            }
        } catch (Exception e) {
            System.err.println("Error handling MATCH_REQUEST: " + e.getMessage());
        }
    }

    private void handleGameOver(JSONObject data) {
        try {
            String username = data.getString("username");

            // Create a consolidated results JSON to record final game stats.
            JSONObject results = new JSONObject();
            results.put("username", username);
            results.put("gameType", data.getString("gameType"));
            results.put("winner", data.getString("winner"));

            // Adjust the original message for forwarding as a game update.
            data.remove("winner");
            data.remove("gameType");
            data.put("type", "GAME_UPDATE");

            if (data.has("playerOneTurns")) {
                results.put("playerOneTurns", data.getInt("playerOneTurns"));
                data.remove("playerOneTurns");
            } else if (data.has("playerTwoTurns")) {
                results.put("playerTwoTurns", data.getInt("playerTwoTurns"));
                data.remove("playerTwoTurns");
            }

            // Record game results and confirm game end.
            MatchManager.addWinnerInfo(results);
            MatchManager.confirmEnding(username);

            // Forward the game update to the opponent.
            Socket opponentSocket = MatchManager.getOpponentSocket(username);
            sendToSocket(opponentSocket, data, "GAME_OVER update forwarding from " + username);
        } catch (Exception e) {
            System.err.println("Error handling GAME_OVER: " + e.getMessage());
        }
    }

    private void handleGameUpdate(JSONObject data) {
        try {
            String username = data.getString("username");
            Socket opponentSocket = MatchManager.getOpponentSocket(username);
            sendToSocket(opponentSocket, data, "GAME_UPDATE forwarding from " + username);
        } catch (Exception e) {
            System.err.println("Error handling GAME_UPDATE: " + e.getMessage());
        }
    }

    private void sendToSocket(Socket socket, JSONObject data, String logMessage) {
        if (socket != null && !socket.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(data.toString());
                System.out.println("Forwarded update: " + logMessage);
            } catch (IOException e) {
                System.err.println("Error sending message: " + e.getMessage());
            }
        } else {
            System.out.println("No valid opponent socket to forward message: " + logMessage);
        }
    }
}
