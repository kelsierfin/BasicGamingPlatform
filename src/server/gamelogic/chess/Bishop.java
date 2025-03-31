package server.gamelogic.chess;

public class Bishop extends PinnerPiece {

    public Bishop(Player player, boolean isOnKingsSide) {
        super(player,3);
        if (isWhite) {
            if (isOnKingsSide) position = ChessGame.BOARD[0][5];
            else position = ChessGame.BOARD[0][2];
        }
        else {
            if (isOnKingsSide) position = ChessGame.BOARD[7][5];
            else position = ChessGame.BOARD[7][2];
        }
    }

    public Bishop(Player player, Square position) {
        super(player,3, position);
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();

        int i;
        boolean pieceReached;

        if (pinner == null || pinner.pinLine.equals("LH")) {
            // Find move options for the diagonal direction characterized by decreasing rank and file
            i = 1;
            pieceReached = false;
            while ((position.rankIndex - i >= 0) && (position.fileIndex - i >= 0) && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex - i][position.fileIndex - i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }

            // Find move options for the diagonal direction characterized by increasing rank and file
            i = 1;
            pieceReached = false;
            while ((position.rankIndex + i < 8) && (position.fileIndex + i < 8) && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex + i][position.fileIndex + i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }
        }

        if (pinner == null || pinner.pinLine.equals("RF")) {
            // Find move options for the diagonal direction characterized by increasing rank and decreasing file
            i = 1;
            pieceReached = false;
            while ((position.rankIndex + i < 8) && (position.fileIndex - i >= 0) && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex + i][position.fileIndex - i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }

            // Find move options for the diagonal direction characterized by decreasing rank and increasing file
            i = 1;
            pieceReached = false;
            while ((position.rankIndex - i >= 0) && (position.fileIndex + i < 8) && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex - i][position.fileIndex + i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }
        }
    }

    private boolean processPossibleMoveOption(Square possibleMoveOption) {
        Piece pieceOnSquare = player.game.boardLayout.get(possibleMoveOption);
        if (player.king.attacker == null) {
            if (pieceOnSquare == null) {
                moveOptions.add(possibleMoveOption);
                return false;
            }
            if (pieceOnSquare.player == player.opponent) moveOptions.add(possibleMoveOption);
        } else {
            if (pieceOnSquare == null) {
                if (player.king.attacker instanceof PinnerPiece p) {
                    if (p.squareToAttackLine.get(possibleMoveOption).equals(p.pinLine)) {
                        moveOptions.add(possibleMoveOption);
                    }
                }
                return false;
            }
            if (pieceOnSquare == player.king.attacker) moveOptions.add(possibleMoveOption);
        }
        return true;
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();

        // Find attacked squares for the diagonal direction characterized by decreasing rank and file
        int i = 1;
        Piece firstPieceReached = null;
        Piece secondPieceReached = null;
        while (position.rankIndex - i >= 0 && position.fileIndex - i >= 0 && firstPieceReached != player.opponent.king && secondPieceReached == null) {
            Square inspectedSquare = ChessGame.BOARD[position.rankIndex-i][position.fileIndex-i];
            if (firstPieceReached == null) {
                attackedSquares.add(inspectedSquare);
                squareToAttackLine.put(inspectedSquare, "LH");
                firstPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (firstPieceReached == player.opponent.king) {
                    player.opponent.king.attacker = this;
                }
            } else {
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (secondPieceReached == player.opponent.king) {
                    firstPieceReached.pinner = this;
                    pinLine = "LH";
                }
            }
            i++;
        }

        // Find attacked squares for the diagonal direction characterized by increasing rank and decreasing file
        i = 1;
        firstPieceReached = null;
        secondPieceReached = null;
        while (position.rankIndex + i < 8 && position.fileIndex - i >= 0 && firstPieceReached != player.opponent.king && secondPieceReached == null) {
            Square inspectedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex - i];
            if (firstPieceReached == null) {
                attackedSquares.add(inspectedSquare);
                squareToAttackLine.put(inspectedSquare, "RF");
                firstPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (firstPieceReached == player.opponent.king) {
                    player.opponent.king.attacker = this;
                }
            } else {
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (secondPieceReached == player.opponent.king) {
                    firstPieceReached.pinner = this;
                    pinLine = "RF";
                }
            }
            i++;
        }

        // Find attacked squares for the diagonal direction characterized by increasing rank and file
        i = 1;
        firstPieceReached = null;
        secondPieceReached = null;
        while (position.rankIndex + i < 8 && position.fileIndex + i < 8 && firstPieceReached != player.opponent.king && secondPieceReached == null) {
            Square inspectedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex + i];
            if (firstPieceReached == null) {
                attackedSquares.add(inspectedSquare);
                squareToAttackLine.put(inspectedSquare, "LH");
                firstPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (firstPieceReached == player.opponent.king) {
                    player.opponent.king.attacker = this;
                }
            } else {
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (secondPieceReached == player.opponent.king) {
                    firstPieceReached.pinner = this;
                    pinLine = "LH";
                }
            }
            i++;
        }

        // Find attacked squares for the diagonal direction characterized by decreasing rank and increasing file
        i = 1;
        firstPieceReached = null;
        secondPieceReached = null;
        while (position.rankIndex - i >= 0 && position.fileIndex + 1 < 8 && firstPieceReached != player.opponent.king && secondPieceReached == null) {
            Square inspectedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex + i];
            if (firstPieceReached == null) {
                attackedSquares.add(inspectedSquare);
                squareToAttackLine.put(inspectedSquare, "RF");
                firstPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (firstPieceReached == player.opponent.king) {
                    player.opponent.king.attacker = this;
                }
            } else {
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                if (secondPieceReached == player.opponent.king) {
                    firstPieceReached.pinner = this;
                    pinLine = "RF";
                }
            }
            i++;
        }
    }
}
