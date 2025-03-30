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

    /**
     * Sort the leaderboard based on player scores (higher scores first)
     *
     * @param leaderboardData The leaderboard data to sort
     */
    private void sortLeaderboard(LeaderboardData leaderboardData) {
        List<String> playerIds = leaderboardData.getPlayerIds();
        List<Integer> playerScores = leaderboardData.getPlayerScores();

        // Simple bubble sort implementation
        for (int i = 0; i < playerIds.size() - 1; i++) {
            for (int j = 0; j < playerIds.size() - i - 1; j++) {
                // If the score at j is less than the score at j+1, swap them
                if (playerScores.get(j) < playerScores.get(j+1)) {
                    // Swap player IDs
                    String tempId = playerIds.get(j);
                    playerIds.set(j, playerIds.get(j+1));
                    playerIds.set(j+1, tempId);

                    // Swap player scores
                    Integer tempScore = playerScores.get(j);
                    playerScores.set(j, playerScores.get(j+1));
                    playerScores.set(j+1, tempScore);
                }
            }
        }

        // Update the leaderboard data with the sorted lists
        leaderboardData.setPlayerIds(playerIds);
        leaderboardData.setPlayerScores(playerScores);
    }

    /**
     * Displays the leaderboard for a specific game type
     *
     * @param gameType The type of game to display the leaderboard for
     */
    public void displayLeaderboard(String gameType) {
        // Check if parameter is valid
        if (gameType == null || gameType.isEmpty()) {
            throw new IllegalArgumentException("Game type cannot be null or empty");
        }

        // Get the leaderboard data
        LeaderboardData leaderboardData = leaderboardDatabase.getLeaderboard(gameType);
        if (leaderboardData == null) {
            System.out.println("No rankings available for game type: " + gameType);
            return;
        }

        List<String> playerIds = leaderboardData.getPlayerIds();
        List<Integer> playerScores = leaderboardData.getPlayerScores();

        if (playerIds.isEmpty()) {
            System.out.println("No rankings available for game type: " + gameType);
            return;
        }

        // Display header
        System.out.println("=== Leaderboard for " + gameType + " ===");
        System.out.println("Rank\tPlayer ID\tScore");
        System.out.println("--------------------------------------------");

        // Display each player in the rankings
        for (int i = 0; i < playerIds.size(); i++) {
            String playerId = playerIds.get(i);
            int score = playerScores.get(i);
            int rank = i + 1; // Rank is 1-based

            System.out.println(rank + "\t" + playerId + "\t" + score);
        }

        System.out.println("--------------------------------------------");
    }

    /**
     * Get the current rank
     *
     * @return The current rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Set the current rank
     *
     * @param rank The new rank
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * Get the leaderboard database
     *
     * @return The leaderboard database
     */
    public LeaderboardDatabase getLeaderboardDatabase() {
        return leaderboardDatabase;
    }

    /**
     * Set the leaderboard database
     *
     * @param leaderboardDatabase The new leaderboard database
     */
    public void setLeaderboardDatabase(LeaderboardDatabase leaderboardDatabase) {
        this.leaderboardDatabase = leaderboardDatabase;
    }
}
