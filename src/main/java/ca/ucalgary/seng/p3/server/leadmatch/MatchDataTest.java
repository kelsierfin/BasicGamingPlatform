//package ca.ucalgary.seng.p3.server.leadmatch;
//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class MatchDataTest {
//
//    @Test
//    public void MatchDataGetterandConstructorTest() {
//        // Test constructor and getter methods
//        MatchData match = new MatchData("player1", "opponent1", "chess", "win", 35);
//
//        // Verify all fields are stored correctly
//        assertEquals("player1", match.getPlayerUsername());
//        assertEquals("opponent1", match.getOpponentUsername());
//        assertEquals("chess", match.getGameType());
//        assertEquals("win", match.getOutcome());
//        assertEquals(35, match.getTurns());
//    }
//
//    @Test
//    public void MatchDataSetterTest() {
//        // Test setter methods for all fields
//        MatchData match = new MatchData("player1", "opponent1", "chess", "win", 35);
//
//        // Modify all properties
//        match.setPlayerUsername("newPlayer");
//        match.setOpponentUsername("newOpponent");
//        match.setGameType("go");
//        match.setOutcome("loss");
//        match.setTurns(42);
//
//        // Verify all modifications were applied
//        assertEquals("newPlayer", match.getPlayerUsername());
//        assertEquals("newOpponent", match.getOpponentUsername());
//        assertEquals("go", match.getGameType());
//        assertEquals("loss", match.getOutcome());
//        assertEquals(42, match.getTurns());
//    }
//}
