//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.*;
//import java.io.*;
//import java.util.*;
//
//import static org.junit.Assert.*;
//
//public class MatchmakingLogicTest {
//
//    private static final String TEST_FILE = "test_matchmaking_stats.csv";
//    private MatchmakingLogic chessMatchmaking;
//
//    @Before
//    public void setUp() throws IOException {
//        // Create test CSV with players having different ELO ratings
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
//            // Alice: 1200 ELO in chess
//            writer.write("alice,4,2,1200,1,0,0,0,4,0,0,0,4,0,0,0,4,bob,chess,win,30\n");
//            // Bob: 1300 ELO in chess
//            writer.write("bob,3,1,1300,2,0,0,0,4,0,0,0,4,0,0,0,4,alice,chess,loss,30\n");
//            // Charlie: 1000 ELO in chess
//            writer.write("charlie,2,1,1000,3,0,0,0,4,0,0,0,4,0,0,0,4\n");
//            // Dave: 1500 ELO in chess (outside alice's ±200 ELO range)
//            writer.write("dave,5,3,1500,4,0,0,0,4,0,0,0,4,0,0,0,4\n");
//        }
//
//        // Use test file instead of actual database file
//        replaceFileNameInStub(TEST_FILE);
//
//        // Initialize chess matchmaking system
//        chessMatchmaking = new MatchmakingLogic("chess");
//    }
//
//    @After
//    public void tearDown() {
//        // Clean up test file
//        new File(TEST_FILE).delete();
//    }
//
//    @Test
//    public void testAddPlayer() {
//        // Verify player is added to queue correctly
//        chessMatchmaking.addPlayer("alice");
//
//        List<String> queue = getQueueFromMatchmaking(chessMatchmaking);
//
//        assertEquals(1, queue.size());
//        assertEquals("alice", queue.get(0));
//    }
//
//    @Test
//    public void testGetGameType() {
//        // Verify game type is correctly returned
//        assertEquals("chess", chessMatchmaking.getGameType());
//
//        // Check different game type
//        MatchmakingLogic connect4Matchmaking = new MatchmakingLogic("connect4");
//        assertEquals("connect4", connect4Matchmaking.getGameType());
//    }
//
//    @Test
//    public void testFindMatchWithValidOpponent() throws IOException {
//        // Test matching algorithm with valid opponents
//        chessMatchmaking.addPlayer("alice"); // ELO 1200
//        chessMatchmaking.addPlayer("bob");   // ELO 1300
//        chessMatchmaking.addPlayer("charlie"); // ELO 1000
//
//        List<String> match = chessMatchmaking.findMatch("alice");
//
//        // Verify match is found and contains correct players
//        assertNotNull(match);
//        assertEquals(2, match.size());
//
//        // Bob should be matched (closer ELO to Alice than Charlie)
//        assertTrue(match.contains("alice"));
//        assertTrue(match.contains("bob"));
//        assertFalse(match.contains("charlie"));
//    }
//
//    @Test
//    public void testFindMatchWithNoValidOpponent() throws IOException {
//        // Test no match found when ELO difference exceeds 200
//        chessMatchmaking.addPlayer("alice"); // ELO 1200
//        chessMatchmaking.addPlayer("dave");  // ELO 1500 (>200 difference from charlie)
//
//        List<String> match = chessMatchmaking.findMatch("charlie"); // ELO 1000
//
//        // No valid match should be found
//        assertNull(match);
//    }
//
//    @Test
//    public void testFindMatchDoesNotMatchWithSelf() throws IOException {
//        // Verify players can't match with themselves
//        chessMatchmaking.addPlayer("alice");
//
//        List<String> match = chessMatchmaking.findMatch("alice");
//
//        // No match should be possible
//        assertNull(match);
//    }
//
//    @Test
//    public void testMatchPlayers() throws IOException {
//        // Test removing matched players from queue
//        chessMatchmaking.addPlayer("alice");
//        chessMatchmaking.addPlayer("bob");
//        chessMatchmaking.addPlayer("charlie");
//
//        chessMatchmaking.matchPlayers("alice", "bob");
//
//        // Verify matched players removed, other player remains
//        List<String> queue = getQueueFromMatchmaking(chessMatchmaking);
//        assertEquals(1, queue.size());
//        assertTrue(queue.contains("charlie"));
//        assertFalse(queue.contains("alice"));
//        assertFalse(queue.contains("bob"));
//    }
//
//    @Test
//    public void testPlayerQueuesForGame() throws IOException {
//        // Test complete queue and match workflow
//        chessMatchmaking.addPlayer("bob"); // ELO 1300
//
//        // Alice should match with Bob immediately
//        chessMatchmaking.playerQueuesForGame("alice"); // ELO 1200
//
//        // Queue should be empty after successful match
//        List<String> queue = getQueueFromMatchmaking(chessMatchmaking);
//        assertTrue(queue.isEmpty());
//    }
//
//    @Test
//    public void testPlayerQueuesForGameNoMatch() throws IOException {
//        // Test behavior when no match is found
//        chessMatchmaking.playerQueuesForGame("alice");
//
//        // Alice should remain in queue
//        List<String> queue = getQueueFromMatchmaking(chessMatchmaking);
//        assertEquals(1, queue.size());
//        assertTrue(queue.contains("alice"));
//    }
//
//    // Helper to replace database filename for testing
//    private void replaceFileNameInStub(String fileName) {
//        try {
//            java.lang.reflect.Field field = DatabaseStub.class.getDeclaredField("FILE_NAME");
//            field.setAccessible(true);
//            field.set(null, fileName);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to set test file name.", e);
//        }
//    }
//
//    // Helper to access private queue field via reflection
//    private List<String> getQueueFromMatchmaking(MatchmakingLogic matchmaking) {
//        try {
//            java.lang.reflect.Field field = MatchmakingLogic.class.getDeclaredField("queue");
//            field.setAccessible(true);
//            return (List<String>) field.get(matchmaking);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to access queue field.", e);
//        }
//    }
//}
