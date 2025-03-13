package Leaderboard_and_Matchmaking_Classes;

public class PlayerStats {

    /*
     * This function references functions that are not yet made and can be changed later to fit the given names
     * Also gameType could instead be a Game object? maybe just an int as well since String seems just quite unneeded
     * Also not 100% sure if PlayerStatDatabase needs to be initialized as an object or if I can reference all the time
     */
    public void updatePlayerStats(Profile player1, Profile player2, boolean player1Won, String gameType, int turns, int time) {

        // This just adds all the player data into the database based on who won the game
        if (player1Won) {
            PlayerStatDatabase.addWin(player1, gameType, turns, time);
            PlayerStatDatabase.addLoss(player2, gameType, turns, time);
        } else {
            PlayerStatDatabase.addWin(player2, gameType, turns, time);
            PlayerStatDatabase.addLoss(player1, gameType, turns, time);
        }

        // Logic for ELO/MMR changes based on who won will be done here which will get sent as a player profile to leaderboard
        // Example: Winner gets flat rate +20 +-1 for every 10 points opponent is above or below in rank
        // Winner gets minimum of 8, no less. Gain is reversed for the loser but can only lose 6 minimum

        PlayerStatDatabase.updateElo(player1, mmr);
        PlayerStatDatabase.updateElo(player2, mmr);
        Leaderboard.updateLeaderboard(gameType, player1);
        Leaderboard.updateLeaderboard(gameType, player2);

    }

}