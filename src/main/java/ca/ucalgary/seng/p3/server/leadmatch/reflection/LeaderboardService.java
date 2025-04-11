package ca.ucalgary.seng.p3.server.leadmatch.reflection;

import ca.ucalgary.seng.p3.server.leadmatch.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.*;

/**
 * Server-side service for handling leaderboard and player stats operations.
 * This class is the implementation that will be called via reflection from the ClientHandler.
 */
public class LeaderboardService {
    private final Gson gson = new Gson();

    /**
     * Get the leaderboard for a specific game type.
     *
     * @param gameType The type of game to get the leaderboard for
     * @return LeaderboardResult containing success status, message, and leaderboard data
     */
    public LeaderboardResult getLeaderboard(String gameType) {
        try {
            LeaderboardData leaderboardData = Leaderboard.getLeaderboard(gameType);

            if (leaderboardData == null || leaderboardData.getPlayerIds() == null || leaderboardData.getPlayerIds().isEmpty()) {
                return new LeaderboardResult(false, "No leaderboard data found for " + gameType, null);
            }

            return new LeaderboardResult(true, "Leaderboard retrieved successfully", leaderboardData);
        } catch (IOException e) {
            return new LeaderboardResult(false, "Error retrieving leaderboard: " + e.getMessage(), null);
        }
    }

    /**
     * Get player statistics for a specific user.
     *
     * @param username The username to get stats for
     * @return PlayerStatsResult containing success status, message, and player stats
     */
    public PlayerStatsResult getPlayerStats(String username) {
        try {
            PlayerStatData playerStats = PlayerStats.getPlayersStats(username);

            if (playerStats == null) {
                return new PlayerStatsResult(false, "No stats found for player " + username, null);
            }

            return new PlayerStatsResult(true, "Player stats retrieved successfully", playerStats);
        } catch (Exception e) {
            return new PlayerStatsResult(false, "Error retrieving player stats: " + e.getMessage(), null);
        }
    }

    /**
     * Get match history for a specific user.
     *
     * @param username The username to get match history for
     * @return MatchHistoryResult containing success status, message, and match history
     */
    public MatchHistoryResult getMatchHistory(String username) {
        try {
            PlayerStatData playerStats = PlayerStats.getPlayersStats(username);

            if (playerStats == null) {
                return new MatchHistoryResult(false, "No stats found for player " + username, null);
            }

            List<MatchData> matchHistory = playerStats.getMatchHistory();
            return new MatchHistoryResult(true, "Match history retrieved successfully", matchHistory);
        } catch (Exception e) {
            return new MatchHistoryResult(false, "Error retrieving match history: " + e.getMessage(), null);
        }
    }

    /**
     * Update player statistics after a game.
     *
     * @param player1 The username of the first player
     * @param player2 The username of the second player
     * @param extraData String in format "gameType,player1Won,turns"
     * @return StatsUpdateResult indicating success or failure
     */
    public StatsUpdateResult updatePlayerStats(String player1, String player2, String extraData) {
        try {
            // Parse the extra data
            String[] parts = extraData.split(",");
            if (parts.length != 3) {
                return new StatsUpdateResult(false, "Invalid extra data format");
            }

            String gameType = parts[0];
            boolean player1Won = Boolean.parseBoolean(parts[1]);
            int turns = Integer.parseInt(parts[2]);

            // Update player stats
            PlayerStats.updatePlayerStats(player1, player2, player1Won, gameType, turns);

            // Update the leaderboard rankings
            Leaderboard.updateRankings(gameType);

            return new StatsUpdateResult(true, "Player stats updated successfully");
        } catch (Exception e) {
            return new StatsUpdateResult(false, "Error updating player stats: " + e.getMessage());
        }
    }

