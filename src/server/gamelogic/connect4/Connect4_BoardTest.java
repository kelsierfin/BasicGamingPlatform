package server.gamelogic.connect4;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Connect4_BoardTest {
    Connect4_Board board = new Connect4_Board();
    Player player_one = new Player("Player1", "Y");
    Player player_two = new Player("Player2", "X");
    private final int ROWS = 6;
    private final int COLUMNS = 7;


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
    public void checkBasicDiagonalWin() {

        int i = 0;
        Connect4_Board testBoard = new Connect4_Board();
        Player player_one = new Player("Player1", "Y");
        Player player_two = new Player("Player2", "X");


        int row = 0;
        int column = 0;
        int switchPlayer = 1;

        while(row<ROWS){
            while(column < COLUMNS){
                if(switchPlayer < 2){
                    testBoard.placeDisc(column, new Disc(player_one.getSelectedSymbol()));
                    switchPlayer++;
                }else if(switchPlayer > 2 && switchPlayer < 5){
                    testBoard.placeDisc(column, new Disc(player_one.getSelectedSymbol()));
                    switchPlayer++;

                    if(switchPlayer == 5){
                        switchPlayer =0;
                    }
                }
                column++;
            }
            row++;
        }

        assertTrue(testBoard.checkWin(player_one.getSelectedSymbol()));








    }


    @Test
    public void checkBasicRowWin(){
        int i = 0;
        Connect4_Board testBoard = new Connect4_Board();
        Player player_one = new Player("Player1", "Y");



        while(i<5){
            testBoard.placeDisc(i, new Disc(player_one.getSelectedSymbol()));
            i++;
        }

        assertTrue(testBoard.checkWin(player_one.getSelectedSymbol()));

    }





    @Test
    public void checkBasicColumnWin() {
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
    public void getSymbol() {
        Connect4_Board testBoard = new Connect4_Board();
        assertEquals("Y", testBoard.getSymbol("[Y]"));
        assertEquals("X", testBoard.getSymbol("[X]"));


    }
}