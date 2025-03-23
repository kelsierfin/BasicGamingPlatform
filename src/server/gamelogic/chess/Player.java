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
    List<Piece> pieces;
//    List<Square> occupiedSquares;
    List<Piece> capturedPieces;
    int materialAdvantage;
//    boolean hasTurn;
    Piece selection;
    List<Square> moveOptions;
    Pawn pawnToPromote;
    boolean kingOrRookMoved;

    public Player(ChessGame game, boolean playsForWhite) {
        this.game = game;
        this.playsForWhite = playsForWhite;
        pieces = new ArrayList<>();
        initializePieces();
        capturedPieces = new ArrayList<>();
        moveOptions = new ArrayList<>();
//        isPromotingPawn = false;
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
        if (moveOptions.contains(clickedSquare)) move(clickedSquare, clickedPiece);
        else if ((clickedPiece != null) && pieces.contains(clickedPiece)) select(clickedPiece);
        else deselect();
    }

    private void move(Square destination, Piece pieceToCapture) {
        if (pieceToCapture != null) {
            capturedPieces.add(pieceToCapture);
            pieceToCapture.position = null;
            materialAdvantage += pieceToCapture.value;
            opponent.materialAdvantage -= pieceToCapture.value;
        }
        game.boardLayout.remove(selection.position);
        game.boardLayout.put(destination, selection);
        selection.position = destination;

        moveOptions.clear();

        for (Piece piece : pieces) {
            piece.moveOptionsUpdated = false;
        }
        if ((selection instanceof Pawn p) && (destination.rankIndex == 7)) {
            pawnToPromote = p;
        }
        game.activePlayer = opponent;
    }

    private void select(Piece selection) {
        this.selection = selection;
        moveOptions = selection.getCurrentMoveOptions();
        // Create select instruction
        // Send select instruction through socket that represents player's connection
    }

    private void deselect() {
        selection = null;
        moveOptions.clear();
        // Create deselect instruction
        // Send deselect instruction through socket that represents player's connection
    }

    private void promotePawn(char pieceType) {
        Square position = selection.position;
        Class<? extends Piece> type = ChessGame.SYMBOL_TO_PIECE.get(pieceType);
        if ((type == Queen.class) || (type == Rook.class) || (type == Knight.class) || (type == Bishop.class)) {
            try {
                Piece newPiece = type.getDeclaredConstructor(Player.class, Square.class).newInstance(this, position);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
            // Replace pawnToPromote with newPiece
        }
        // else handle invalid pieceType
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
