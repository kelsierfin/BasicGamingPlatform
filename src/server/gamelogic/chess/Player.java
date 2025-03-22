package server.gamelogic.chess;

import java.util.ArrayList;
import java.util.List;

public class Player {
    ChessGame game;
    boolean playsForWhite;
    Player opponent;
    List<Piece> pieces;
    List<Square> occupiedSquares;
//    Square selection;   // Can be used to get move options using selection.findMoveOptions()
    List<Piece> capturedPieces;
    int materialAdvantage;
    Piece selection;
    Square clickedSquare;
    Piece clickedPiece;
    List<Square> moveOptions;

    public Player(ChessGame game, boolean playsForWhite) {
        this.game = game;
        this.playsForWhite = playsForWhite;
        pieces = new ArrayList<>();
        initializePieces();
        capturedPieces = new ArrayList<>();
        moveOptions = new ArrayList<>();
    }

    public void enactClick(String squareName) {
        if (game.activePlayer == this) {
            clickedSquare = ChessGame.nameToSquare.get(squareName);
            clickedPiece = game.squareToPiece.get(clickedSquare);
            if (moveOptions.contains(clickedSquare)) move();
            else if ((clickedPiece != null) && pieces.contains(clickedPiece)) {
                selection = clickedPiece;
                moveOptions = selection.getMoveOptions();
                // Set instruction string to instruction to select clicked square
                // Send instruction string through socket that represents the active player's connection
            }
            else {
                selection = null;
                moveOptions.clear();
                // Set instruction string to instruction to deselect currently selected square
                // Send instruction string through socket that represents the active player's connection
            }
        }
    }

    void move() {
        if (clickedPiece != null) {
            capturedPieces.add(clickedPiece);
            clickedPiece.position = null;
            materialAdvantage += clickedPiece.value;
            opponent.materialAdvantage -= clickedPiece.value;
        }
        game.squareToPiece.remove(selection.position);
        game.squareToPiece.put(clickedSquare, selection);
        selection.position = clickedSquare;

        selection = null;
        moveOptions.clear();
        for (Piece piece : pieces) {
            piece.moveOptionsUpdated = false;
        }
    }

    public void initializePieces() {
        pieces.add(new King(this));
        pieces.add(new Queen(this));
        pieces.add(new Bishop(this, true));
        pieces.add(new Bishop(this, false));
        pieces.add(new Knight(this, true));
        pieces.add(new Knight(this, false));
        pieces.add(new Rook(this, true));
        pieces.add(new Rook(this, false));
        pieces.add(new Pawn(this,1));
        pieces.add(new Pawn(this,2));
        pieces.add(new Pawn(this,3));
        pieces.add(new Pawn(this,4));
        pieces.add(new Pawn(this,5));
        pieces.add(new Pawn(this,6));
        pieces.add(new Pawn(this,7));
        pieces.add(new Pawn(this,8));
    }

//    private Square findSquare(String squareName) {
//        return game.board[squareName.charAt(1)-'1'][squareName.charAt(0)-'a'];
//    }

//    private boolean isValidReselection(Square square) {
//        if (square != selection.position) {
//            for (Piece piece : pieces) {
//                if (piece.position == square) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
