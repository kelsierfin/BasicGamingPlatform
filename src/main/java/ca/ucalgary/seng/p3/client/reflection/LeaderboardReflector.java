package ca.ucalgary.seng.p3.client.reflection;

import ca.ucalgary.seng.p3.network.ClientSocketService;
import ca.ucalgary.seng.p3.network.Request;
import ca.ucalgary.seng.p3.network.Response;
import ca.ucalgary.seng.p3.client.models.ClientLeaderboardData;
import ca.ucalgary.seng.p3.client.models.ClientMatchData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Client-side reflector for leaderboard and player stats operations.
 * This class communicates with the server using the ClientSocketService.
 */
public class LeaderboardReflector {
    private static LeaderboardReflector instance;
    private final ClientSocketService socketService;
    private final Gson gson = new Gson();

    public static LeaderboardReflector getInstance() {
        if (instance == null) {
            instance = new LeaderboardReflector();
        }
        return instance;
    }

    private LeaderboardReflector() {
        this.socketService = ClientSocketService.getInstance();
    }

    /**
     * Get the leaderboard for a specific game type.
     *
     * @param gameType The type of game to get the leaderboard for
     * @return LeaderboardData containing player IDs and scores
     */
    public LeaderboardResult getLeaderboard(String gameType) {
        Request req = new Request("getLeaderboard", gameType, "");
        Response res = socketService.sendRequest(req);

        if (!res.isSuccess()) {
            return new LeaderboardResult(false, res.getMessage(), null);
        }

        // Parse leaderboard data from the response
        try {
            Map<String, Object> dataMap = gson.fromJson(res.getExtra(), new TypeToken<Map<String, Object>>(){}.getType());

            @SuppressWarnings("unchecked")
            List<String> playerIds = (List<String>) dataMap.get("playerIds");

            List<Integer> playerScores = new ArrayList<>();
            @SuppressWarnings("unchecked")
            List<Double> scores = (List<Double>) dataMap.get("playerScores");
            for (Double score : scores) {
                playerScores.add(score.intValue());
            }

            ClientLeaderboardData leaderboardData = new ClientLeaderboardData(playerIds, playerScores);
            return new LeaderboardResult(true, "Leaderboard retrieved successfully", leaderboardData);
        } catch (Exception e) {
            return new LeaderboardResult(false, "Error parsing leaderboard data: " + e.getMessage(), null);
        }
    }

    /**
     * Get player statistics for a specific user.
     *
     * @param username The username to get stats for
     * @return PlayerStatsResult containing player stats
     */
    public PlayerStatsResult getPlayerStats(String username) {
        Request req = new Request("getPlayerStats", username, "");
        Response res = socketService.sendRequest(req);

        if (!res.isSuccess()) {
            return new PlayerStatsResult(false, res.getMessage(), null);
        }

        try {
            Map<String, Object> statsMap = gson.fromJson(res.getExtra(), new TypeToken<Map<String, Object>>(){}.getType());

            int gamesPlayed = ((Double) statsMap.get("overallGamesPlayed")).intValue();
            int gamesWon = ((Double) statsMap.get("overallGamesWon")).intValue();
            double winRate = (Double) statsMap.get("overallWinRate");

            // Parse game-specific stats
            Map<String, Map<String, Object>> gameStatsMap = new HashMap<>();

            @SuppressWarnings("unchecked")
            Map<String, Object> stats = (Map<String, Object>) statsMap.get("gameStats");
            for (String game : stats.keySet()) {
                @SuppressWarnings("unchecked")
                Map<String, Object> gameStats = (Map<String, Object>) stats.get(game);
                gameStatsMap.put(game, gameStats);
            }

            return new PlayerStatsResult(true, "Player stats retrieved successfully",
                    gamesPlayed, gamesWon, winRate, gameStatsMap);
        } catch (Exception e) {
            return new PlayerStatsResult(false, "Error parsing player stats: " + e.getMessage(), null);
        }
    }

