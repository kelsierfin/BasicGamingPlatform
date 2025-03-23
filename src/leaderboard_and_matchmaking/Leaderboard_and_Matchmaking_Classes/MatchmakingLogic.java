//package Leaderboard_and_Matchmaking_Classes;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MatchmakingLogic {
//    private List<GameStats> playerList; // List to store all players
//
//    /**
//     * Constructor to initialize the matchmaking system with a list of players
//     */
//    public MatchmakingLogic() {
//        this.playerList = new ArrayList<>();
//    }
//
//    /**
//     * Adds a player to the matchmaking system
//     * @param player The player to be added
//     */
//    public void addPlayer(GameStats player) {
//        playerList.add(player);
//    }
//
//    /**
//     * Finds an opponent for a given player within an ELO range of ±200
//     * @param player The player searching for a match
//     * @return A matched player or null if no match is found
//     */
//    public GameStats findMatch(GameStats player) {
//        GameStats bestMatch = null;
//        int smallestDifference = 201; // Start with a value greater than 200
//
//        for (GameStats opponent : playerList) {
//            // Make sure the player doesn't match with themselves
//            if (!opponent.equals(player)) {
//                int eloDifference = Math.abs(player.getELO() - opponent.getELO());
//
//                // Check if the opponent is within the 200 ELO range
//                if (eloDifference <= 200 && eloDifference < smallestDifference) {
//                    bestMatch = opponent;
//                    smallestDifference = eloDifference; // Update to find the closest match
//                }
//            }
//        }
//        return bestMatch; // Return the best match or null if no match was found
//    }
//
//    /**
//     * Matches players and prints the results
//     */
//    public void matchPlayers() {
//        for (GameStats player : playerList) {
//            GameStats opponent = findMatch(player);
//
//            if (opponent != null) {
//                System.out.println("Player with ELO " + player.getELO() +
//                        " is matched with player with ELO " + opponent.getELO());
//            } else {
//                System.out.println("No match found for player with ELO " + player.getELO());
//            }
//        }
//    }
//}
