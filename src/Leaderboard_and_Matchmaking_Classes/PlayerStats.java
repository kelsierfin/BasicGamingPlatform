package Leaderboard_and_Matchmaking_Classes;

// This class is for giving the information from a completed game to the player statistics database
// TODO: Update the function to reference placeholder functions correctly once they have been created
// gameType could be changed to a Game object or maybe an int
// Import player profile once it is created

public class PlayerStats {

    /**
     * Adds player statistics from a completed game to the database
     *
     * @param player1 The profile of the player who went first
     * @param player2 The profile of the player who went second
     * @param player1Won Whether or not player1 won the game that was played
     * @param gameType The type of game played (chess, checkers, etc.)
     * @param turns How many turns were played until the winning move
     * @param time The amount of time the match took to complete
     */
    public void updatePlayerStats(Profile player1, Profile player2, boolean player1Won, String gameType, int turns, int time) {

        // Get the mmr from the two players prior to the match, including how far apart they are
        int mmr1 = player1.getElo();
        int mmr2 = player2.getElo();
        int mmrDifference = mmr2 - mmr1;
        int rankChange = 20;

        // Add a win to the profile of the winning player, a loss to the other
        if (player1Won) {
            PlayerStatDatabase.addWin(player1, gameType, turns, time);
            PlayerStatDatabase.addLoss(player2, gameType, turns, time);
        } else {
            PlayerStatDatabase.addWin(player2, gameType, turns, time);
            PlayerStatDatabase.addLoss(player1, gameType, turns, time);
        }

        // Logic for ELO/MMR changes based on who won will be based on relative ranking differences
        // Example: Winner gets flat rate +20 +-1 for every 10 points opponent is above or below in rank
        // Winner gets minimum of 8, no less. Gain is reversed for the loser but can only lose 6 minimum
        // There is currently no maximum rank gain or loss, that could be added if necessary (+-40 maximum for example)
        // There will also be next to no rank inflation, more calculations would be needed to create an "average rank"
        if (player1Won) {
            rankChange = 20 + (mmrDifference / 10);
            if (rankChange < 8) {
                mmr1 += 8;
                if (rankChange < 6) {
                    mmr2 -= 6;
                } else {
                    mmr2 -= rankChange;
                }
            } else {
                mmr1 += rankChange;
                mmr2 -= rankChange;
            }
        } else {
            rankChange = 20 - (mmrDifference / 10);
            if (rankChange < 8) {
                mmr2 += 8;
                if (rankChange < 6) {
                    mmr1 -= 6;
                } else {
                    mmr1 -= rankChange;
                }
            } else {
                mmr2 += rankChange;
                mmr1 -= rankChange;
            }
        }

        // Update the database with the new mmr numbers and send it to update the leaderboard data
        // updateLeaderboard might only need to be called once depending on if it updates only for the 1 player
        PlayerStatDatabase.updateElo(player1, mmr1);
        PlayerStatDatabase.updateElo(player2, mmr2);
        Leaderboard.updateLeaderboard(gameType, player1);
        Leaderboard.updateLeaderboard(gameType, player2);

    }

}