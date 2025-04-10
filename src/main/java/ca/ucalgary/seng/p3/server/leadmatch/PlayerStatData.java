package ca.ucalgary.seng.p3.server.leadmatch;

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

    public int getOverallGamesPlayed() {
        return overallGamesPlayed;
    }

    public int getOverallGamesWon() {
        return overallGamesWon;
    }

    public double getOverallWinRate() {
        return overallWinRate;
    }

    public GameStats getChessStats(String game) {
        return gameStats.get("chess");
    }

    public GameStats getGoStats(String game) {
        return gameStats.get("go");
    }

    public GameStats getConnect4Stats(String game) {
        return gameStats.get("connect4");
    }

    public GameStats getTictactoeStats(String game) {
        return gameStats.get("tictactoe");
    }

    public GameStats getGameStats(String game) {
        return gameStats.get(game);
    }

    public List<MatchData> getMatchHistory() {
        return matchHistory;
    }

    /**
     * adds a win to the players stats updating their overall and games stats
     * @param gameType
     */
    public void addWin(String gameType) {
        this.overallGamesPlayed ++;
        this.overallGamesWon ++;
        setMatchWinRate();
        GameStats game = getGameStats(gameType);
        game.addWin();
    }

    /**
     * adds a loss to the players stats updating their overall and games stats
     * @param gameType
     */
    public void addLoss(String gameType) {
        this.overallGamesPlayed ++;
        setMatchWinRate();
        GameStats game = getGameStats(gameType);
        game.addLoss();
    }

    /**
     *  used to add a new match to the match history and keeps the match history to a max of 20 matches
     *  the newest match will be in index 0 and the oldest match will be in index 20
     * @param playerUsername
     * @param opponentUsername
     * @param outcome
     * @param turns
     */
    public void addMatch(String playerUsername, String opponentUsername, String gameType, String outcome, int turns) {
        MatchData match = new MatchData(playerUsername, opponentUsername, gameType, outcome, turns);
        this.matchHistory.addFirst(match);
        if (this.matchHistory.size() > 20) {
            this.matchHistory = this.matchHistory.subList(0, 20);
        }
    }


    // Helper to calculate winrate whenever it changes
    private void setMatchWinRate() {
        this.overallWinRate = Math.floor((double)overallGamesWon / (double)overallGamesPlayed * 10000) / 100;
    }

}