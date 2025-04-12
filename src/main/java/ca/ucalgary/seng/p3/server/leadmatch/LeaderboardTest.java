//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.Before;
//import org.junit.Test;
//import java.io.IOException;
//import java.util.*;
//import static org.junit.Assert.*;
//
//public class LeaderboardTest {
//
//    @Before
//    public void setUp() {
//        // Reset mock data before each test for isolation
//        DatabaseStub.clearMockData();
//    }
//
//    @Test
//    public void testGetLeaderboardSortedCorrectly() throws IOException {
//        // Test leaderboard sorting functionality
//        // Create unsorted test data
//        List<String> ids = new ArrayList<>(Arrays.asList("user1", "user2", "user3"));
//        List<Integer> scores = new ArrayList<>(Arrays.asList(1200, 1400, 1000));
//        LeaderboardData leaderboard = new LeaderboardData(ids, scores);
//
//        // Set up test data in database stub
//        DatabaseStub.setMockLeaderboard("chess", leaderboard);
//
//        // Get sorted leaderboard
//        LeaderboardData result = Leaderboard.getLeaderboard("chess");
//
//        // Expected order: highest score first
//        List<String> expectedIds = Arrays.asList("user2", "user1", "user3");
//        List<Integer> expectedScores = Arrays.asList(1400, 1200, 1000);
//
//        // Verify sorting works correctly
//        assertEquals(expectedIds, result.getPlayerIds());
//        assertEquals(expectedScores, result.getPlayerScores());
//    }
//
//    @Test
//    public void testUpdateRankingsSortsAndSaves() throws IOException {
//        // Test ranking update and persistence
//        // Create test data
//        List<String> ids = new ArrayList<>(Arrays.asList("a", "b", "c"));
//        List<Integer> scores = new ArrayList<>(Arrays.asList(100, 300, 200));
//        LeaderboardData leaderboard = new LeaderboardData(ids, scores);
//
//        // Set up test data and trigger update
//        DatabaseStub.setMockLeaderboard("puzzle", leaderboard);
//        Leaderboard.updateRankings("puzzle");
//
//        // Verify data was sorted and saved
//        LeaderboardData updated = DatabaseStub.getUpdatedLeaderboard("puzzle");
//
//        // Check order is by descending score
//        assertEquals(Arrays.asList("b", "c", "a"), updated.getPlayerIds());
//        assertEquals(Arrays.asList(300, 200, 100), updated.getPlayerScores());
//    }
//
//    @Test
//    public void testLastPositionReturnsCorrectValue() throws IOException {
//        // Test getting the next rank position for a new player
//        // Create test data with 2 existing players
//        List<String> ids = new ArrayList<>(Arrays.asList("x", "y"));
//        List<Integer> scores = new ArrayList<>(Arrays.asList(1100, 900));
//        LeaderboardData leaderboard = new LeaderboardData(ids, scores);
//
//        // Set up test data
//        DatabaseStub.setMockLeaderboard("checkers", leaderboard);
//
//        // Get the last position
//        int last = Leaderboard.lastPosition("checkers");
//
//        // Should be one more than current player count
//        assertEquals(3, last); // 2 players + 1
//    }
//}
