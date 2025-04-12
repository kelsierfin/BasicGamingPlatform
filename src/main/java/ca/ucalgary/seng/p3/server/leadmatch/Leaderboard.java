package ca.ucalgary.seng.p3.server.leadmatch;

import java.io.IOException;
import java.util.List;

public class Leaderboard {


    /**
     * this updates players rank according to the players elo
     * used after a game to update the leaderboard/players ranks
     *
     * @param gameType
     * @throws IOException
     */
    public static void updateRankings(String gameType) throws IOException {
        LeaderboardData leaderboard = DatabaseStub.getLeaderboard(gameType);
        Leaderboard.sortLeaderboard(leaderboard);
        DatabaseStub.updateRanking(gameType, leaderboard);
    }

    /**
     * Gets the size + 1 of the leaderboard for a game. this is used when adding a new account to set their rank to the lowest rank
     *
     * @param gameType
     * @return
     * @throws IOException
     */
    public static int lastPosition(String gameType) throws IOException {
        LeaderboardData leaderboard = DatabaseStub.getLeaderboard(gameType);
        return leaderboard.getlast() + 1;
    }

    /**
     * Sort the leaderboard based on player scores (higher scores first)
     *
     * @param leaderboardData The leaderboard data to sort
     */
    private static void sortLeaderboard(LeaderboardData leaderboardData) {
        List<String> playerIds = leaderboardData.getPlayerIds();
        List<Integer> playerScores = leaderboardData.getPlayerScores();

        // Simple bubble sort implementation
        for (int i = 0; i < playerIds.size() - 1; i++) {
            for (int j = 0; j < playerIds.size() - i - 1; j++) {
                // If the score at j is less than the score at j+1, swap them
                if (playerScores.get(j) < playerScores.get(j + 1)) {
                    // Swap player IDs
                    String tempId = playerIds.get(j);
                    playerIds.set(j, playerIds.get(j + 1));
                    playerIds.set(j + 1, tempId);

                    // Swap player scores
                    Integer tempScore = playerScores.get(j);
                    playerScores.set(j, playerScores.get(j + 1));
                    playerScores.set(j + 1, tempScore);
                }
            }
        }

        // Update the leaderboard data with the sorted lists
        leaderboardData.setPlayerIds(playerIds);
        leaderboardData.setPlayerScores(playerScores);
    }

    /**
     * gets the leaderboard data of a game that holds 2 arrays first array holds the players usernames with the index of the player being their rank
     * (ie playerIds[0] is the rank 1 player) the player scores array holds the associated players ELO
     * This is to be called by the gui team to get the leaderboard that needs to be displayed.
     *
     * @param gameType
     * @return
     * @throws IOException
     */
    public static LeaderboardData getLeaderboard(String gameType) throws IOException {
        LeaderboardData leaderboard = DatabaseStub.getLeaderboard(gameType);
        Leaderboard.sortLeaderboard(leaderboard);
        return leaderboard;
    }

//    /**
//     * Displays the leaderboard for a specific game type
//     *
//     * @param gameType The type of game to display the leaderboard for
//     */
//    public static void displayLeaderboard(String gameType) throws IOException {
//        // Check if parameter is valid
//        if (gameType == null || gameType.isEmpty()) {
//            throw new IllegalArgumentException("Game type cannot be null or empty");
//        }
//
//        // Get the leaderboard data
//        LeaderboardData leaderboardData = PlayerStatDatabase.getLeaderboard(gameType);
//        if (leaderboardData == null) {
//            System.out.println("No rankings available for game type: " + gameType);
//            return;
//        }
//        Leaderboard.sortLeaderboard(leaderboardData);
//        List<String> playerIds = leaderboardData.getPlayerIds();
//        List<Integer> playerScores = leaderboardData.getPlayerScores();
//
//        if (playerIds.isEmpty()) {
//            System.out.println("No rankings available for game type: " + gameType);
//            return;
//        }
//
//        // Display header
//        System.out.println("=== Leaderboard for " + gameType + " ===");
//        System.out.println("Rank\tPlayer ID\tScore");
//        System.out.println("--------------------------------------------");
//
//        // Display each player in the rankings
//        for (int i = 0; i < playerIds.size(); i++) {
//            String playerId = playerIds.get(i);
//            int score = playerScores.get(i);
//            int rank = i + 1; // Rank is 1-based
//
//            System.out.println(rank + "\t" + playerId + "\t" + score);
//        }
//
//        System.out.println("--------------------------------------------");
//    }
}
