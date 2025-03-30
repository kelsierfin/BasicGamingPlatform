package Leaderboard_and_Matchmaking_Classes;

import java.util.HashMap;

/**
 * This class is a stub for the database.
 * The real database will likely combine all the database classes that we have.
 * Making this class more represent a table in the database that holds the player stats with the PlayerStatData and class more representing the schema and data in this table.
 */
public class PlayerStatDatabase {

    private HashMap<Profile, PlayerStatData> playersStats;


    public PlayerStatDatabase() {
        this.playersStats = new HashMap<>();
    }


    /**
     * adds a new playerstats to a player.
     * should be used when making a new player with the starting player stats
     *
     * @param profile
     * @param playerData
     */
    public void addStats(Profile profile, PlayerStatData playerData){

        // checks if entered parameters are null
        if (profile == null || playerData == null) {
            throw new IllegalArgumentException("profile and player data cannot be null");
        }

        // Check if a profile for that game already exists
        boolean profileExists = false;
        for (int i = 0; i < playersStats.size(); i++) {
            if (playersStats.containsKey(profile)) {
                profileExists = true;
                break;
            }
        }
        if (!profileExists) {
            playersStats.put(profile, playerData);
        }
    }

    /**
     * returns the players stats
     * @param profile
     * @return PlayerStatData
     */
    public PlayerStatData getStats(Profile profile){
        return playersStats.get(profile);
    }


}

}
