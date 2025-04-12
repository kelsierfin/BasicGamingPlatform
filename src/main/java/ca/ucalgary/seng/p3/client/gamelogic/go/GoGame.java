package ca.ucalgary.seng.p3.client.gamelogic.go;

import ca.ucalgary.seng.p3.client.gamelogic.GameInterface;

/** For testing GameClient. Not actual implementation. */
public class GoGame implements GameInterface {
    GoPlayer playerOne;
    GoPlayer playerTwo;
    boolean gameOver;
    int winner;
    private int playerOneTurns;
    private int playerTwoTurns;

    @Override
    public GoPlayer getPlayerOne() {
        return playerOne;
    }

    @Override
    public GoPlayer getPlayerTwo() {
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