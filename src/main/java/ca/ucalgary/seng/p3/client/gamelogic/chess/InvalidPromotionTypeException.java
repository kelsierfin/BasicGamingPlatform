package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class InvalidPromotionTypeException extends RuntimeException {
    public InvalidPromotionTypeException() {
        super("Character passed as parameter cannot be mapped to a valid piece type for pawn promotion");
    }
}
