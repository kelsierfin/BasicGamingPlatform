//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.Test;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import static org.junit.Assert.*;
//
///**
// * Simple test class for LeaderboardData
// */
//public class LeaderboardDataTest {
//
//    /**
//     * Test basic functionality of LeaderboardData
//     */
//    @Test
//    public void testLeaderboardData() {
//        // Create test data
//        List<String> playerIds = new ArrayList<>(Arrays.asList("player1", "player2"));
//        List<Integer> playerScores = new ArrayList<>(Arrays.asList(100, 85));
//
//        // Create and test leaderboard
//        LeaderboardData leaderboard = new LeaderboardData(playerIds, playerScores);
//
//        // Test initial state
//        assertEquals(2, leaderboard.getlast());
//        assertEquals("player1", leaderboard.getPlayerIds().get(0));
//        assertEquals(Integer.valueOf(100), leaderboard.getPlayerScores().get(0));
//
//        // Test adding a player
//        leaderboard.addPlayerId("player3");
//        leaderboard.addPlayerScore(120);
//
//        // Verify the addition
//        assertEquals(3, leaderboard.getlast());
//        assertEquals("player3", leaderboard.getPlayerIds().get(2));
//        assertEquals(Integer.valueOf(120), leaderboard.getPlayerScores().get(2));
//    }
//}