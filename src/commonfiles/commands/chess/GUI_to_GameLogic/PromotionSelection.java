package commonfiles.commands.chess.GUI_to_GameLogic;

import commonfiles.commands.chess.ChessCommand;

public class PromotionSelection extends ChessCommand {
    public char pieceType;  // K = King, Q = Queen, R = Rook, B = Bishop, N = Knight, P = Pawn

    public PromotionSelection() {
        super("PROMOTE_TO");
    }
}
