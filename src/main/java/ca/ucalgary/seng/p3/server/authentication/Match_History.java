package ca.ucalgary.seng.p3.server.authentication;

import ca.ucalgary.seng.p3.server.leadmatch.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Match_History {
    private String username;

    public Match_History(String username) {
        this.username = username;
    }

    // Static method for direct access from other classes
    public static List<MatchData> getMatchHistory(String username) {
        try {
            PlayerStatData playerStatData = PlayerStats.getPlayersStats(username);
            return playerStatData.getMatchHistory();
        } catch (IOException e) {
            System.err.println("Error retrieving match history: " + e.getMessage());
            return new ArrayList<>(); // Return empty list on error
        }
    }

    // Instance method for use with existing Match_History objects
    public List<MatchData> getMatchHistory() {
        return getMatchHistory(this.username);
    }

    public List<String[]> viewMatchHistory() {
        List<String[]> matchHistoryList = new ArrayList<>();
        try {
            List<MatchData> matches = getMatchHistory(username);

            if (matches.isEmpty()) {
                matchHistoryList.add(new String[] { "no matches played" });
                return matchHistoryList;
            }

            for (MatchData match : matches) {
                String[] matchRow = new String[] {
                        match.getGameType(),
                        match.getOutcome(),
                        match.getPlayerUsername(),
                        match.getOpponentUsername(),
                        String.valueOf(match.getTurns())
                };
                matchHistoryList.add(matchRow);
            }

        } catch (Exception e) {
            System.err.println("Error displaying match history: " + e.getMessage());
            matchHistoryList.add(new String[] { "error retrieving match history" });
        }

        return matchHistoryList;
    }

    public static String matchHistoryToString(List<String[]> matchHistory) {
        StringBuilder sb = new StringBuilder();
        for (String[] match : matchHistory) {
            sb.append(String.join(",", match));  // joins array with commas
            sb.append("\n");                     // new line for each match
        }
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {

        Match_History history1 = new Match_History("player1");
        Match_History history2 = new Match_History("player2");
        Match_History history3 = new Match_History("player3");
        Match_History history4 = new Match_History("player4");
        Match_History history5 = new Match_History("player5");
        System.out.println(matchHistoryToString(history1.viewMatchHistory()));
        System.out.println(matchHistoryToString(history2.viewMatchHistory()));
        System.out.println(matchHistoryToString(history3.viewMatchHistory()));
        System.out.println(matchHistoryToString(history4.viewMatchHistory()));
        System.out.println(matchHistoryToString(history5.viewMatchHistory()));
    }

}