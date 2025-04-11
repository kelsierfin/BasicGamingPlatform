package ca.ucalgary.seng.p3.client.reflection;

import ca.ucalgary.seng.p3.network.ClientSocketService;
import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import java.util.Map;

/**
 * Client-side reflector for matchmaking operations.
 * This class communicates with the server using the ClientSocketService.
 */
public class MatchmakingReflector {
    private static MatchmakingReflector instance;
    private final ClientSocketService socketService;
    private final Gson gson = new Gson();

    public static MatchmakingReflector getInstance() {
        if (instance == null) {
            instance = new MatchmakingReflector();
        }
        return instance;
    }

    private MatchmakingReflector() {
        this.socketService = ClientSocketService.getInstance();
    }

    /**
     * Match players for a game.
     *
     * @param player1 First player's username
     * @param player2 Second player's username
     * @param gameType Type of game to match
     * @return MatchResult containing match information
     */
    public MatchResult matchPlayers(String player1, String player2, String gameType) {
        Request req = new Request("matchPlayers", player1, player2, gameType);
        Response res = socketService.sendRequest(req);

        return new MatchResult(res.isSuccess(), res.getMessage(),
                res.isSuccess() ? gameType : null,
                res.isSuccess() ? player1 : null,
                res.isSuccess() ? player2 : null);
    }

    /**
     * Queue up for a game and wait for matchmaking.
     *
     * @param player Player looking for a match
     * @param gameType Type of game to queue for
     * @return QueueResult containing queue status
     */
    public QueueResult queueForGame(String player, String gameType) {
        Request req = new Request("queueForGame", player, gameType);
        Response res = socketService.sendRequest(req);

        return new QueueResult(res.isSuccess(), res.getMessage());
    }

    /**
     * Find a match based on ELO ranking.
     *
     * @param player Player looking for a match
     * @param gameType Type of game to match
     * @return FindMatchResult containing match information if a match was found
     */
    public FindMatchResult findMatch(String player, String gameType) {
        Request req = new Request("findMatch", player, gameType);
        Response res = socketService.sendRequest(req);

        if (!res.isSuccess()) {
            return new FindMatchResult(false, res.getMessage(), null, null);
        }

        try {
            if (res.getExtra() == null) {
                return new FindMatchResult(true, res.getMessage(),
                        res.getUsername(), // player1
                        null); // player2
            }

            // Parse match data if included
            Map<String, String> matchData = gson.fromJson(res.getExtra(),
                    new TypeToken<Map<String, String>>(){}.getType());

            String player1 = matchData.getOrDefault("player1", null);
            String player2 = matchData.getOrDefault("player2", null);

            return new FindMatchResult(true, res.getMessage(), player1, player2);
        } catch (Exception e) {
            return new FindMatchResult(false, "Error parsing match data: " + e.getMessage(), null, null);
        }
    }

    // Result classes for typed responses

    public static class MatchResult {
        private final boolean success;
        private final String message;
        private final String gameType;
        private final String player1;
        private final String player2;

        public MatchResult(boolean success, String message, String gameType, String player1, String player2) {
            this.success = success;
            this.message = message;
            this.gameType = gameType;
            this.player1 = player1;
            this.player2 = player2;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getGameType() { return gameType; }
        public String getPlayer1() { return player1; }
        public String getPlayer2() { return player2; }
    }

    public static class QueueResult {
        private final boolean success;
        private final String message;

        public QueueResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class FindMatchResult {
        private final boolean success;
        private final String message;
        private final String player1;
        private final String player2;

        public FindMatchResult(boolean success, String message, String player1, String player2) {
            this.success = success;
            this.message = message;
            this.player1 = player1;
            this.player2 = player2;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getPlayer1() { return player1; }
        public String getPlayer2() { return player2; }
    }
}