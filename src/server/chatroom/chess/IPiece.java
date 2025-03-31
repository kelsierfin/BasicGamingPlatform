package server.gamelogic.chess;

import java.util.List;

public interface IPiece {
    void updateMoveOptions();
    void updateAttackedSquares();
}
