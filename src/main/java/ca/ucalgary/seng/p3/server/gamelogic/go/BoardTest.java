package ca.ucalgary.seng.p3.server.gamelogic.go;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        board.initialize(board);
    }

    @Test
    public void testPlaceStoneValid() {
        Board.MoveResult result = board.placeStone(3, 3, 1);
        assertTrue(result.success);
        assertEquals(0, result.cleared.size());
        assertEquals(1, board.getStoneColor(3, 3));
    }

    @Test
    public void testPlaceStoneInvalid() {
        board.placeStone(5, 5, 1);
        Board.MoveResult result = board.placeStone(5, 5, 2);
        assertFalse(result.success);
    }

    @Test
    public void testSurroundedCapture() {
        board.placeStone(1, 2, 2);
        board.placeStone(2, 1, 2);
        board.placeStone(2, 3, 2);
        board.placeStone(3, 2, 2);
        board.placeStone(2, 2, 1);

        Board.MoveResult result = board.placeStone(4, 4, 2); // Trigger checkSurround
        List<int[]> cleared = result.cleared;
        assertTrue(cleared.stream().anyMatch(pos -> pos[0] == 2 && pos[1] == 2));
        assertEquals(3, board.getStoneColor(2, 2)); // 3 = black_eaten
    }

    @Test
    public void testCheckSurroundReturnValue() {
        board.placeStone(0, 1, 2);
        board.placeStone(1, 0, 2);
        board.placeStone(1, 2, 2);
        board.placeStone(2, 1, 2);
        board.placeStone(1, 1, 1);

        List<int[]> captured = board.checkSurround();
        assertFalse(captured.isEmpty());
        assertTrue(captured.stream().anyMatch(p -> p[0] == 1 && p[1] == 1));
    }

    @Test
    public void testSurrender() {
        int winner = board.surrender(1);
        assertEquals(2, winner);
        assertEquals(3, board.getState());
    }

    @Test
    public void testPassTurnAndCheckState() {
        board.passTurn();
        board.passTurn();
        int state = board.getState();
        assertTrue(state == 1 || state == 2); // Game must end with a winner
    }
}
