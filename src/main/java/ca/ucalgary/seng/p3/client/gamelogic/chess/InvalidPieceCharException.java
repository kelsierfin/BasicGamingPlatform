package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class InvalidPieceCharException extends RuntimeException {
    public InvalidPieceCharException() {
        super("Character passed as parameter cannot be mapped to a piece type");
    }
}
