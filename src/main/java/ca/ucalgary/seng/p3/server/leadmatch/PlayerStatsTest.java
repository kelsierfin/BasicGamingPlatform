//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.*;
//import static org.junit.Assert.*;
//
//import java.io.*;
//import Leaderboard_and_Matchmaking_Classes.PlayerStats;
//import Leaderboard_and_Matchmaking_Classes.PlayerStatData;
//import Leaderboard_and_Matchmaking_Classes.DatabaseStub;
//
//public class PlayerStatsTest {
//
//    private static final String STATS_FILE = "test_player_rank.csv";
//
//    // Makes a file to use as the database that PlayerStats draws from
//    @Before
//    public void setUp() throws IOException {
//        try (BufferedWriter stats = new BufferedWriter(new FileWriter(STATS_FILE))) {
//            stats.write("player1,5,5,300,40,1,1,200,50,1,1,200,50,1,1,200,50\n");
//            stats.write("player2,10,6,600,18,1,1,200,50,1,1,200,50,1,1,200,50\n");
//        }
//
//        replaceFileNameInStub(STATS_FILE);
//    }
//
//    // Delete the file after testing
//    @After
//    public void tearDown() {
//        new File(STATS_FILE).delete();
//    }
//
//    // Chess stats were assigned to 2 players.
//    // They play, player 2 wins and the minimum rank change is given to both
//    @Test
//    public void playerRankTest() throws IOException {
//
//        PlayerStats.updatePlayerStats("player1", "player2", false, "chess", 30);
//
//        PlayerStatData person1 = DatabaseStub.getPlayerStatData("player1");
//        PlayerStatData person2 = DatabaseStub.getPlayerStatData("player2");
//
//        assertEquals(294, person1.getChessStats().getELO());
//        assertEquals(608, person2.getChessStats().getELO());
//
//    }
//
//    // Helper to swap the STATS_FILE in the stub
//    // Made by Min Oh 30099454
//    private void replaceFileNameInStub(String fileName) {
//        try {
//            java.lang.reflect.Field field = DatabaseStub.class.getDeclaredField("STATS_FILE");
//            field.setAccessible(true);
//            field.set(null, fileName);
//        } catch (Exception e) {
//            throw new RuntimeException("Unable to set test file name.", e);
//        }
//    }
//
//}