    /**
     * Get leaderboard position for a player across all supported games.
     *
     * @param username The username to get positions for
     * @return LeaderboardPositionResult containing position information
     */
    public LeaderboardPositionResult getLeaderboardPositions(String username) {
        try {
            Map<String, Map<String, Integer>> positions = new HashMap<>();
            String[] gameTypes = {"Chess", "Connect4", "Go", "Tic Tac Toe"};

            for (String gameType : gameTypes) {
                LeaderboardData leaderboardData = Leaderboard.getLeaderboard(gameType);
                List<String> playerIds = leaderboardData.getPlayerIds();
                List<Integer> playerScores = leaderboardData.getPlayerScores();

                int index = playerIds.indexOf(username);
                if (index != -1) {
                    Map<String, Integer> gamePosition = new HashMap<>();
                    gamePosition.put("rank", index + 1);
                    gamePosition.put("elo", playerScores.get(index));
                    positions.put(gameType, gamePosition);
                }
            }

            if (positions.isEmpty()) {
                return new LeaderboardPositionResult(false, "No leaderboard positions found for " + username, null);
            }

            return new LeaderboardPositionResult(true, "Leaderboard positions retrieved successfully", positions);
        } catch (Exception e) {
            return new LeaderboardPositionResult(false, "Error retrieving leaderboard positions: " + e.getMessage(), null);
        }
    }

    // Result classes for typed responses

    public static class LeaderboardResult {
        private final boolean success;
        private final String message;
        private final LeaderboardData leaderboardData;
        private final String leaderboardJson;

        public LeaderboardResult(boolean success, String message, LeaderboardData leaderboardData) {
            this.success = success;
            this.message = message;
            this.leaderboardData = leaderboardData;

            // Serialize the data for network transfer
            if (leaderboardData != null) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("playerIds", leaderboardData.getPlayerIds());
                dataMap.put("playerScores", leaderboardData.getPlayerScores());
                this.leaderboardJson = new Gson().toJson(dataMap);
            } else {
                this.leaderboardJson = null;
            }
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public LeaderboardData getLeaderboardData() { return leaderboardData; }
        public String getLeaderboardJson() { return leaderboardJson; }
    }

    public static class PlayerStatsResult {
        private final boolean success;
        private final String message;
        private final PlayerStatData playerStats;
        private final String statsJson;

        public PlayerStatsResult(boolean success, String message, PlayerStatData playerStats) {
            this.success = success;
            this.message = message;
            this.playerStats = playerStats;

            // Serialize the data for network transfer
            if (playerStats != null) {
                Map<String, Object> statsMap = new HashMap<>();
                statsMap.put("overallGamesPlayed", playerStats.getOverallGamesPlayed());
                statsMap.put("overallGamesWon", playerStats.getOverallGamesWon());
                statsMap.put("overallWinRate", playerStats.getOverallWinRate());

                // Include game-specific stats
                Map<String, Object> gameStatsMap = new HashMap<>();
                for (String game : new String[]{"chess", "connect4", "go", "tictactoe"}) {
                    GameStats gameStats = playerStats.getGameStats(game);
                    Map<String, Object> gameData = new HashMap<>();
                    gameData.put("gamesPlayed", gameStats.getGamesPlayed());
                    gameData.put("gamesWon", gameStats.getGamesWon());
                    gameData.put("winRate", gameStats.getWinRate());
                    gameData.put("elo", gameStats.getELO());
                    gameData.put("rank", gameStats.getRank());
                    gameStatsMap.put(game, gameData);
                }
                statsMap.put("gameStats", gameStatsMap);

                this.statsJson = new Gson().toJson(statsMap);
            } else {
                this.statsJson = null;
            }
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public PlayerStatData getPlayerStats() { return playerStats; }
        public String getStatsJson() { return statsJson; }
    }

    public static class MatchHistoryResult {
        private final boolean success;
        private final String message;
        private final List<MatchData> matches;
        private final String matchesJson;

        public MatchHistoryResult(boolean success, String message, List<MatchData> matches) {
            this.success = success;
            this.message = message;
            this.matches = matches;

            // Serialize the data for network transfer
            if (matches != null) {
                this.matchesJson = new Gson().toJson(matches);
            } else {
                this.matchesJson = null;
            }
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public List<MatchData> getMatches() { return matches; }
        public String getMatchesJson() { return matchesJson; }
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
        private final String positionsJson;

        public LeaderboardPositionResult(boolean success, String message, Map<String, Map<String, Integer>> positions) {
            this.success = success;
            this.message = message;
            this.positions = positions;

            // Serialize the data for network transfer
            if (positions != null) {
                this.positionsJson = new Gson().toJson(positions);
            } else {
                this.positionsJson = null;
            }
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Map<String, Map<String, Integer>> getPositions() { return positions; }
        public String getPositionsJson() { return positionsJson; }
    }
}