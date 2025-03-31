package commonfiles.commands.chess.GameLogic_to_GUI;

import commonfiles.commands.chess.ChessCommand;

public class Move extends ChessCommand {
    public String source;
    public String destination;
    public char movedPiece;
    public boolean capture;
    public char capturedPiece;
    public boolean advantageWhite;
    public int materialAdvantage;
    public boolean pawnPromotion;

    public Move() {
        super("MOVE");
    }
}
