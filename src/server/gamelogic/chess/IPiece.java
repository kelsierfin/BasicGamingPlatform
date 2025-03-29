package server.gamelogic.chess;

import java.util.List;

public interface IPiece {
//    List<Square> getCurrentMoveOptions();
    void updateMoveOptions();
    void updateAttackedSquares();
}
