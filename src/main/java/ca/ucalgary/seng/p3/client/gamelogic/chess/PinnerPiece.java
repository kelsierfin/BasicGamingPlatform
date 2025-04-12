package ca.ucalgary.seng.p3.client.gamelogic.chess;

import java.util.HashMap;

public abstract class PinnerPiece extends Piece{
    HashMap<Square, String> squareToAttackLine;
    String kingAttackLine;

    public PinnerPiece(ChessPlayer player, char type, int value) {
        super(player, type, value);
        squareToAttackLine = new HashMap<>();
    }

    public PinnerPiece(ChessPlayer player, char type, int value, Square position) {
        super(player, type, value, position);
        squareToAttackLine = new HashMap<>();
    }

    protected void addGridWiseMoveOptions() {
        if (pinLine != null && (pinLine.equals("LH") || pinLine.equals("RF"))) return;

        int i;
        boolean pieceReached;

        if (pinLine == null || pinLine.equals("F")) {
            // Find move options for direction characterized by increasing rank
            i = 1;
            pieceReached = false;
            while (position.rankIndex + i < 8 && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex + i][position.fileIndex];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }

            // Find move options for direction characterized by decreasing rank
            i = 1;
            pieceReached = false;
            while (position.rankIndex - i >= 0 && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex - i][position.fileIndex];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }
        }

        if (pinLine == null || pinLine.equals("R")) {
            // Find move options for direction characterized by increasing file
            i = 1;
            pieceReached = false;
            while (position.fileIndex + i < 8 && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex][position.fileIndex + i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }

            // Find move options for direction characterized by decreasing file
            i = 1;
            pieceReached = false;
            while (position.fileIndex - i >= 0 && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex][position.fileIndex - i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }
        }
    }

    protected void addDiagonalMoveOptions() {
        if (pinLine != null && (pinLine.equals("R") || pinLine.equals("F"))) return;

        int i;
        boolean pieceReached;

        if (pinLine == null || pinLine.equals("LH")) {
            // Find move options for the diagonal direction characterized by increasing rank and file
            i = 1;
            pieceReached = false;
            while ((position.rankIndex + i < 8) && (position.fileIndex + i < 8) && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex + i][position.fileIndex + i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }

            // Find move options for the diagonal direction characterized by decreasing rank and file
            i = 1;
            pieceReached = false;
            while ((position.rankIndex - i >= 0) && (position.fileIndex - i >= 0) && !pieceReached) {
                Square possibleMoveOption = ChessGame.BOARD[position.rankIndex - i][position.fileIndex - i];
                pieceReached = processPossibleMoveOption(possibleMoveOption);
                i++;
            }
        }

        if (pinLine == null || pinLine.equals("RF")) {
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

    protected boolean processPossibleMoveOption(Square possibleMoveOption) {
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
                    if (p.kingAttackLine.equals(p.squareToAttackLine.get(possibleMoveOption))) {
                        moveOptions.add(possibleMoveOption);
                    }
                }
                return false;
            }
            if (pieceOnSquare == player.king.attacker) moveOptions.add(possibleMoveOption);
        }
        return true;
    }

    protected void addGridWiseAttackLines() {
        int i;
        Piece firstPieceReached;
        Piece secondPieceReached;

        // Find attacked squares for direction characterized by increasing rank
        i = 1;
        firstPieceReached = null;
        while (position.rankIndex + i < 8 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "F");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "F";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent){
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.rankIndex + i < 8 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "F";
            }
        }

        // Find attacked squares for direction characterized by decreasing rank
        i = 1;
        firstPieceReached = null;
        while (position.rankIndex - i >= 0 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "F");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "F";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent){
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.rankIndex - i >= 0 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "F";
            }
        }

        // Find attacked squares for the diagonal direction characterized by increasing file
        i = 1;
        firstPieceReached = null;
        while (position.fileIndex + i < 8 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex][position.fileIndex + i];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "R");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "R";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent) {
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.fileIndex + i < 8 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex][position.fileIndex + i];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "R";
            }
        }

        // Find attacked squares for direction characterized by decreasing file
        i = 1;
        firstPieceReached = null;
        while (position.fileIndex - i >= 0 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex][position.fileIndex - i];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "R");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "R";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent){
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.fileIndex - i >= 0 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex][position.fileIndex - i];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "R";
            }
        }
    }

    protected void addDiagonalAttackLines() {
        int i;
        Piece firstPieceReached;
        Piece secondPieceReached;

        // Find attacked squares for the diagonal direction characterized by increasing rank and file
        i = 1;
        firstPieceReached = null;
        while (position.rankIndex + i < 8 && position.fileIndex + i < 8 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex + i];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "LH");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "LH";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent){
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.rankIndex + i < 8 && position.fileIndex + 1 < 8 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex + i];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "LH";
            }
        }

        // Find attacked squares for the diagonal direction characterized by decreasing rank and file
        i = 1;
        firstPieceReached = null;
        while (position.rankIndex - i >= 0 && position.fileIndex - i >= 0 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex - i];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "LH");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "LH";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent){
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.rankIndex - i >= 0 && position.fileIndex - i >= 0 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex - i];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "LH";
            }
        }

        // Find attacked squares for the diagonal direction characterized by increasing rank and decreasing file
        i = 1;
        firstPieceReached = null;
        while (position.rankIndex + i < 8 && position.fileIndex - i >= 0 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex - i];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "RF");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "RF";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent){
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.rankIndex + i < 8 && position.fileIndex - i >= 0 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex + i][position.fileIndex - i];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "RF";
            }
        }

        // Find attacked squares for the diagonal direction characterized by decreasing rank and increasing file
        i = 1;
        firstPieceReached = null;
        while (position.rankIndex - i >= 0 && position.fileIndex + i < 8 && firstPieceReached == null) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex + i];
            attackedSquares.add(attackedSquare);
            squareToAttackLine.put(attackedSquare, "RF");
            firstPieceReached = player.game.boardLayout.get(attackedSquare);
            i++;
        }
        if (firstPieceReached == player.opponent.king) {
            player.opponent.king.attacker = this;
            kingAttackLine = "RF";
        } else if (firstPieceReached != null && firstPieceReached.player == player.opponent) {
            // Figure out if first piece reached is pinned to king
            secondPieceReached = null;
            while (position.rankIndex - i >= 0 && position.fileIndex + i < 8 && secondPieceReached == null) {
                Square inspectedSquare = ChessGame.BOARD[position.rankIndex - i][position.fileIndex + i];
                secondPieceReached = player.game.boardLayout.get(inspectedSquare);
                i++;
            }
            if (secondPieceReached == player.opponent.king) {
                firstPieceReached.pinLine = "RF";
            }
        }
    }
}
