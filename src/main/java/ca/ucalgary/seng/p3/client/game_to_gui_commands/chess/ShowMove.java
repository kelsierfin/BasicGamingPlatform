package ca.ucalgary.seng.p3.client.game_to_gui_commands.chess;

public class ShowMove implements GameToGuiCommand {
    public boolean whiteMoved;      // Determines colour of piece symbols that fill squares
    public String squareToEmpty;        // Ex: b6, h4       May not be necessary for GUI on which move was made if it keeps track of selected square
    public String squareToFill;     // Only needed by GUI on which move wasn't made
    public char pieceType;      // K = King, Q = Queen, R = Rook, B = Bishop, N = Knight, P = Pawn
    public int materialAdvantageIncrease;
    public CaptureInfo captureInfo;     // null if no capture
    public CastleInfo castleInfo;       // null if not a castle
    public GameOverInfo gameOverInfo;   // null if game not over
    public boolean clientDisconnected;

    public ShowMove(boolean whiteMoved) {
        this.whiteMoved = whiteMoved;
    }
}
