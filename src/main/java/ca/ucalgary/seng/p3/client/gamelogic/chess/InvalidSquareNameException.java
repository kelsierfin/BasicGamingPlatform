package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class InvalidSquareNameException extends RuntimeException {
    public InvalidSquareNameException() {
        super("String passed as parameter does not constitute a valid square name");
    }
}