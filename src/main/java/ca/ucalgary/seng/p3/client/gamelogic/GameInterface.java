package ca.ucalgary.seng.p3.client.gamelogic;

public interface GameInterface {

    GeneralPlayer<?> getPlayerOne();

    GeneralPlayer<?> getPlayerTwo();

    /**
     * If there's a winner, returns winning player's number, otherwise returns 0.
     */
    int getWinner();

    /**
     * Returns the number of turns played by Player One.
     */
    int getPlayerOneTurns();

    /**
     * Returns the number of turns played by Player Two.
     */
    int getPlayerTwoTurns();
}
