package ca.ucalgary.seng.p3.server;

import org.json.JSONObject;
import ca.ucalgary.seng.p3.server.leadmatch.PlayerStats;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MatchManager {
    // Thread-safe maps to hold socket and match pairings.
    private static final Map<String, Socket> userSocketMap = new ConcurrentHashMap<>();
    private static final Map<String, String> matchedPairs = new ConcurrentHashMap<>();

    public static void addUser(String username, Socket socket) {
        userSocketMap.put(username, socket);
        System.out.println("Registered user: " + username + " at " + socket.getRemoteSocketAddress());
    }

    public static Socket getSocket(String username) {
        return userSocketMap.get(username);
    }

    public static void addMatch(String username1, String username2) {
        matchedPairs.put(username1, username2);
        matchedPairs.put(username2, username1);
        System.out.println("Match established: " + username1 + " ↔ " + username2);

        // Prepare MATCH_FOUND messages.
        JSONObject matchMessage1 = new JSONObject();
        matchMessage1.put("type", "MATCH_FOUND");
        matchMessage1.put("opponent", username2);
        matchMessage1.put("playerOneLocal", true);

        JSONObject matchMessage2 = new JSONObject();
        matchMessage2.put("type", "MATCH_FOUND");
        matchMessage2.put("opponent", username1);
        matchMessage2.put("playerOneLocal", false);

        sendMessageToUser(username1, matchMessage1);
        sendMessageToUser(username2, matchMessage2);
    }

    public static String getOpponentUsername(String username) {
        return matchedPairs.get(username);
    }

    public static Socket getOpponentSocket(String username) {
        String opponent = getOpponentUsername(username);
        return opponent != null ? getSocket(opponent) : null;
    }

    public static void removeUser(String username) {
        userSocketMap.remove(username);
        String opponent = matchedPairs.remove(username);
        if (opponent != null) {
            matchedPairs.remove(opponent);
        }
    }

    // Map to store game results for confirmation.
    private static final Map<String, JSONObject> confirmWinner = new ConcurrentHashMap<>();

    public static void addWinnerInfo(JSONObject results) {
        String username = results.getString("username");
        confirmWinner.put(username, results);
        System.out.println("Recorded results for " + username + ": " + results);
    }

    public static void confirmEnding(String username) throws IOException {
        String opponent = getOpponentUsername(username);
        if (opponent == null) {
            System.err.println("No opponent found for " + username + " in confirmEnding.");
            return;
        }
        if (confirmWinner.containsKey(username) && confirmWinner.containsKey(opponent)) {
            JSONObject userResult = confirmWinner.get(username);
            JSONObject opponentResult = confirmWinner.get(opponent);
            if (userResult.getString("gameType").equals(opponentResult.getString("gameType"))) {
                if (userResult.getString("winner").equals(opponentResult.getString("winner"))) {
                    int turns = userResult.has("playerOneTurns")
                            ? userResult.getInt("playerOneTurns")
                            : userResult.has("playerTwoTurns") ? userResult.getInt("playerTwoTurns") : 0;
                    boolean player1Won = username.equals(userResult.getString("winner"));
                    PlayerStats.updatePlayerStats(username, opponent, player1Won, userResult.getString("gameType"), turns);
                    System.out.println("Game ending confirmed and stats updated for " + username + " and " + opponent);
                    confirmWinner.remove(username);
                    confirmWinner.remove(opponent);
                } else {
                    System.err.println("Mismatch in winner reported for " + username + " and " + opponent);
                }
            } else {
                System.err.println("Mismatch in game type reported by " + username + " and " + opponent);
            }
        }
    }

    private static void sendMessageToUser(String username, JSONObject message) {
        Socket socket = getSocket(username);
        if (socket != null && !socket.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message.toString());
                System.out.println("Sent message to " + username + ": " + message);
            } catch (IOException e) {
                System.err.println("Error sending message to " + username + ": " + e.getMessage());
            }
        } else {
            System.err.println("Socket for " + username + " is unavailable or closed.");
        }
    }
}
