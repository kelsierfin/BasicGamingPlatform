import java.util.ArrayList;
import java.util.List;

public class MatchHistory {

    private User user;
    // Store multiple matches using a List
    private List<Match> matches;

    // Constructor
    public MatchHistory(User user) {
        this.user = user;
        this.matches = new ArrayList<>(); // Initialize the list
    }

    // Add a match to the history
    public void addMatch(Match match) {
        matches.add(match);
    }

    // Remove a match from the history
    public void removeMatch(Match match) {
        matches.remove(match);
    }

    // Return the entire match history for this user
    public List<Match> getMatchHistory() {
        return matches;
    }
}
