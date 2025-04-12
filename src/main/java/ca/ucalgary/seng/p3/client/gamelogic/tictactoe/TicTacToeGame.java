package ca.ucalgary.seng.p3.client.gamelogic.tictactoe;

import ca.ucalgary.seng.p3.client.gamelogic.GameInterface;

/** For testing GameClient. Not actual implementation. */
public class TicTacToeGame implements GameInterface {
    TicTacToePlayer playerOne;
    TicTacToePlayer playerTwo;
    boolean gameOver;
    int winner;
    private int playerOneTurns;
    private int playerTwoTurns;

    @Override
    public TicTacToePlayer getPlayerOne() {
        return playerOne;
    }

    @Override
    public TicTacToePlayer getPlayerTwo() {
        return playerTwo;
    }

    @Override
    public int getWinner() {
        return winner;
    }

    @Override
    public int getPlayerOneTurns() {
        return playerOneTurns;
    }

    @Override
    public int getPlayerTwoTurns() {
        return playerTwoTurns;
    }
}