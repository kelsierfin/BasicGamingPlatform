//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.Test;
//import java.util.*;
//import static org.junit.Assert.*;
//import Leaderboard_and_Matchmaking_Classes.PlayerStatData;
//import Leaderboard_and_Matchmaking_Classes.MatchData;
//import Leaderboard_and_Matchmaking_Classes.GameStats;
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
//        List<MatchData> matchListTest = new ArrayList<MatchData>();
//        matchListTest.addFirst(matchTest);
//        Map<String, GameStats> gamesPlayed = new HashMap<String, GameStats>();
//        gamesPlayed.put("chess", chessTest);
//
//        // Test constructor
//
//        PlayerStatData playerStatTests = new PlayerStatData(10, 5, gamesPlayed, matchListTest);
//
//        // Test the counters for games played and games won
//
//        playerStatTests.addWin("chess");
//        playerStatTests.addLoss("chess");
//        assertEquals(6, playerStatTests.getGameStats("chess").getGamesWon());
//        assertEquals(6, playerStatTests.getOverallGamesWon());
//        assertEquals(12, playerStatTests.getOverallGamesPlayed());
//        assertEquals(50.0, playerStatTests.getOverallWinRate(), 0.01);
//
//
//
//        // Test the match history and adding to it
//
//        playerStatTests.addMatch("Player1", "Player2", "chess", "Player2 wins", 20);
//        List<MatchData> matchListTwo = new ArrayList<MatchData>();
//        MatchData matchTest1 = new MatchData("Player1", "Player2", "chess", "Player1 wins", 25);
//        MatchData matchTest2 = new MatchData("Player1", "Player2", "chess", "Player2 wins", 20);
//        matchListTwo.addFirst(matchTest1);
//        matchListTwo.addFirst(matchTest2);
//        assertEquals(matchListTwo, playerStatTests.getMatchHistory());
//
//        // Test the other game specific stat retrievals
//
//        GameStats chessData = new GameStats(100, 25, 381, 32);
//        GameStats tictactoeData = new GameStats(20, 2, 200, 99);
//        GameStats connect4Data = new GameStats(3, 2, 40, 180);
//        GameStats goData = new GameStats(309, 200, 150, 140);
//        Map<String, GameStats> allGamesPlayed = new HashMap<String, GameStats>();
//        allGamesPlayed.put("chess", chessData);
//        allGamesPlayed.put("tictactoe", tictactoeData);
//        allGamesPlayed.put("connect4", connect4Data);
//        allGamesPlayed.put("go", goData);
//        PlayerStatData playerStatTest = new PlayerStatData(432, 229, allGamesPlayed, matchListTest);
//
//        assertEquals(playerStatTest.getChessStats(), chessData);
//        assertEquals(playerStatTest.getGoStats(), goData);
//        assertEquals(playerStatTest.getConnect4Stats(), connect4Data);
//        assertEquals(playerStatTest.getTictactoeStats(), tictactoeData);
//    }
//
//}