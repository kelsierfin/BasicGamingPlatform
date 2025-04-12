//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.Test;
//import static org.junit.Assert.*;
//
//public class GameStatsTest {
//
//    @Test
//    public void testConstructorAndGetters() {
//        // Test constructor and all getter methods
//        GameStats stats = new GameStats(10, 7, 1200, 5);
//
//        // Verify direct stats are stored correctly
//        assertEquals(10, stats.getGamesPlayed());
//        assertEquals(7, stats.getGamesWon());
//        assertEquals(1200, stats.getELO());
//        assertEquals(5, stats.getRank());
//        // Verify calculated stat (win rate) is correct
//        assertEquals(70.0, stats.getWinRate(), 0.01);
//    }
//
//    @Test
//    public void testAddWin() {
//        // Test win tracking functionality
//        GameStats stats = new GameStats(4, 2, 1100, 10);
//        stats.addWin();
//
//        // Verify both games played and games won increase
//        assertEquals(5, stats.getGamesPlayed());
//        assertEquals(3, stats.getGamesWon());
//        // Verify win rate is recalculated correctly
//        assertEquals(60.0, stats.getWinRate(), 0.01);
//    }
//
//    @Test
//    public void testAddLoss() {
//        // Test loss tracking functionality
//        GameStats stats = new GameStats(4, 2, 1100, 10);
//        stats.addLoss();
//
//        // Verify games played increases but wins doesn't
//        assertEquals(5, stats.getGamesPlayed());
//        assertEquals(2, stats.getGamesWon());
//        // Verify win rate decreases appropriately
//        assertEquals(40.0, stats.getWinRate(), 0.01);
//    }
//
//    @Test
//    public void testSetELO() {
//        // Test ELO rating modification
//        GameStats stats = new GameStats(4, 2, 1100, 10);
//        stats.setELO(1350);
//
//        // Verify ELO is updated
//        assertEquals(1350, stats.getELO());
//    }
//}
