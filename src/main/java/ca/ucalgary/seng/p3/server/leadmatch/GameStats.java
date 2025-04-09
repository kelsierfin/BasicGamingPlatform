package ca.ucalgary.seng.p3.server.leadmatch;

// This is an object used by PlayerStatsData simply to store information about a player's games played and rank

public class GameStats {

    private int gamesPlayed;
    private int gamesWon;
    private double winRate;
    private int ELO;
    private int rank;

    /**
     * Constructor for if a profile has to get added into the database
     * Rank could potentially be later assigned using a leaderboard getRank function
     *
     * @param gamesPlayed The number of games started by this player
     * @param gamesWon The number of games ending in a win for this player
     * @param ELO The number for this player's skill rating
     * @param rank The rank this player is relative to other players using ELO
     */
    public GameStats(int gamesPlayed, int gamesWon, int ELO, int rank) {

        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.newWinRate();
        this.ELO = ELO;
        this.rank = rank;

    }

//    /**
//     * Creates a player profile for a new player who does not have games played yet
//     */
//    public newProfile() {
//        this.gamesPlayed = 0;
//        this.gamesWon = 0;
//        this.winRate = 0;
//        this.ELO = 0; // Assign to the starting ELO, might not be 0 later on
//    }

    /**
     * Adds a game that counts as a victory to the profile
     */
    public void addWin() {
        gamesPlayed++;
        gamesWon++;
        this.newWinRate();
    }

    /**
     * Adds a game that counts as a loss to the profile
     */
    public void addLoss() {
        gamesPlayed++;
        this.newWinRate();
    }

    /**
     * Changes the ELO based on calculations in another class
     *
     * @param ELO The matchmaking rating that the player has
     */
    public void setELO(int ELO) {
        this.ELO = ELO;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public double getWinRate() {
        return winRate;
    }

    public int getELO() {
        return ELO;
    }

    public int getRank() {
        return rank;
    }

    /**
     * This function is just a helper to be able to change the winrate quickly
     *
     * @return The adjusted winrate after a new game
     */
    private void newWinRate() {
        winRate = Math.floor((double)gamesWon / (double)gamesPlayed * 10000) / 100;
    }

}