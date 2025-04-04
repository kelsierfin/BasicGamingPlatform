package server.gamelogic.connect4;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Connect4_BoardTest {
    Connect4_Board board = new Connect4_Board();
    Player player_one = new Player("Player1", "Y");
    Player player_two = new Player("Player2", "X");


    @Test
    public void validMove() {
        assertFalse(board.validMove(1000));
        assertFalse(board.validMove(7));
        assertFalse(board.validMove(-1));
        assertTrue(board.validMove(4));
        assertTrue(board.validMove(0));
        assertTrue(board.validMove(3));


    }

    @Test
    public void getBoard() {

    }

    @Test
    public void placeDisc() {





    }






    @Test
    public void checkBasicDiagonalWin() {
        int i = 0;
        Connect4_Board testBoard = new Connect4_Board();
        Player player_one = new Player("Player1", "Y");



        while(i<6){
            testBoard.placeDisc(0, new Disc(player_one.getSelectedSymbol()));
            i++;
        }

        assertTrue(testBoard.checkWin(player_one.getSelectedSymbol()));


    }

    @Test
    public void checkIfOneColumnIsFull() {
        int i = 0;
        Connect4_Board testBoard = new Connect4_Board();
        Player player_one = new Player("Player1", "Y");



        while(i<6){
            testBoard.placeDisc(0, new Disc(player_one.getSelectedSymbol()));
            i++;
        }

        assertTrue(testBoard.isColumnFull());




    }


    @Test
    public void getSymbol() {
        Connect4_Board testBoard = new Connect4_Board();
        assertEquals("Y", testBoard.getSymbol("[Y]"));
        assertEquals("X", testBoard.getSymbol("[X]"));


    }
}