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
            clickedSquare = findSquare(squareName);
            clickedPiece = clickedSquare.piece;
            if (moveOptions.contains(clickedSquare)) move();
            else if ((clickedPiece != null) && pieces.contains(clickedPiece)) {
                selection = clickedPiece;
                moveOptions = selection.findMoveOptions();
                // Create select instruction file
                // Send instruction file to active player's computer
            }
            else {
                selection = null;
                moveOptions.clear();
                // Create deselect instruction file
                // Send instruction file to active player's computer
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
        selection.position.piece = null;
        clickedSquare.piece = selection;
        selection.position = clickedSquare;

        selection = null;
        moveOptions.clear();
        for (Piece piece : pieces) {
            piece.moveOptionsUpdated = false;
        }
    }

    public void initializePieces() {
        pieces.add(new King(game, this));
        pieces.add(new Queen(game, this));
        pieces.add(new Bishop(game, this, true));
        pieces.add(new Bishop(game, this, false));
        pieces.add(new Knight(game, this, true));
        pieces.add(new Knight(game, this, false));
        pieces.add(new Rook(game, this, true));
        pieces.add(new Rook(game, this, false));
        pieces.add(new Pawn(game, this,1));
        pieces.add(new Pawn(game, this,2));
        pieces.add(new Pawn(game, this,3));
        pieces.add(new Pawn(game, this,4));
        pieces.add(new Pawn(game, this,5));
        pieces.add(new Pawn(game, this,6));
        pieces.add(new Pawn(game, this,7));
        pieces.add(new Pawn(game, this,8));
    }

    private Square findSquare(String squareName) {
        return game.board[squareName.charAt(1)-'1'][squareName.charAt(0)-'a'];
    }

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
