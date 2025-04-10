package ca.ucalgary.seng.p3.server.authentication;

import ca.ucalgary.seng.p3.server.leadmatch.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPlayerProfile {

    public static Map<String, Object> getPlayerStats(String username) throws IOException {
        PlayerStatData playerStats = DatabaseStub.getPlayerStatData(username);
        Map<String, Object> stats = new HashMap<>();

        stats.put("username", username);
        stats.put("overallGamesPlayed", playerStats.getOverallGamesPlayed());
        stats.put("overallGamesWon", playerStats.getOverallGamesWon());
        stats.put("overallWinRate", playerStats.getOverallWinRate());

        stats.put("chessStats", getGameStats(playerStats.getGameStats("chess")));
        stats.put("connect4Stats", getGameStats(playerStats.getGameStats("connect4")));
        stats.put("goStats", getGameStats(playerStats.getGameStats("go")));
        stats.put("tictactoeStats", getGameStats(playerStats.getGameStats("tictactoe")));

        // Add match history to stats
        stats.put("matchHistory", getMatchHistoryData(username));

        return stats;

    }

    public static List<String[]> getMatchHistoryData(String username) {
        Match_History matchHistory = new Match_History(username);
        return  matchHistory.viewMatchHistory();
    }

    public static Map<String, Map<String, Integer>> getLeaderboardPositions(String username) throws IOException {
        Map<String, Map<String, Integer>> positions = new HashMap<>();

        positions.put("chess", getLeaderboardPosition(username, "chess"));
        positions.put("connect4", getLeaderboardPosition(username, "connect4"));
        positions.put("go", getLeaderboardPosition(username, "go"));
        positions.put("tictactoe", getLeaderboardPosition(username, "tictactoe"));

        return positions;
    }

    public static Map<String, Integer> getGameStats(GameStats gameStats) {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("gamesPlayed", gameStats.getGamesPlayed());
        stats.put("gamesWon", gameStats.getGamesWon());
        stats.put("elo", gameStats.getELO());
        stats.put("rank", gameStats.getRank());
        stats.put("winRate", (int)(gameStats.getWinRate()));
        return stats;
    }


    private static Map<String, Integer> getLeaderboardPosition(String username, String gameType) throws IOException {
        LeaderboardData leaderboard = DatabaseStub.getLeaderboard(gameType);
        int index = leaderboard.getPlayerIds().indexOf(username);

        Map<String, Integer> position = new HashMap<>();
        position.put("rank", index != -1 ? index + 1 : -1);
        position.put("elo", index != -1 ? leaderboard.getPlayerScores().get(index) : 0);

        return position;
    }
    public static String getFormattedMatchHistory(String username) {
        List<String[]> rawHistory = getMatchHistoryData(username);
        return Match_History.matchHistoryToString(rawHistory);
    }

    public static void main(String[] args) {
        try {
            String testPlayer = "player2";

            System.out.println("=== Player Statistics for " + testPlayer + " ===");
            System.out.println("--------------------------------------------");

            // Get all stats
            Map<String, Object> playerStats = getPlayerStats(testPlayer);
            Map<String, Map<String, Integer>> leaderboardPositions = getLeaderboardPositions(testPlayer);

            // Overall stats
            System.out.println("Overall Statistics:");
            System.out.println("Games Played: " + playerStats.get("overallGamesPlayed"));
            System.out.println("Games Won: " + playerStats.get("overallGamesWon"));
            System.out.printf("Win Rate: %.2f%%\n", playerStats.get("overallWinRate"));
            System.out.println();

            // Game-specific stats
            System.out.println("Game-Specific Statistics:");
            printGameStats("Chess", (Map<String, Integer>) playerStats.get("chessStats"));
            printGameStats("Connect 4", (Map<String, Integer>) playerStats.get("connect4Stats"));
            printGameStats("Go", (Map<String, Integer>) playerStats.get("goStats"));
            printGameStats("Tic-Tac-Toe", (Map<String, Integer>) playerStats.get("tictactoeStats"));

            // Match history
            System.out.println("\nMatch History:");
            System.out.println(getFormattedMatchHistory(testPlayer));

            // Leaderboard positions
            System.out.println("\nLeaderboard Positions:");
            System.out.printf("  %-12s: Rank %d (ELO: %d)\n", "Chess",
                    leaderboardPositions.get("chess").get("rank"),
                    leaderboardPositions.get("chess").get("elo"));
            System.out.printf("  %-12s: Rank %d (ELO: %d)\n", "Connect4",
                    leaderboardPositions.get("connect4").get("rank"),
                    leaderboardPositions.get("connect4").get("elo"));
            System.out.printf("  %-12s: Rank %d (ELO: %d)\n", "Go",
                    leaderboardPositions.get("go").get("rank"),
                    leaderboardPositions.get("go").get("elo"));
            System.out.printf("  %-12s: Rank %d (ELO: %d)\n", "Tictactoe",
                    leaderboardPositions.get("tictactoe").get("rank"),
                    leaderboardPositions.get("tictactoe").get("elo"));

        } catch (IOException e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printGameStats(String gameName, Map<String, Integer> stats) {
        System.out.println("  " + gameName + ":");
        System.out.println("    Games Played: " + stats.get("gamesPlayed"));
        System.out.println("    Games Won: " + stats.get("gamesWon"));
        System.out.printf("    Win Rate: %.2f%%\n", stats.get("winRate").doubleValue());
        System.out.println("    ELO Rating: " + stats.get("elo"));
        System.out.println("    Rank: " + stats.get("rank"));
        System.out.println();
    }
}