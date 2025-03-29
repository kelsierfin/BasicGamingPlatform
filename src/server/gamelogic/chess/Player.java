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
    List<Square> attackedSquares;
    Pawn pawnToPromote;
    boolean canCastle;

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
                if (pawnToPromote == null) enactClick((c.squareName));
                // else deal with invalid input
            } else if (input instanceof PromotionSelection ps) {
                if (pawnToPromote != null) promotePawn(ps.pieceType);
                // else deal with invalid input
            }
            // else deal with invalid input
        }
    }

    private void enactClick(String squareName) {
        Square clickedSquare = ChessGame.NAME_TO_SQUARE.get(squareName);
        Piece clickedPiece = game.boardLayout.get(clickedSquare);

        if (selection != null && selection.moveOptions.contains(clickedSquare))
            move(clickedSquare, clickedPiece);
        else if ((clickedPiece != null) && pieces.contains(clickedPiece))
            select(clickedPiece);
        else deselect();
    }

    private void move(Square destination, Piece pieceToCapture) {
        movedPiece = selection;
        selection = null;

        if (pieceToCapture != null) {
            capturedPieces.add(pieceToCapture);
            pieceToCapture.position = null;
            materialAdvantage += pieceToCapture.value;
            opponent.materialAdvantage -= pieceToCapture.value;
        }

        if (canCastle) {
            if (destination == kingSideRook.castleKingDest) {
                game.boardLayout.remove(kingSideRook.position);
                game.boardLayout.put(kingSideRook.castleDest, kingSideRook);
                kingSideRook.position = kingSideRook.castleDest;
            }
            if (destination == queenSideRook.castleKingDest) {
                game.boardLayout.remove(queenSideRook.position);
                game.boardLayout.put(queenSideRook.castleDest, queenSideRook);
                queenSideRook.position = queenSideRook.castleDest;
            }
            canCastle = false;
        }

        game.boardLayout.remove(movedPiece.position);
        game.boardLayout.put(destination, movedPiece);
        movedPiece.position = destination;

        if (movedPiece instanceof CastledPiece cp) cp.hasMoved = true;

        for (Piece piece : pieces) {
            piece.moveOptionsUpdated = false;
        }

        if ((movedPiece instanceof Pawn p) && (destination.rankIndex == 7)) {
            pawnToPromote = p;
        } else {
        updateAttackedSquares();
        game.activePlayer = opponent;
        }
    }

    private void select(Piece selection) {
        this.selection = selection;
        if (!selection.moveOptionsUpdated) selection.updateMoveOptions();
        // Create select instruction
        // Send select instruction through socket that represents player's connection
    }

    private void deselect() {
        selection = null;
        // Create deselect instruction
        // Send deselect instruction through socket that represents player's connection
    }

    private void promotePawn(char pieceType) {
        Square position = selection.position;
        Class<? extends Piece> type = ChessGame.SYMBOL_TO_PIECE.get(pieceType);
        if ((type == Queen.class) || (type == Rook.class) || (type == Knight.class) || (type == Bishop.class)) {
            try {
                Piece newPiece = type.getDeclaredConstructor(Player.class, Square.class).newInstance(this, position);
                // Replace pawnToPromote with newPiece
                updateAttackedSquares();
                game.activePlayer = opponent;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        // else handle invalid pieceType
    }

    private void updateAttackedSquares() {
        attackedSquares.clear();
        for (Piece piece : pieces) {
            piece.updateAttackedSquares();
            for (Square square : piece.attackedSquares) {
                if (!attackedSquares.contains(square)) attackedSquares.add(square);
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
