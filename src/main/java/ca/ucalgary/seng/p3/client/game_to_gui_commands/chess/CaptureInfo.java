package ca.ucalgary.seng.p3.client.game_to_gui_commands.chess;

public class CaptureInfo {
    public char capturedPieceType; // Determines which captured piece type's label to increment by one

    public CaptureInfo(char capturedPieceType) {
        this.capturedPieceType = capturedPieceType;
    }
}
