//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.*;
//import java.io.*;
//
//import static org.junit.Assert.*;
//
//public class DatabaseStubTest {
//
//    private static final String TEST_FILE = "test_stats.csv";
//
//    @Before
//    public void setUp() throws IOException {
//        // Set up a test CSV file with dummy data
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
//            writer.write("alice,4,2,1200,1," +
//                    "0,0,0,4," +
//                    "0,0,0,4," +
//                    "0,0,0,4," +
//                    "bob,chess,win,30\n");
//        }
//
//        // Temporarily override the actual file name
//        replaceFileNameInStub(TEST_FILE);
//    }
//
//    @After
//    public void tearDown() {
//        // Delete the test file after each test
//        new File(TEST_FILE).delete();
//    }
//
//    @Test
//    public void testGetPlayerStatData() throws IOException {
//        // Test retrieving player statistics from database
//        PlayerStatData data = DatabaseStub.getPlayerStatData("alice");
//
//        // Verify chess stats match expected values from test data
//        assertEquals(4, data.getGameStats("chess").getGamesPlayed());
//        assertEquals(2, data.getGameStats("chess").getGamesWon());
//        assertEquals(1200, data.getGameStats("chess").getELO());
//    }
//
//    @Test
//    public void testUpdateStats() throws IOException {
//        // Test updating player statistics in database
//        PlayerStatData data = DatabaseStub.getPlayerStatData("alice");
//        data.getGameStats("chess").addWin(); // Increments both games played and games won
//        DatabaseStub.updateStats("alice", data);
//
//        // Verify updated values are correctly saved and retrieved
//        PlayerStatData updated = DatabaseStub.getPlayerStatData("alice");
//        assertEquals(5, updated.getGameStats("chess").getGamesPlayed());
//        assertEquals(3, updated.getGameStats("chess").getGamesWon());
//    }
//
//    // Helper to replace database filename for testing
//    private void replaceFileNameInStub(String fileName) {
//        try {
//            // Use reflection to access and modify private static field
//            java.lang.reflect.Field field = DatabaseStub.class.getDeclaredField("FILE_NAME");
//            field.setAccessible(true);
//            field.set(null, fileName);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to set test file name.", e);
//        }
//    }
