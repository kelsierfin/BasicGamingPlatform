package server.gamelogic.chess;

import java.util.ArrayList;
import java.util.List;

public class Player {
    ChessGame game;
    boolean playsForWhite;
    List<Piece> pieces;
    List<Square> occupiedSquares;
//    Square selection;   // Can be used to get move options using selection.findMoveOptions()
    List<Piece> capturedPieces;
    Piece selection;
    Square clickedSquare;
    List<Square> moveOptions;

    public Player(ChessGame game, boolean playsForWhite) {
        this.game = game;
        this.playsForWhite = playsForWhite;
        pieces = new ArrayList<>();
        initializePieces();
        capturedPieces = new ArrayList<>();
        moveOptions = new ArrayList<>();
    }

    public void enactClick(String input) {
        if (game.activePlayer == this) {
            clickedSquare = findSquare(input);
            if (moveOptions.contains(clickedSquare)) {
                game.move(selection, clickedSquare);
                selection = null;
                moveOptions.clear();
                for (Piece piece : pieces) {
                    piece.moveOptionsUpdated = false;
                }
            }
            else if ((clickedSquare != selection.position) && occupiedSquares.contains(clickedSquare)) {
                selection = clickedSquare.piece;
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

    private Square findSquare(String input) {
        return game.board[input.charAt(1)-'1'][input.charAt(0)-'a'];
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
