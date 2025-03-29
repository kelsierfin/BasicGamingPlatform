import java.util.ArrayList;
import java.util.List;

public class MatchHistory {
    private List<MatchData> allMatches;  // Stores all matches played globally

    // Constructor
    public MatchHistory() {
        this.allMatches = new ArrayList<>();
    }

    // Add a match to the global match history
    public void addMatch(MatchData match) {
        allMatches.add(match);
    }

    // Get the match history for a specific player
    public List<MatchData> getMatchHistoryForPlayer(GameStats player) {
        List<MatchData> playerMatches = new ArrayList<>();
        for (MatchData match : allMatches) {
            if (match.getPlayers().contains(player)) { // Check if the player participated in the match
                playerMatches.add(match);
            }
        }
        return playerMatches;
    }

    // Display match history for a specific player
    public void displayMatchHistoryForPlayer(GameStats player) {
        List<MatchData> playerMatches = getMatchHistoryForPlayer(player);
        System.out.println("Match History for Player (ELO: " + player.getELO() + "):");

        if (playerMatches.isEmpty()) {
            System.out.println("No matches played yet.");
            return;
        }

        for (int i = 0; i < playerMatches.size(); i++) {
            System.out.println("Match " + (i + 1) + ":");
            for (GameStats stats : playerMatches.get(i).getPlayers()) {
                System.out.println(" - Player ELO: " + stats.getELO() + ", Games Played: " + stats.getGamesPlayed() + ", Wins: " + stats.getGamesWon());
            }
        }
    }
}
