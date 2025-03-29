package Leaderboard_and_Matchmaking_Classes;

import java.util.List;

public class Leaderboard {
    // Field
    private int rank;
    private LeaderboardDatabase leaderboardDatabase;

    // Constructor
    public Leaderboard() {
        this.rank = 0; // Default rank
        this.leaderboardDatabase = new LeaderboardDatabase();
    }

    /**
     * Updates the leaderboard for a specific game type with a player's profile information
     *
     * @param gameType The type of game (e.g., "chess", "tictactoe", "connectfour", "go")
     * @param profile The player's profile to update
     * @param playerProfile Additional player-specific profile information
     * @return The updated rank of the player
     */
    public int updateLeaderboard(String gameType, String playerId, int score) {
        // Check if parameters are valid
        if (gameType == null || gameType.isEmpty()) {
            throw new IllegalArgumentException("Game type cannot be null or empty");
        }
        if (playerId == null || playerId.isEmpty()) {
            throw new IllegalArgumentException("Player ID cannot be null or empty");
        }

        // Get the leaderboard for the specific game type
        LeaderboardData leaderboardData = leaderboardDatabase.getLeaderboard(gameType);

        // If leaderboard doesn't exist for this game type, create it
        if (leaderboardData == null) {
            leaderboardData = new LeaderboardData();
            leaderboardDatabase.addLeaderboard(gameType, leaderboardData);
        }

        // Check if player already exists in the leaderboard
        List<String> playerIds = leaderboardData.getPlayerIds();
        List<Integer> playerScores = leaderboardData.getPlayerScores();

        int playerIndex = -1;
        for (int i = 0; i < playerIds.size(); i++) {
            if (playerIds.get(i).equals(playerId)) {
                playerIndex = i;
                break;
            }
        }

        // Add or update player
        if (playerIndex == -1) {
            // New player, add to leaderboard
            playerIds.add(playerId);
            playerScores.add(score);
        } else {
            // Existing player, update score
            playerScores.set(playerIndex, score);
        }

        // Update the leaderboard data
        leaderboardData.setPlayerIds(playerIds);
        leaderboardData.setPlayerScores(playerScores);

        // Sort the leaderboard based on scores (higher scores first)
        sortLeaderboard(leaderboardData);

        // Find player's new position (rank)
        for (int i = 0; i < playerIds.size(); i++) {
            if (playerIds.get(i).equals(playerId)) {
                this.rank = i + 1; // +1 because ranks start at 1, not 0
                break;
            }
        }

        return this.rank;
    }
