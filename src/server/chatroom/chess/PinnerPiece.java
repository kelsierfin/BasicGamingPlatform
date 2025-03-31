package server.gamelogic.chess;

import java.util.HashMap;

public abstract class PinnerPiece extends Piece{
    String pinLine;     // "R" = rank, "F" = file,
                        // "LH" diagonal going from low rank and file to high rank and file
                        // "RF" diagonal going from high rank and low file to low rank and high file
    HashMap<Square, String> squareToAttackLine;

    public PinnerPiece(Player player, int value) {
        super(player, value);
        squareToAttackLine = new HashMap<>();
    }

    public PinnerPiece(Player player, int value, Square position) {
        super(player,3, position);
        squareToAttackLine = new HashMap<>();
    }
}
