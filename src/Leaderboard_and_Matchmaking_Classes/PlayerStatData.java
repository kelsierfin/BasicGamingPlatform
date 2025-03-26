package Leaderboard_and_Matchmaking_Classes;

import java.util.*;

public class PlayerStatData {

    private int overallGamesPlayed;
    private int overallGamesWon;
    private double overallWinRate;
    private Map<String, GameStats> gameStats;
    private List<MatchData> matchHistory;

    /**
     * Create a new character profile with all the stats already known
     *
     * @param overallGamesPlayed All matches played from all games combined
     * @param overallGamesWon All wins across every match from every game
     * @param gameStats The statistics for each game in specific
     * @param matchHistory The history of the most recent matches played by this profile
     */
    public PlayerStatData(int overallGamesPlayed, int overallGamesWon,
                          Map<String, GameStats> gameStats, List<MatchData> matchHistory) {
        this.overallGamesPlayed = overallGamesPlayed;
        this.overallGamesWon = overallGamesWon;
        this.setMatchWinRate();
        this.gameStats = gameStats;
        this.matchHistory = matchHistory;
    }

    /**
     * Creating a new player with no stats yet
     */
    public newPlayerData() {

        overallGamesPlayed = 0;
        overallGamesWon = 0;
        overallWinRate = 0;
        gameStats = new HashMap<String gameType, GameStats gameStats>();
        GameStats gameOne = newProfile();
        GameStats gameTwo = newProfile();
        GameStats gameThree = newProfile();
        GameStats gameFour = newProfile(); // Can add more if more games are added;
        gameStats.put("Chess", gameOne);
        gameStats.put("Go", gameTwo);
        gameStats.put("Connect4", gameThree);
        gameStats.put("Tictactoe", gameFour);
        matchHistory = new ArrayList<MatchData>();

    }

    /**
     * Update the ArrayList of matchHistory to keep it up to date, a limit of 20 recent matches are kept
     *
     * @param lastGame The game just played to be added
     */
    public void updateMatchHistory(MatchData lastGame) {
        matchHistory.add(lastGame);
        if (matchHistory.size() > 20) {
            matchHistory = matchHistory.subList(0, 20);
        }
        overallGamesPlayed;
        // lastGame.getWin is a placeholder until the function is added
        if (lastGame.getWin()) {
            gameStats.get(lastGame.getGame).addWin();
            overallGamesWon++;
        } else {
            gameStats.get(lastGame.getGame).addLoss();
        }
        this.setMatchWinRate();

    }

    // Helper to calculate winrate whenever it changes
    private double setMatchWinRate() {
        winRate = Math.floor((double)overallGamesWon / (double)overallGamesPlayed * 10000) / 100;
    }

}