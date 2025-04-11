package ca.ucalgary.seng.p3.client.models;

import java.util.List;

/**
 * Client-side representation of leaderboard data.
 * This class mirrors the server-side LeaderboardData but lives on the client.
 */
public class ClientLeaderboardData {
    private List<String> playerIds;
    private List<Integer> playerScores;

    public ClientLeaderboardData(List<String> playerIds, List<Integer> playerScores) {
        this.playerIds = playerIds;
        this.playerScores = playerScores;
    }

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }

    public List<Integer> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(List<Integer> playerScores) {
        this.playerScores = playerScores;
    }

    public int getlast() {
        return this.playerIds.size();
    }
}