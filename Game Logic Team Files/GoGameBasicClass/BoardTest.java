import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    private Board board;
    private Player blackPlayer;
    private Player whitePlayer;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.initialize(board);
        // Create two players: black (team 1) and white (team 2)
        blackPlayer = new Player(1, 12345, "blackPass");
        whitePlayer = new Player(2, 67890, "whitePass");
    }

    @Test
    void testPlayerUCIDTeamPassword() {
        // Verify initial values from constructor
        assertEquals(1, blackPlayer.getTeam(), "Initial team should be 1 (black)");
        assertEquals(12345, blackPlayer.getUCID(), "Initial UCID should match constructor value");
        assertEquals("blackPass", blackPlayer.getPassword(), "Initial password should match constructor value");
        // Change the player's attributes using setters
        blackPlayer.setTeam(2);
        blackPlayer.setUCID(11111);
        blackPlayer.setPassword("newPass");
        // Verify updated values
        assertEquals(2, blackPlayer.getTeam(), "Team setter should update the team value");
        assertEquals(11111, blackPlayer.getUCID(), "UCID setter should update the UCID value");
        assertEquals("newPass", blackPlayer.getPassword(), "Password setter should update the password");
    }

    @Test
    void testPlaceStoneThroughPlayer() {
        // Black player places a stone on an empty position via Player.placeStone()
        blackPlayer.placeStone(4, 4, board);
        assertEquals(1, board.matrix[4][4].getColor(), 
                "Black player should successfully place a black stone at (4,4)");
        // White player attempts to place a stone on the same spot, which is occupied
        whitePlayer.placeStone(4, 4, board);
        // The position should still have the black stone (white placement should be rejected by Board)
        assertEquals(1, board.matrix[4][4].getColor(), 
                "White player should not be able to place a stone on (4,4) since it's already occupied by black");
    }

    @Test
    void testPlayerSurrender() {
        // White player surrenders via Player.surrender()
        whitePlayer.surrender(board);
        // The board state should indicate a surrender has occurred (state = 3)
        assertEquals(3, board.state, "Board state should be 3 after a player surrenders");
        // By rules, if white surrendered, black is the winner. (The Board handles winner output in its console message)
        // We just ensure the surrender was registered in the game state.
    }
}
