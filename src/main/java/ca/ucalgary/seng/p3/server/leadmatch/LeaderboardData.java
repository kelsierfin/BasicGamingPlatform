package ca.ucalgary.seng.p3.server.leadmatch;

import java.util.List;

/**
 * This class represents the data structure for a specific game's leaderboard
 * Designed as a simple data container
 */
public class LeaderboardData {
    // Fields to store player IDs and scores
    private List<String> playerIds;
    private List<Integer> playerScores;

    /**
     * Constructor for LeaderboardData
     */
    public LeaderboardData(List<String> playerIds, List<Integer> playerScores) {
        this.playerIds = playerIds;
        this.playerScores = playerScores;
    }

    /**
     * Get the list of player IDs
     *
     * @return List of player IDs
     */
    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void addPlayerId(String playerId) {
        this.playerIds.add(playerId);
    }

    public void addPlayerScore(int playerScore) {
        this.playerScores.add(playerScore);
    }

    public int getlast(){
        return this.playerIds.size();
    }

    /**
     * Set the list of player IDs
     *
     * @param playerIds The new list of player IDs
     */
    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }

    /**
     * Get the list of player scores
     *
     * @return List of player scores
     */
    public List<Integer> getPlayerScores() {
        return playerScores;
    }

    /**
     * Set the list of player scores
     *
     * @param playerScores The new list of player scores
     */
    public void setPlayerScores(List<Integer> playerScores) {
        this.playerScores = playerScores;
    }
}