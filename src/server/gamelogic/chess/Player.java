package server.gamelogic.chess;

import commonfiles.commands.chess.ChessCommand;
import commonfiles.commands.chess.GUI_to_GameLogic.Click;
import commonfiles.commands.chess.GUI_to_GameLogic.PromotionSelection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Player {
    ChessGame game;
    boolean playsForWhite;
    Player opponent;
    King king;
    Rook kingSideRook;
    Rook queenSideRook;
    List<Piece> pieces;
    List<Piece> capturedPieces;
    int materialAdvantage;
    Piece selection;
    Piece movedPiece;
    List<Square> moveOptions;
    List<Square> attackedSquares;
    boolean promotingPawn;
    Square clickedSquare;
    Piece clickedPiece;

    public Player(ChessGame game, boolean playsForWhite) {
        this.game = game;
        this.playsForWhite = playsForWhite;
        pieces = new ArrayList<>();
        initializePieces();
        capturedPieces = new ArrayList<>();
        materialAdvantage = 0;
        attackedSquares = new ArrayList<>();
    }

    public void processInput(ChessCommand input) {
        if (game.activePlayer == this) {
            if (input instanceof Click c) {
                if (!promotingPawn) enactClick((c.squareName));
                // else deal with invalid input
            } else if (input instanceof PromotionSelection ps) {
                if (promotingPawn) promotePawn(ps.pieceType);
                // else deal with invalid input
            }
            // else deal with invalid input
        }
    }

    private void enactClick(String squareName) {
        clickedSquare = ChessGame.NAME_TO_SQUARE.get(squareName);
        clickedPiece = game.boardLayout.get(clickedSquare);

        if (selection != null && selection.moveOptions.contains(clickedSquare)) {
            if (selection instanceof Pawn p && clickedSquare.rankIndex == 7) promotingPawn = true;
            else move();
        }
        else if ((clickedPiece != null) && pieces.contains(clickedPiece)) select();
        else deselect();
    }

    private void move() {
        Square destination = clickedSquare;
        Piece capturedPiece = clickedPiece;
        movedPiece = selection;
        selection = null;
        king.attacker = null;

        if (capturedPiece != null) {
            capturedPieces.add(capturedPiece);
            capturedPiece.position = null;
            materialAdvantage += capturedPiece.value;
            opponent.materialAdvantage -= capturedPiece.value;
        }

        game.boardLayout.remove(movedPiece.position);
        game.boardLayout.put(destination, movedPiece);
        movedPiece.position = destination;

        if (movedPiece == king && !king.hasMoved) {
            if (destination == kingSideRook.castleKingDest) {
                game.boardLayout.remove(kingSideRook.position);
                game.boardLayout.put(kingSideRook.castleDest, kingSideRook);
                kingSideRook.position = kingSideRook.castleDest;
                kingSideRook.hasMoved = true;
            }
            if (destination == queenSideRook.castleKingDest) {
                game.boardLayout.remove(queenSideRook.position);
                game.boardLayout.put(queenSideRook.castleDest, queenSideRook);
                queenSideRook.position = queenSideRook.castleDest;
                queenSideRook.hasMoved = true;
            }
            king.hasMoved = true;
        }

        if (movedPiece instanceof Rook r) {
            if (!r.hasMoved) r.hasMoved = true;
        }

        updateAttackedSquares();
        opponent.updateMoveOptions();

        if (opponent.moveOptions.isEmpty()) {
            if (opponent.king.attacker != null) {
                // Checkmate
            } else {
                // Draw
            }
        } else game.activePlayer = opponent;
    }

    private void select() {
        this.selection = clickedPiece;
        // Create select instruction
        // Send select instruction through socket that represents player's connection
    }

    private void deselect() {
        selection = null;
        // Create deselect instruction
        // Send deselect instruction through socket that represents player's connection
    }

    private void promotePawn(char pieceType) {
        Class<? extends Piece> type = ChessGame.SYMBOL_TO_PIECE.get(pieceType);
        if ((type == Queen.class) || (type == Rook.class) || (type == Knight.class) || (type == Bishop.class)) {
            try {
                Piece newPiece = type.getDeclaredConstructor(Player.class, Square.class).newInstance(this, selection.position);
                game.boardLayout.put(selection.position, newPiece);
                pieces.remove(selection);
                pieces.add(newPiece);
                selection = newPiece;

                move();
                promotingPawn = false;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        // else handle invalid pieceType
    }

    public void updateAttackedSquares() {
        attackedSquares.clear();

        for (Piece piece : opponent.pieces) {
            piece.pinner = null;
        }

        for (Piece piece : pieces) {
            piece.updateAttackedSquares();
            for (Square square : piece.attackedSquares) {
                if (!attackedSquares.contains(square)) attackedSquares.add(square);
            }
        }
    }

    public void updateMoveOptions() {
        moveOptions.clear();
        for (Piece piece : pieces) {
            piece.updateMoveOptions();
            for (Square square : piece.moveOptions) {
                if (!moveOptions.contains(square)) moveOptions.add(square);
            }
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

}
