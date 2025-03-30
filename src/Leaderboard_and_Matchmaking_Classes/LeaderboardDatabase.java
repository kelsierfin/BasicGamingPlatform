package Leaderboard_and_Matchmaking_Classes;

import java.util.HashMap;

/**
 * This class is a stub for the database.
 * The real database will likely combine all the database classes that we have.
 * Making this class more represent a table in the database that holds the leaderboards with the LeaderboardData class more representing the schema and data in this table.
 */
public class LeaderboardDatabase {

    private HashMap<String, LeaderboardData> leaderboard;


    public LeaderboardDatabase() {
        this.leaderboard = new HashMap<>();
    }


    /**
     * adds a new leaderboard.
     * can be used if a new game gets added to our system or one of the games doesn't have a leaderboard
     *
     * @param gametype
     * @param leaderboardData
     */
    public void addLeaderboard(String gametype, LeaderboardData leaderboardData){

        // checks if entered parameters are null
        if (gametype == null || leaderboardData == null) {
            throw new IllegalArgumentException("gametype and leaderboard cannot be null");
        }

        // Check if a leaderboard for that game already exists
        boolean leaderboardExists = false;
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.containsKey(gametype)) {
                leaderboardExists = true;
                break;
            }
        }
        if (!leaderboardExists) {
            leaderboard.put(gametype, leaderboardData);
        }
    }

    /**
     * returns the leaderboard data for a game
     * @param gametype
     * @return LeaderboardData
     */
    public LeaderboardData getLeaderboard(String gametype){
        return leaderboard.get(gametype);
    }


}
