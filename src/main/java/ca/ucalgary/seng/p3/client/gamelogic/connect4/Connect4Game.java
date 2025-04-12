package ca.ucalgary.seng.p3.client.gamelogic.connect4;

import ca.ucalgary.seng.p3.client.gamelogic.GameInterface;

/** For testing GameClient. Not actual implementation. */
public class Connect4Game implements GameInterface {
    Connect4Player playerOne;
    Connect4Player playerTwo;
    boolean gameOver;
    int winner;
    private int playerOneTurns;
    private int playerTwoTurns;

    @Override
    public Connect4Player getPlayerOne() {
        return playerOne;
    }

    @Override
    public Connect4Player getPlayerTwo() {
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