    /**
     * Get match history for a specific user.
     *
     * @param username The username to get match history for
     * @return MatchHistoryResult containing match history
     */
    public MatchHistoryResult getMatchHistory(String username) {
        Request req = new Request("getMatchHistory", username, "");
        Response res = socketService.sendRequest(req);

        if (!res.isSuccess()) {
            return new MatchHistoryResult(false, res.getMessage(), null);
        }

        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> matchDataList = gson.fromJson(res.getExtra(), new TypeToken<List<Map<String, Object>>>(){}.getType());

            List<ClientMatchData> matches = new ArrayList<>();
            for (Map<String, Object> matchMap : matchDataList) {
                String playerUsername = (String) matchMap.get("playerUsername");
                String opponentUsername = (String) matchMap.get("opponentUsername");
                String gameType = (String) matchMap.get("gameType");
                String outcome = (String) matchMap.get("outcome");
                int turns = ((Double) matchMap.get("turns")).intValue();

                matches.add(new ClientMatchData(playerUsername, opponentUsername, gameType, outcome, turns));
            }

            return new MatchHistoryResult(true, "Match history retrieved successfully", matches);
        } catch (Exception e) {
            return new MatchHistoryResult(false, "Error parsing match history: " + e.getMessage(), null);
        }
    }

    /**
     * Update player statistics after a game.
     *
     * @param player1 The username of the first player
     * @param player2 The username of the second player
     * @param player1Won Whether player1 won the game
     * @param gameType The type of game played
     * @param turns The number of turns played
     * @return StatsUpdateResult indicating success or failure
     */
    public StatsUpdateResult updatePlayerStats(String player1, String player2, boolean player1Won, String gameType, int turns) {
        String extraData = gameType + "," + player1Won + "," + turns;
        Request req = new Request("updatePlayerStats", player1, player2, extraData);
        Response res = socketService.sendRequest(req);

        return new StatsUpdateResult(res.isSuccess(), res.getMessage());
    }

    /**
     * Get leaderboard position for a player across all supported games.
     *
     * @param username The username to get positions for
     * @return LeaderboardPositionResult containing position information
     */
    public LeaderboardPositionResult getLeaderboardPositions(String username) {
        Request req = new Request("getLeaderboardPositions", username, "");
        Response res = socketService.sendRequest(req);

        if (!res.isSuccess()) {
            return new LeaderboardPositionResult(false, res.getMessage(), null);
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Map<String, Double>> positionsMap = gson.fromJson(res.getExtra(),
                    new TypeToken<Map<String, Map<String, Double>>>(){}.getType());

            // Convert Double values to Integer
            Map<String, Map<String, Integer>> positions = new HashMap<>();
            for (String game : positionsMap.keySet()) {
                Map<String, Double> gamePos = positionsMap.get(game);
                Map<String, Integer> convertedGamePos = new HashMap<>();

                for (String key : gamePos.keySet()) {
                    convertedGamePos.put(key, gamePos.get(key).intValue());
                }

                positions.put(game, convertedGamePos);
            }

            return new LeaderboardPositionResult(true, "Leaderboard positions retrieved successfully", positions);
        } catch (Exception e) {
            return new LeaderboardPositionResult(false, "Error parsing leaderboard positions: " + e.getMessage(), null);
        }
    }

    // Result classes for typed responses

    public static class LeaderboardResult {
        private final boolean success;
        private final String message;
        private final ClientLeaderboardData leaderboardData;

        public LeaderboardResult(boolean success, String message, ClientLeaderboardData leaderboardData) {
            this.success = success;
            this.message = message;
            this.leaderboardData = leaderboardData;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public ClientLeaderboardData getLeaderboardData() { return leaderboardData; }
    }

    public static class PlayerStatsResult {
        private final boolean success;
        private final String message;
        private final int overallGamesPlayed;
        private final int overallGamesWon;
        private final double overallWinRate;
        private final Map<String, Map<String, Object>> gameStats;

        public PlayerStatsResult(boolean success, String message,
                                 int overallGamesPlayed, int overallGamesWon, double overallWinRate,
                                 Map<String, Map<String, Object>> gameStats) {
            this.success = success;
            this.message = message;
            this.overallGamesPlayed = overallGamesPlayed;
            this.overallGamesWon = overallGamesWon;
            this.overallWinRate = overallWinRate;
            this.gameStats = gameStats;
        }

        // Constructor for error cases
        public PlayerStatsResult(boolean success, String message, Map<String, Object> dummy) {
            this.success = success;
            this.message = message;
            this.overallGamesPlayed = 0;
            this.overallGamesWon = 0;
            this.overallWinRate = 0;
            this.gameStats = new HashMap<>();
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public int getOverallGamesPlayed() { return overallGamesPlayed; }
        public int getOverallGamesWon() { return overallGamesWon; }
        public double getOverallWinRate() { return overallWinRate; }
        public Map<String, Map<String, Object>> getGameStats() { return gameStats; }
    }

    public static class MatchHistoryResult {
        private final boolean success;
        private final String message;
        private final List<ClientMatchData> matches;

        public MatchHistoryResult(boolean success, String message, List<ClientMatchData> matches) {
            this.success = success;
            this.message = message;
            this.matches = matches;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<ClientMatchData> getMatches() { return matches; }
    }

    public static class StatsUpdateResult {
        private final boolean success;
        private final String message;

        public StatsUpdateResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }

    public static class LeaderboardPositionResult {
        private final boolean success;
        private final String message;
        private final Map<String, Map<String, Integer>> positions;

        public LeaderboardPositionResult(boolean success, String message, Map<String, Map<String, Integer>> positions) {
            this.success = success;
            this.message = message;
            this.positions = positions;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Map<String, Map<String, Integer>> getPositions() { return positions; }
    }
}