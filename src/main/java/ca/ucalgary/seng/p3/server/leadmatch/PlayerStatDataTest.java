//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.Test;
//
//import java.util.HashMap;
//import static org.junit.Assert.*;
//
////    public PlayerStatData(int overallGamesPlayed, int overallGamesWon,
////                          Map<String, GameStats> gameStats, List<MatchData> matchHistory) {
////    public GameStats(int gamesPlayed, int gamesWon, int ELO, int rank)
////    public MatchData(String playerUsername, String opponentUsername, String gameType,  String outcome, int turns)
//
//public class PlayerStatDataTest {
//
//    /**
//     * Tests PlayerStatData and the objects it has
//     */
//    @Test
//    public void testPlayerStatData() {
//
//        GameStats chessTest = new GameStats(10, 5, 100, 32);
//        MatchData matchTest = new MatchData("Player1", "Player2", "chess", "Player1 wins", 25);
//        List<MatchData> matchListTest = new List<MatchData>();
//        matchListTest.addFirst(matchTest);
//        Map<String, GameStats> gamesPlayed = new HashMap<String, GameStats>();
//        gamesPlayed.add("chess", chessTest);
//        PlayerStatData playerStatTests = new PlayerStatData(10, 5, gamesPlayed, matchListTest);
//
//        // Test the counters for games played and games won
//
//        playerStatTests.addWin("chess");
//        playerStatTests.addLoss("chess");
//        assertEquals(11, playerStatTests.getGameStats("chess").getGamesWon());
//        assertEquals(11, playerStatTests.getOverallGamesPlayed());
//
//        // Test the match history and adding to it
//
//        playerStatTests.addMatch("Player1", "Player2", "chess", "Player2 wins", 20);
//        List<MatchData> matchListTwo = new List<MatchData>();
//        matchTest1 = new MatchData("Player1", "Player2", "chess", "Player1 wins", 25);
//        matchTest2 = new MatchData("Player1", "Player2", "chess", "Player2 wins", 20);
//        matchListTwo.addFirst(matchTest1);
//        matchListTwo.addFirst(matchTest2);
//        assertEquals(playerStatTests.getMatchHistory(), matchListTwo);
//
//    }
//
//}