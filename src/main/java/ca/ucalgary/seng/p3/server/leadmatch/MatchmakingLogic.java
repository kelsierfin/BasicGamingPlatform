package ca.ucalgary.seng.p3.server.leadmatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatchmakingLogic {
    private List<String> queue; // list of usernames for players that are in queue for the game
    private String gameType;

    /**
     * Constructor to initialize the queue for a game
     */
    public MatchmakingLogic(String gameType) {
        this.queue = new ArrayList<>();
        this.gameType = gameType;
    }


    public String getGameType() {
        return gameType;
    }

    /**
     * Adds a player to the matchmaking queue
     * @param username The username of the player to be added
     */
    public void addPlayer(String username) {
        queue.add(username);
    }

    /**
     * Finds an opponent for a given player within an ELO range of ±200
     * @param username The player searching for a match
     * @return a list of strings that holds the two best matched players
     */
    public List<String> findMatch(String username) throws IOException {
        String bestMatch = null;
        int smallestDifference = 201; // Start with a value greater than 200

        for (String opponent : queue) {
            // Make sure the player doesn't match with themselves
            if (!opponent.matches(username)) {
                PlayerStatData playerData = DatabaseStub.getPlayerStatData(username);
                PlayerStatData opponentData = DatabaseStub.getPlayerStatData(opponent);
                GameStats playerGame = playerData.getGameStats(this.gameType);
                GameStats opponentGame = opponentData.getGameStats(this.gameType);
                int eloDifference = Math.abs(playerGame.getELO() - opponentGame.getELO());

                // Check if the opponent is within the 200 ELO range
                if (eloDifference <= 200 && eloDifference < smallestDifference) {
                    bestMatch = opponent;
                    smallestDifference = eloDifference; // Update to find the closest match
                }
            }
        }
        if (bestMatch != null) {
            // sets up the player to be returned and randomized which player is player 1
            Random rand = new Random();
            int num = rand.nextInt(101);
            ArrayList<String> returnMatch = new ArrayList<>();
            if (num < 50) {
                returnMatch.add(bestMatch);
                returnMatch.add(username);
            } else {
                returnMatch.add(username);
                returnMatch.add(bestMatch);
            }
            return returnMatch; // Return the best match or null if no match was found
        }else{
            return null;
        }
    }

    /**
     * Matches players removing them from the queue and starting a game
     * @param player1 username of player 1
     * @param player2 username of player 2
     * @throws IOException
     */
    public void matchPlayers(String player1, String player2) throws IOException {
        queue.remove(player1);
        queue.remove(player2);
        // need to put the code here to stat a game on the two clients with the usernames player 1 and player 2 with the game being gameType
        // will have to work with networking for this as I am unsure how this will work
    }

    /**
     * adds a player to the queue and checks the queue for players of similar elo if there is one it will start a match with those two players
     * this is to be used when a player clicks play for a game
     * @param username
     * @throws IOException
     */
    public void playerQueuesForGame(String username) throws IOException {
        addPlayer(username);
        List<String> players = findMatch(username);
        if (players != null) {
            matchPlayers(players.get(0), players.get(1));
        }
    }
}


/**
 * Possible code for integrating with networking multiplayer servers
 */
//package Leaderboard_and_Matchmaking_Classes;
//
//import server.NetworkManager;
//import server.Connection;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class MatchmakingLogic {
//    private List<String> queue; // list of usernames for players that are in queue for the game
//    private String gameType;
//    private NetworkManager networkManager; // Network manager to handle network-related tasks
//
//    /**
//     * Constructor to initialize the queue for a game
//     */
//    public MatchmakingLogic(String gameType, NetworkManager networkManager) {
//        this.queue = new ArrayList<>();
//        this.gameType = gameType;
//        this.networkManager = networkManager; // Initialize the network manager
//    }
//
//    public String getGameType() {
//        return gameType;
//    }
//
//    /**
//     * Adds a player to the matchmaking queue
//     * @param username The username of the player to be added
//     */
//    public void addPlayer(String username) {
//        queue.add(username);
//    }
//
//    /**
//     * Finds an opponent for a given player within an ELO range of ±200
//     * @param username The player searching for a match
//     * @return a list of strings that holds the two best-matched players
//     */
//    public List<String> findMatch(String username) throws IOException {
//        String bestMatch = null;
//        int smallestDifference = 201; // Start with a value greater than 200
//
//        for (String opponent : queue) {
//            // Make sure the player doesn't match with themselves
//            if (!opponent.matches(username)) {
//                PlayerStatData playerData = DatabaseStub.getPlayerStatData(username);
//                PlayerStatData opponentData = DatabaseStub.getPlayerStatData(opponent);
//                GameStats playerGame = playerData.getGameStats(this.gameType);
//                GameStats opponentGame = opponentData.getGameStats(this.gameType);
//                int eloDifference = Math.abs(playerGame.getELO() - opponentGame.getELO());
//
//                // Check if the opponent is within the 200 ELO range
//                if (eloDifference <= 200 && eloDifference < smallestDifference) {
//                    bestMatch = opponent;
//                    smallestDifference = eloDifference; // Update to find the closest match
//                }
//            }
//        }
//
//        if (bestMatch != null) {
//            // Randomly decide who will be player 1 or player 2
//            Random rand = new Random();
//            int num = rand.nextInt(101);
//            ArrayList<String> returnMatch = new ArrayList<>();
//            if (num < 50) {
//                returnMatch.add(bestMatch);
//                returnMatch.add(username);
//            } else {
//                returnMatch.add(username);
//                returnMatch.add(bestMatch);
//            }
//            return returnMatch; // Return the best match or null if no match was found
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * Matches players, removes them from the queue, and starts a game
//     * @param player1 username of player 1
//     * @param player2 username of player 2
//     * @throws IOException
//     */
//    public void matchPlayers(String player1, String player2) throws IOException {
//        queue.remove(player1);
//        queue.remove(player2);
//
//        // Establish network connections for both players
//        Connection connection1 = networkManager.createConnection(player1);
//        Connection connection2 = networkManager.createConnection(player2);
//
//        try {
//            // Connect players using NetworkManager
//            networkManager.establishConnection(connection1);
//            networkManager.establishConnection(connection2);
//
//            // Send game start message or any data you need for initialization
//            String startMessage = "Game Start: " + gameType;
//            networkManager.sendAsync(connection1, startMessage.getBytes());
//            networkManager.sendAsync(connection2, startMessage.getBytes());
//
//            // Further setup for game can go here (e.g., sending initial game state, etc.)
//        } catch (IOException e) {
//            e.printStackTrace();
//            // Handle connection failure (close connections, log errors, etc.)
//        }
//    }
//
//    /**
//     * Adds a player to the queue and checks the queue for players of similar ELO.
//     * If a match is found, it starts a match with those two players.
//     * @param username The username of the player
//     * @throws IOException
//     */
//    public void playerQueuesForGame(String username) throws IOException {
//        addPlayer(username);
//        List<String> players = findMatch(username);
//        if (players != null) {
//            matchPlayers(players.get(0), players.get(1));
//        }
//    }
//}
