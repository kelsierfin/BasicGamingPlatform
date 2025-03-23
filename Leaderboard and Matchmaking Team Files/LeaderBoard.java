import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

// TODO: Import Profile class from Authentication team once available

public class Leaderboard {
    // Field
    private int rank;

    // Constructor
    public Leaderboard() {
        this.rank = 0; // Default rank
    }

    /**
     * Updates the leaderboard for a specific game type with a player's profile information
     *
     * @param gameType The type of game (e.g., "chess", "tictactoe", "connectfour", "go")
     * @param profile The player's profile to update
     * @param playerProfile Additional player-specific profile information
     * @return The updated rank of the player
     * TODO: Update implementation when Authentication team has uploaded the Profile class
     */
    public int updateLeaderboard(String gameType, Profile profile, Profile playerProfile) {
        // Check if parameters are valid
        if (gameType == null || gameType.isEmpty()) {
            throw new IllegalArgumentException("Game type cannot be null or empty");
        }
        if (profile == null || playerProfile == null) {
            throw new IllegalArgumentException("Profile and playerProfile cannot be null");
        }

        // TODO: Implement connection to LeaderboardDatabase once available
        // In a real implementation, this would:
        // 1. Get the leaderboard for the specific game type from LeaderboardDatabase
        // 2. Update the player's information in that leaderboard
        // 3. Recalculate rankings
        // 4. Return the player's new rank

        // For now, use a placeholder rank calculation
        // In a real implementation, this would be calculated based on player stats
        this.rank = 1; // Placeholder

        // Return the updated rank
        return this.rank;
    }

    /**
     * Displays the leaderboard for a specific game type
     *
     * @param gameType The type of game to display the leaderboard for (e.g., "chess", "tictactoe", "connectfour", "go")
     * TODO: Update implementation when Authentication team has uploaded the Profile class
     */
    public void displayLeaderboard(String gameType) {
        // Check if parameter is valid
        if (gameType == null || gameType.isEmpty()) {
            throw new IllegalArgumentException("Game type cannot be null or empty");
        }

        // TODO: Get leaderboard data from LeaderboardDatabase
        // In a real implementation, this would be:
        // LeaderboardData leaderboardData = leaderboardDatabase.getGameLeaderboard(gameType);
        // List<Profile> rankings = leaderboardData.getRankings();

        // Placeholder for now
        List<Profile> rankings = new ArrayList<>(); // Empty list as placeholder

        // Check if there are any rankings to display
        if (rankings.isEmpty()) {
            System.out.println("No rankings available for game type: " + gameType);
            return;
        }

        // Display header
        System.out.println("=== Leaderboard for " + gameType + " ===");
        System.out.println("Rank\tUsername\tWins\tLosses\tWin Rate");
        System.out.println("--------------------------------------------");

        // Display each player in the rankings
        for (int i = 0; i < rankings.size(); i++) {
            Profile profile = rankings.get(i);
            // TODO: Once User and Profile classes are available, uncomment and adjust:
            // User user = profile.getUser();
            // String username = user.getUsername();
            // int wins = profile.getWinsForGameType(gameType);
            // int losses = profile.getLossesForGameType(gameType);
            // double winRate = wins > 0 ? (double)wins / (wins + losses) * 100 : 0;

            // Display with placeholder values for now
            System.out.println((i + 1) + "\t" + "Player" + i + "\t" + "0\t0\t0.0%");
        }

        System.out.println("--------------------------------------------");
    }
}