package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class ReflectiveInstantiationException extends RuntimeException {
    public ReflectiveInstantiationException() {
        super("Reflective instantiation attempted in promotePawn() failed");
    }
}
