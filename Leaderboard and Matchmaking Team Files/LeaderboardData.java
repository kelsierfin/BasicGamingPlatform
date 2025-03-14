import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

// TODO: Import Profile and User classes from Authentication team once available
// import [your-project-package].Profile;
// import [your-project-package].User;

public class LeaderboardData {
    // Field for storing player rankings
    private List<Profile> rankings;  // TODO: Update when Authentication team has uploaded the Profile class

    // Constructor
    public LeaderboardData() {
        this.rankings = new ArrayList<>();
    }

    /**
     * Adds player data to the leaderboard rankings.
     *
     * @param profile The player's profile being added to the leaderboard
     * @param playerProfile The player's game-specific profile with stats
     * TODO: Update implementation when Authentication team has uploaded the Profile class
     */
    public void addData(Profile profile, Profile playerProfile) {
        // Check if parameters are valid
        if (profile == null || playerProfile == null) {
            throw new IllegalArgumentException("Profile and playerProfile cannot be null");
        }

        // Check if player already exists in the rankings
        boolean playerExists = false;
        for (int i = 0; i < rankings.size(); i++) {
            Profile existingProfile = rankings.get(i);
            if (existingProfile.equals(profile)) {
                // Update existing profile with new data
                rankings.set(i, profile);
                playerExists = true;
                break;
            }
        }

        // If player doesn't exist in rankings, add them
        if (!playerExists) {
            rankings.add(profile);
        }

        // Recalculate rankings for all profiles
        recalculateRankings();
    }

    /**
     * Recalculates rankings for all profiles and updates their rank fields.
     * TODO: Update once Profile and User classes are available from Authentication team
     */
    private void recalculateRankings() {
        // Sort profiles based on game statistics (win rate, score, etc.)
        Collections.sort(rankings, (p1, p2) -> {
            // TODO: Implement proper comparison logic using game statistics
            // This might use data from match history, win rates, etc.
            // Example (once methods are available):
            // return Integer.compare(p2.getUser().getMatchHistory().getWinCount(),
            //                        p1.getUser().getMatchHistory().getWinCount());

            // Temporary placeholder return to avoid compilation errors
            return 0; // No sorting for now
        });

        // Update rank in each User object based on their position in the sorted list
        for (int i = 0; i < rankings.size(); i++) {
            Profile profile = rankings.get(i);
            // TODO: Once User and Profile classes are available, uncomment and adjust:
            // User user = profile.getUser();
            // user.setRank(i + 1);  // Ranks start at 1
        }
    }
}