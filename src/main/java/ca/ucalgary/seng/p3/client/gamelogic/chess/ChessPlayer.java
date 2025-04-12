package ca.ucalgary.seng.p3.client.gamelogic.chess;

import ca.ucalgary.seng.p3.client.ClientNetworkManager;
import ca.ucalgary.seng.p3.client.controllers.ChessController;
import ca.ucalgary.seng.p3.client.game_to_gui_commands.chess.*;
import ca.ucalgary.seng.p3.client.gamelogic.GeneralPlayer;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ChessPlayer implements GeneralPlayer<ChessController> {
    ChessGame game;
    ClientNetworkManager network;
    ChessController gui;
    boolean playsForWhite;
    ChessPlayer opponent;
    King king;
    Rook kingSideRook;
    Rook queenSideRook;
    List<Piece> pieces;
    List<Piece> capturedPieces;
    int materialAdvantage;
    Piece selection;
    boolean promotingPawn;
    Square clickedSquare;
    Piece clickedPiece;
    boolean hasMoved;
    int numTurns;

    public ChessPlayer(ChessGame game, boolean playsForWhite) {
        this.game = game;
        this.playsForWhite = playsForWhite;
        pieces = new ArrayList<>();
        initializePieces();
        capturedPieces = new ArrayList<>();
        materialAdvantage = 0;
    }

    @Override
    public void setNetwork(ClientNetworkManager network) {
        this.network = network;
    }

    @Override
    public void setGUI(ChessController gui) {
        this.gui = gui;
    }

    // May be renamed to emulateTurn()
    @Override
    public boolean updateGame(JSONObject update) throws IllegalArgumentException {
        try {
            Square moveSource = ChessGame.NAME_TO_SQUARE.get(update.getString("moveSource"));
            if (moveSource == null) throw new IllegalArgumentException("moveSource string isn't a square name");

            selection = game.boardLayout.get(moveSource);
            if (selection == null) throw new IllegalArgumentException("Square found through moveSource is empty");
            if (!pieces.contains(selection)) throw new IllegalArgumentException("Square found through moveSource holds an opponent piece");

            try {
                clickedSquare = ChessGame.NAME_TO_SQUARE.get(update.getString("moveDest"));
                if (clickedSquare == null) throw new IllegalArgumentException("moveDest string isn't a square name");
                else if (!selection.moveOptions.contains(clickedSquare)) throw new IllegalArgumentException("square found through moveDest is not a move option");
                clickedPiece = game.boardLayout.get(clickedSquare);

                promotingPawn = (selection instanceof Pawn && ((playsForWhite && clickedSquare.rankIndex == 7)
                        || (!playsForWhite && clickedSquare.rankIndex == 0)));

                String newPieceTypeString = update.optString("newPieceType");
                ShowMove guiUpdateInfo;

                if (newPieceTypeString.isEmpty()) {
                    if (promotingPawn) throw new IllegalArgumentException("newPieceType for pawn promotion is missing");
                    guiUpdateInfo = move();
                } else {
                    if (!promotingPawn) throw new IllegalArgumentException("No pawn promotion can occur");
                    if (newPieceTypeString.equals("null")) throw new IllegalArgumentException("newPieceType must be a string");
                    try {
                        guiUpdateInfo = promotePawn(newPieceTypeString.charAt(0));
                    } catch (InvalidPromotionTypeException e) {
                        throw new IllegalArgumentException("newPieceType is not a valid promotion type");
                    }
                }
                // gui.updateGUI(guiUpdateInfo);
                return game.activePlayer == null;
            } catch (JSONException e) {
                throw new IllegalArgumentException("moveDest must exist and have a string value");
            }
        } catch (JSONException e) {
            throw new IllegalArgumentException("moveSource must exist and have a string value");
        }
    }

    public ShowMove promotePawn(char pieceType) throws InvalidPromotionTypeException, ReflectiveInstantiationException {
        Class<? extends Piece> type = ChessGame.SYMBOL_TO_PIECE.get(pieceType);
        if ((type == Queen.class) || (type == Rook.class) || (type == Knight.class) || (type == Bishop.class)) {
            try {
                Piece newPiece = type.getDeclaredConstructor(ChessPlayer.class, Square.class).newInstance(this, selection.position);
                pieces.remove(selection);
                pieces.add(newPiece);
                selection = newPiece;

                ShowMove guiUpdateInfo = move();
                promotingPawn = false;
                return guiUpdateInfo;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new ReflectiveInstantiationException();
            }
        }
        // else handle invalid pieceType
        throw new InvalidPromotionTypeException();
    }

    protected GameToGuiCommand enactClick(String squareName) throws InvalidSquareNameException {
        clickedSquare = ChessGame.NAME_TO_SQUARE.get(squareName);
        if (clickedSquare == null) throw new InvalidSquareNameException();
        clickedPiece = game.boardLayout.get(clickedSquare);
        GameToGuiCommand response;

        if (selection != null && selection.moveOptions.contains(clickedSquare)) {
            if (selection instanceof Pawn && ((playsForWhite && clickedSquare.rankIndex == 7)
                    || (!playsForWhite && clickedSquare.rankIndex == 0))) {
                promotingPawn = true;
                response = new GetPromotionType();  // Instruction prompting for piece type selection for pawn promotion
            }
            else response = move();
        }
        else if (clickedPiece != null && clickedPiece.player == this) {
            response = select();
        }
        else response = deselect();

        return response;
    }

    protected ShowMove move() {
        Piece movedPiece = selection;
        selection = null;
        Square destination = clickedSquare;
        Piece capturedPiece = clickedPiece;
        king.attacker = null;
        numTurns++;

        ShowMove guiUpdateInfo = new ShowMove(playsForWhite);
        guiUpdateInfo.squareToEmpty = movedPiece.position.name;
        guiUpdateInfo.squareToFill = destination.name;
        guiUpdateInfo.pieceType = movedPiece.type;

        JSONObject emulateTurnInfo = new JSONObject();
        emulateTurnInfo.put("moveSource", movedPiece.position.name);
        emulateTurnInfo.put("moveDest", destination.name);

        if (capturedPiece != null) {
            capturedPieces.add(capturedPiece);
            materialAdvantage += capturedPiece.value;
            opponent.materialAdvantage -= capturedPiece.value;
            if (capturedPiece == kingSideRook) kingSideRook.captured = true;
            else if (capturedPiece == queenSideRook) queenSideRook.captured = true;

            guiUpdateInfo.materialAdvantageIncrease = capturedPiece.value;
            guiUpdateInfo.captureInfo = new CaptureInfo(capturedPiece.type);
        }

        if (promotingPawn) {
            guiUpdateInfo.materialAdvantageIncrease += selection.value - 1;
            emulateTurnInfo.put("newPieceType", selection.type + "");
        }

        game.boardLayout.remove(movedPiece.position);
        game.boardLayout.put(destination, movedPiece);
        movedPiece.position = destination;

        if (movedPiece == king && !king.hasMoved) {
            if (destination == kingSideRook.kingCastleDest) castle(kingSideRook, guiUpdateInfo);
            if (destination == queenSideRook.kingCastleDest) castle(queenSideRook, guiUpdateInfo);
            king.hasMoved = true;
        }

        if (movedPiece instanceof Rook r) {
            if (!r.hasMoved) r.hasMoved = true;
        }

        if (hasMoved) {
            updateAttackedSquares(movedPiece);
            opponent.updateMoveOptions();

            if (opponent.hasMoveOptions()) game.activePlayer = opponent;
            else {
                if (opponent.king.attacker != null) {
                    // Checkmate
                    if (playsForWhite) game.winner = 1;
                    else game.winner = 2;
                    guiUpdateInfo.gameOverInfo = new GameOverInfo(true);
                } else {
                    // Draw
                    // Keep game.winner at initial value of 0
                    guiUpdateInfo.gameOverInfo = new GameOverInfo(false);
                }
                game.activePlayer = null;
            }
        } else {
            hasMoved = true;
            if (opponent.hasMoved) opponent.updateMoveOptions();
            game.activePlayer = opponent;
        }

        try {
            network.sendMessage(emulateTurnInfo);
        } catch (IOException e) {
            guiUpdateInfo.clientDisconnected = true;
        }
        return guiUpdateInfo;
    }

    private void castle(Rook rook, ShowMove guiUpdateInfo) {
        guiUpdateInfo.castleInfo = new CastleInfo();
        guiUpdateInfo.castleInfo.rookSource = rook.position.name;
        guiUpdateInfo.castleInfo.rookDest = rook.castleDest.name;

        game.boardLayout.remove(rook.position);
        game.boardLayout.put(rook.castleDest, rook);
        rook.position = rook.castleDest;
        rook.hasMoved = true;
    }

    private Select select() {
        Select selectInfo = new Select();
        selectInfo.squareToDeselect = selection.position.name;
        this.selection = clickedPiece;
        for (Square moveOption : selection.moveOptions)
            selectInfo.moveOptions.add(moveOption.name);
        return selectInfo;
    }

    private Deselect deselect() {
        Deselect deselectInfo = new Deselect();
        deselectInfo.squareToDeselect = selection.position.name;
        selection = null;
        return deselectInfo;
    }

    protected void updateAttackedSquares(Piece movedPiece) {
        List<Square> attackedSquares = opponent.king.opponentAttackedSquares;
        attackedSquares.clear();

        for (Piece piece : opponent.pieces) piece.pinLine = null;

        for (Piece piece : pieces) {
            if (piece instanceof PinnerPiece || piece == movedPiece) piece.updateAttackedSquares();

            for (Square square : piece.attackedSquares) {
                if (!attackedSquares.contains(square)) attackedSquares.add(square);
            }
        }
    }

    protected void updateMoveOptions() {
        for (Piece piece : pieces) piece.updateMoveOptions();
    }

    protected boolean hasMoveOptions() {
        for (Piece piece : pieces) {
            if (!piece.moveOptions.isEmpty()) return true;
        }
        return false;
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
