package ca.ucalgary.seng.p3.client.gamelogic.chess;

import java.util.List;

public class King extends Piece {
    Piece attacker;
    List<Square> opponentAttackedSquares;
    boolean hasMoved;

    public King(ChessPlayer player) {
        super(player,'K',0);

        player.king = this;
        hasMoved = false;

        int fileIndex;
        if (isWhite) fileIndex = 0;
        else fileIndex = 7;

        position = ChessGame.BOARD[fileIndex][4];
        player.game.boardLayout.put(position, this);
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();
        Square possibleMoveOption;

        if (position.rankIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        // Add castling options
        if (!hasMoved && attacker == null) {
            if (!player.kingSideRook.hasMoved && !player.kingSideRook.captured &&
                    player.game.boardLayout.get(player.kingSideRook.castleDest) == null &&
                    player.game.boardLayout.get(player.kingSideRook.kingCastleDest) == null &&
                    !opponentAttackedSquares.contains(player.kingSideRook.castleDest) &&
                    !opponentAttackedSquares.contains(player.kingSideRook.kingCastleDest)) {
                moveOptions.add(player.kingSideRook.kingCastleDest);
            }
            if (!player.queenSideRook.hasMoved && !player.queenSideRook.captured &&
                    player.game.boardLayout.get(player.queenSideRook.castleDest) == null &&
                    player.game.boardLayout.get(player.queenSideRook.kingCastleDest) == null &&
                    player.game.boardLayout.get(ChessGame.BOARD[position.rankIndex][1]) == null &&
                    !opponentAttackedSquares.contains(player.queenSideRook.castleDest) &&
                    !opponentAttackedSquares.contains(player.queenSideRook.kingCastleDest)) {
                moveOptions.add(player.queenSideRook.kingCastleDest);
            }
        }
    }

    private void processPossibleMoveOption(Square possibleMoveOption) {
        Piece pieceOnSquare = player.game.boardLayout.get(possibleMoveOption);
        if ((pieceOnSquare == null || pieceOnSquare.player == player.opponent)
                && !opponentAttackedSquares.contains(possibleMoveOption))
            moveOptions.add(possibleMoveOption);
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();
        Square attackedSquare;

        if (position.rankIndex + 1 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex];
            attackedSquares.add(attackedSquare);
        }

        if (position.rankIndex - 1 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex];
            attackedSquares.add(attackedSquare);
        }

        if (position.fileIndex + 1 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex][position.fileIndex + 1];
            attackedSquares.add(attackedSquare);
        }

        if (position.fileIndex - 1 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex][position.fileIndex - 1];
            attackedSquares.add(attackedSquare);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex + 1 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 1];
            attackedSquares.add(attackedSquare);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex - 1 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex - 1];
            attackedSquares.add(attackedSquare);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex - 1 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 1];
            attackedSquares.add(attackedSquare);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex + 1 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex + 1];
            attackedSquares.add(attackedSquare);
        }
    }
}
