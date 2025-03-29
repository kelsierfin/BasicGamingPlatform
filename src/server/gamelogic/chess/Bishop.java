package server.gamelogic.chess;

public class Bishop extends Piece {

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

        // Find move options for the diagonal direction characterized by decreasing rank and file
        int i = 1;
        boolean pieceReached = false;
        while ((position.rankIndex - i >= 0) && (position.fileIndex - i >= 0) && !pieceReached) {
            Square possibleMoveOption = ChessGame.BOARD[position.rankIndex-i][position.fileIndex-i];
            pieceReached = processPossibleMoveOption(possibleMoveOption);
            i++;
        }

        // Find move options for the diagonal direction characterized by increasing rank and decreasing file
        i = 1;
        pieceReached = false;
        while ((position.rankIndex + i < 8) && (position.fileIndex - i >= 0) && !pieceReached) {
            Square possibleMoveOption = ChessGame.BOARD[position.rankIndex+i][position.fileIndex-i];
            pieceReached = processPossibleMoveOption(possibleMoveOption);
            i++;
        }

        // Find move options for the diagonal direction characterized by increasing rank and file
        i = 1;
        pieceReached = false;
        while ((position.rankIndex + i < 8) && (position.fileIndex + i < 8) && !pieceReached) {
            Square possibleMoveOption = ChessGame.BOARD[position.rankIndex+i][position.fileIndex+i];
            pieceReached = processPossibleMoveOption(possibleMoveOption);
            i++;
        }

        // Find move options for the diagonal direction characterized by decreasing rank and increasing file
        i = 1;
        pieceReached = false;
        while ((position.rankIndex - i >= 0) && (position.fileIndex + i < 8) && !pieceReached) {
            Square possibleMoveOption = ChessGame.BOARD[position.rankIndex-i][position.fileIndex+i];
            pieceReached = processPossibleMoveOption(possibleMoveOption);
            i++;
        }

        moveOptionsUpdated = true;
    }

    private boolean processPossibleMoveOption(Square possibleMoveOption) {
        Piece pieceOnSquare = player.game.boardLayout.get(possibleMoveOption);
        if (pieceOnSquare == null) {
            moveOptions.add(possibleMoveOption);
            return false;
        }
        if (pieceOnSquare.player == player.opponent) moveOptions.add(possibleMoveOption);
        return true;
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();

        // Find attacked squares for the diagonal direction characterized by decreasing rank and file
        int i = 1;
        boolean pieceReached = false;
        while ((position.rankIndex - i >= 0) && (position.fileIndex - i >= 0) && !pieceReached) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex-i][position.fileIndex-i];
            attackedSquares.add(attackedSquare);
            Piece pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare != null) pieceReached = true;
            i++;
        }

        // Find attacked squares for the diagonal direction characterized by increasing rank and decreasing file
        i = 1;
        pieceReached = false;
        while ((position.rankIndex + i < 8) && (position.fileIndex - i >= 0) && !pieceReached) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex+i][position.fileIndex-i];
            attackedSquares.add(attackedSquare);
            Piece pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare != null) pieceReached = true;
            i++;
        }

        // Find attacked squares for the diagonal direction characterized by increasing rank and file
        i = 1;
        pieceReached = false;
        while ((position.rankIndex + i < 8) && (position.fileIndex + i < 8) && !pieceReached) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex+i][position.fileIndex+i];
            attackedSquares.add(attackedSquare);
            Piece pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare != null) pieceReached = true;
            i++;
        }

        // Find attacked squares for the diagonal direction characterized by decreasing rank and increasing file
        i = 1;
        pieceReached = false;
        while ((position.rankIndex - i >= 0) && (position.fileIndex + i < 8) && !pieceReached) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex-i][position.fileIndex+i];
            attackedSquares.add(attackedSquare);
            Piece pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare != null) pieceReached = true;
            i++;
        }
    }
}
