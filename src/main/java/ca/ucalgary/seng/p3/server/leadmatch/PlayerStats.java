package ca.ucalgary.seng.p3.server.leadmatch;

// This class is for giving the information from a completed game to the player statistics database
// gameType could be changed to a Game object or maybe an int
// Import player profile once it is created

import java.io.IOException;

public class PlayerStats {

    /**
     * Adds player statistics from a completed game to the database
     *
     * @param player1 The username of the player who went first
     * @param player2 The username of the player who went second
     * @param player1Won Whether or not player1 won the game that was played
     * @param gameType The type of game played (chess, checkers, etc.)
     * @param turns How many turns were played until the winning move
     */
    public static void updatePlayerStats(String player1, String player2, boolean player1Won, String gameType, int turns) throws IOException {

        // Get the mmr from the two players prior to the match, including how far apart they are
        PlayerStatData player1stats = DatabaseStub.getPlayerStatData(player1);
        PlayerStatData player2stats = DatabaseStub.getPlayerStatData(player2);
        GameStats player1gstats = player1stats.getGameStats(gameType);
        GameStats player2gstats = player2stats.getGameStats(gameType);
//        int mmr1 = player1gstats.getELO();
//        int mmr2 = player2gstats.getELO();
//        int mmrDifference = mmr2 - mmr1;
//        int rankChange = 20;

        // Add a win to the profile of the winning player, a loss to the other
        if (player1Won) {
            player1stats.addWin(gameType);
            player2stats.addLoss(gameType);
            player1stats.addMatch(player1, player2, "Win", gameType, turns);
            player2stats.addMatch(player2, player1, "Loss", gameType,turns);
        } else {
            player1stats.addWin(gameType);
            player2stats.addLoss(gameType);
            player1stats.addMatch(player1, player2, "Loss", gameType,turns);
            player2stats.addMatch(player2, player1, "Win", gameType,turns);
        }

        // Logic for ELO/MMR changes based on who won will be based on relative ranking differences
        // Example: Winner gets flat rate +20 +-1 for every 10 points opponent is above or below in rank
        // Winner gets minimum of 8, no less. Gain is reversed for the loser but can only lose 6 minimum
        // There is currently no maximum rank gain or loss, that could be added if necessary (+-40 maximum for example)
        // There will also be next to no rank inflation, more calculations would be needed to create an "average rank"
//        if (player1Won) {
//            rankChange = 20 + (mmrDifference / 10);
//            if (rankChange < 8) {
//                mmr1 += 8;
//                if (rankChange < 6) {
//                    mmr2 -= 6;
//                } else {
//                    mmr2 -= rankChange;
//                }
//            } else {
//                mmr1 += rankChange;
//                mmr2 -= rankChange;
//            }
//        } else {
//            rankChange = 20 - (mmrDifference / 10);
//            if (rankChange < 8) {
//                mmr2 += 8;
//                if (rankChange < 6) {
//                    mmr1 -= 6;
//                } else {
//                    mmr1 -= rankChange;
//                }
//            } else {
//                mmr2 += rankChange;
//                mmr1 -= rankChange;
//            }
//        }
//        if(mmr1 < 0){
//            mmr1 = 0;
//        }
//        if(mmr2 < 0){
//            mmr2 = 0;
//        }
//
//        // Update the database with the new mmr numbers and player stats
//        player1gstats.setELO(mmr1);
//        player2gstats.setELO(mmr2);
//        DatabaseStub.updateStats(gameType, player1stats);
//        DatabaseStub.updateStats(gameType, player2stats);
//        Leaderboard.updateRankings(gameType);
    }

    /**
     * gets the player stats for the entered username.
     * used for displaying the players stats
     * @param username
     * @return
     * @throws IOException
     */
    public static PlayerStatData getPlayersStats(String username) throws IOException {
        return DatabaseStub.getPlayerStatData(username);
    }

}