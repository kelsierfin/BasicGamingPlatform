package ca.ucalgary.seng.p3.server.leadmatch;
import java.io.*;
import java.util.*;
import java.util.HashMap;

/**
 * This class is a stub for the database.
 * The stub database will hold the data in a csv file in the following format
 *         | games stats for chess         | game stats for connect 4       | game stats for go             | game stats for tic-tac-toe    |match history
 * username,games_played,games_won,elo,rank,games_played,games_won, elo,rank,games_played,games_won elo,rank,games_played,games_won,elo,rank,opponent_username, game_type,game_outcome,total_turns,...(continues for as many games as they played)
 * number of strings (17 + 4 per match)
 */
public class DatabaseStub {

    private static final String FILE_NAME = "src/main/resources/database/stats.csv";

    /**
     * reads the stats.csv file and puts each line into a list of arrays of strings with each of the arrays of strings
     * holding a players stats as outlined above
     *
     * this is used when updating and getting a players stats
     * @return List<String[]>
     * @throws IOException
     */
    private static ArrayList<String[]> loadStats() throws IOException {
        ArrayList<String[]> stats = new ArrayList<>();
        File file = new File(FILE_NAME);

        // If the file doesn't exist, create an empty one
        if (!file.exists()) {
            file.createNewFile();
            return stats;
        }

        // Read each line from the file and split by commas
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 17) {
                    // adds each part seperated by a comma as an array of string to the total list of all the players stats
                    stats.add(parts);
                }
            }
        }

        return stats;
    }

    /**
     *  takes a list of arrays of strings and puts them into the csv file
     *  each array of strings will be a different line with commas between each string
     *  used in the updateStats method
     * @param stats
     * @throws IOException
     */
    private static void saveStats(List<String[]> stats) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] stat : stats) {
                writer.write(String.join(",", stat) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    /**
     * reads the csv file if there is a player with the username returns that players stats in the form of the object Player stat data
     * used when viewing or updating player stats
     * @param userName
     * @return PlayerStatData
     * @throws IOException
     */
    public static PlayerStatData getPlayerStatData(String userName) throws IOException {
        ArrayList<String[]> stats = loadStats();
        for (String[] stat : stats) {
            if (stat[0].equals(userName)) {
                // generates the match history
                List<MatchData> matchHistory = new ArrayList<>();
                for (int i = 17; i < stat.length; i += 4){
                    matchHistory.add(new MatchData(stat[0], stat[i], stat[i+1], stat[i+2], Integer.parseInt(stat[i+3])));
                }
                int gamesPlayed = (Integer.parseInt(stat[1]) + Integer.parseInt(stat[5]) + Integer.parseInt(stat[9]) + Integer.parseInt(stat[13]));
                int gamesWon = (Integer.parseInt(stat[2]) + Integer.parseInt(stat[6]) + Integer.parseInt(stat[10]) + Integer.parseInt(stat[14]));
                // makes the hash map that contains all the stats for each game
                Map<String, GameStats> statsPerGame = new HashMap<>();
                statsPerGame.put("chess", new GameStats(Integer.parseInt(stat[1]),Integer.parseInt(stat[2]), Integer.parseInt(stat[3]), Integer.parseInt(stat[4])));
                statsPerGame.put("connect4", new GameStats(Integer.parseInt(stat[5]),Integer.parseInt(stat[6]), Integer.parseInt(stat[7]), Integer.parseInt(stat[8])));
                statsPerGame.put("go", new GameStats(Integer.parseInt(stat[9]),Integer.parseInt(stat[10]), Integer.parseInt(stat[11]), Integer.parseInt(stat[12])));
                statsPerGame.put("tictactoe", new GameStats(Integer.parseInt(stat[13]),Integer.parseInt(stat[14]), Integer.parseInt(stat[15]), Integer.parseInt(stat[16])));
                return new PlayerStatData(gamesPlayed, gamesWon, statsPerGame, matchHistory);
            }
        }
        throw new IllegalArgumentException("No stats found for player " + userName);
    }



    public static LeaderboardData getLeaderboard(String gameType) throws IOException {
        List<String[]> stats = loadStats();
        List<String> leaderboard = new ArrayList<>();
        List<Integer> ELOList = new ArrayList<>();

        for (String[] stat : stats) {
            if (gameType.equalsIgnoreCase("chess")){
                leaderboard.add(stat[0]);
                try {
                    ELOList.add(Integer.parseInt(stat[3])); // Chess Elo is at index 3
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Elo value for Chess: " + stat[3]);
                }
            } else if (gameType.equalsIgnoreCase("connect4")) {
                leaderboard.add(stat[0]);
                try {
                    ELOList.add(Integer.parseInt(stat[7])); // Connect4 Elo is at index 7
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Elo value for Connect4: " + stat[7]);
                }
            } else if (gameType.equalsIgnoreCase("go")) {
                leaderboard.add(stat[0]);
                try {
                    ELOList.add(Integer.parseInt(stat[11])); // Go Elo is at index 11
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Elo value for Go: " + stat[11]);
                }
            } else if (gameType.equalsIgnoreCase("tic tac toe")) {
                leaderboard.add(stat[0]);
                try {
                    ELOList.add(Integer.parseInt(stat[15])); // TicTacToe Elo is at index 15
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Elo value for TicTacToe: " + stat[15]);
                }
            }
        }

        if (leaderboard.isEmpty() || ELOList.isEmpty()) {
            System.out.println("No players or scores found for " + gameType);
        }

        return new LeaderboardData(leaderboard, ELOList);
    }


//    /**
//     * makes a leaderboard data with the data from the csv file.
//     * used for displaying and updating the leaderboard
//     * @param gameType
//     * @return
//     * @throws IOException
//     */
//    public static LeaderboardData getLeaderboard(String gameType) throws IOException {
//        ArrayList<String[]> stats = loadStats();
//        List<String> leaderboard = new ArrayList<>();
//        List<Integer> ELOList = new ArrayList<>();
//        for (String[] stat : stats) {
//            if (gameType.equals("chess")) {
//                leaderboard.add(stat[0]);
//                ELOList.add(Integer.parseInt(stat[3]));
//            }else if (gameType.equals("connect4")) {
//                leaderboard.add(stat[0]);
//                ELOList.add(Integer.parseInt(stat[7]));
//            }else if (gameType.equals("go")) {
//                leaderboard.add(stat[0]);
//                ELOList.add(Integer.parseInt(stat[11]));
//            }else if (gameType.equals("tictactoe")) {
//                leaderboard.add(stat[0]);
//                ELOList.add(Integer.parseInt(stat[15]));
//            }
//        }
//        return new LeaderboardData(leaderboard, ELOList);
//    }


    /**
     * Takes leaderboard data and the gametype that data is for and updates a players rankings base on the leaderboard data
     * with the username at the first index of the playerIds list being the number 1 player and so on
     * @param gameType
     * @param leaderboardData
     * @throws IOException
     */
    public static void updateRanking(String gameType, LeaderboardData leaderboardData) throws IOException {
        ArrayList<String[]> stats = loadStats();
        List<String> leaderboard = leaderboardData.getPlayerIds();

        for (String[] stat : stats) {
            int index = leaderboard.indexOf(stat[0]);
            if (gameType.equals("chess")) {
                stat[4] = String.valueOf(index + 1);
            }else if (gameType.equals("connect4")) {
                stat[8] = String.valueOf(index + 1);
            }else if (gameType.equals("go")) {
                stat[12] = String.valueOf(index + 1);
            }else if (gameType.equals("tictactoe")) {
                stat[16] = String.valueOf(index + 1);
            }
        }
    }

    /**
     * Takes a username and a that players stats and if the username is in the csv file it replaces that players stats with the players stats in the param
     * used when updating a players stats after a game
     * @param username
     * @param playerStatData
     * @throws IOException
     */
    public static void updateStats(String username, PlayerStatData playerStatData) throws IOException {
        ArrayList<String[]> stats = loadStats();
        for (String[] stat : stats) {
            if (stat[0].equals(username)) {
                // adds updated chess data to the list
                GameStats chessStats = playerStatData.getGameStats("chess");
                stat[1] = String.valueOf(chessStats.getGamesPlayed());
                stat[2] = String.valueOf(chessStats.getGamesWon());
                stat[3] = String.valueOf(chessStats.getELO());
                stat[4] = String.valueOf(chessStats.getRank());
                // adds updated connect 4 data to the list
                GameStats connect4Stats = playerStatData.getGameStats("connect4");
                stat[5] = String.valueOf(connect4Stats.getGamesPlayed());
                stat[6] = String.valueOf(connect4Stats.getGamesWon());
                stat[7] = String.valueOf(connect4Stats.getELO());
                stat[8] = String.valueOf(connect4Stats.getRank());
                // adds updated go data to the list
                GameStats goStats = playerStatData.getGameStats("go");
                stat[9] = String.valueOf(goStats.getGamesPlayed());
                stat[10] = String.valueOf(goStats.getGamesWon());
                stat[11] = String.valueOf(goStats.getELO());
                stat[12] = String.valueOf(goStats.getRank());
                // adds updated tic-tac-toe to the list
                GameStats tictactoestats = playerStatData.getGameStats("tictactoe");
                stat[13] = String.valueOf(tictactoestats.getGamesPlayed());
                stat[14] = String.valueOf(tictactoestats.getGamesWon());
                stat[15] = String.valueOf(tictactoestats.getELO());
                stat[16] = String.valueOf(tictactoestats.getRank());
                //adds any matches the player had to the list
                int i = 17;
                List<MatchData> matchHistory = playerStatData.getMatchHistory();
                for (MatchData match : matchHistory) {
                    if(stat.length <= i){
                        String[] stat2 = new String[stat.length + 4]; // increases stat size by 4
                        System.arraycopy(stat, 0, stat2, 0, stat.length); // Copy elements from oldArray to newArray
                        stat = stat2;
                    }
                    stat[i] = match.getOpponentUsername();
                    stat[i+1] = match.getGameType();
                    stat[i+2] = match.getOutcome();
                    stat[i+3] = String.valueOf(match.getTurns());
                    i += 4;
                }
            }
        }
        saveStats(stats);
    }



    /**
     * adds the stating stats for a new player to the csv file
     * should be called when making a new account in authentication
     * @param username
     * @throws IOException
     */
    public static void saveNewStats(String username) throws IOException {
        // Open the file in append mode and write the account details
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            // when creating new stats the player is set to the lowest rank
            String lastChessRank = String.valueOf(Leaderboard.lastPosition("chess"));
            String lastConnect4Rank = String.valueOf(Leaderboard.lastPosition("connect4"));
            String lastGoRank = String.valueOf(Leaderboard.lastPosition("go"));
            String lastTictactoeRank = String.valueOf(Leaderboard.lastPosition("tictactoe"));
            bw.write(username + ",0,0,0," + lastChessRank + ",0,0,0," + lastConnect4Rank + ",0,0,0," + lastGoRank + ",0,0,0," + lastTictactoeRank);
            bw.newLine(); // Move to the next line for the next entry
        }
    }


}
