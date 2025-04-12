package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class Knight extends Piece {

    public Knight(ChessPlayer player, boolean isOnKingsSide) {
        super(player,'N',3);

        int rankIndex;
        int fileIndex;

        if (isWhite) rankIndex = 0;
        else rankIndex = 7;

        if (isOnKingsSide) fileIndex = 6;
        else fileIndex = 1;

        position = ChessGame.BOARD[rankIndex][fileIndex];
        player.game.boardLayout.put(position, this);

        initializeMoveOptions();
    }

    public Knight(ChessPlayer player, Square position) {
        super(player,'N',3, position);
    }

    protected void initializeMoveOptions() {
        Square lowFileOption;
        Square highFileOption;
        int rankIndex;

        if (isWhite) rankIndex = position.rankIndex + 2;
        else rankIndex = position.rankIndex - 2;

        lowFileOption = ChessGame.BOARD[rankIndex][position.fileIndex - 1];
        highFileOption = ChessGame.BOARD[rankIndex][position.fileIndex + 1];

        moveOptions.add(lowFileOption);
        moveOptions.add(highFileOption);
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();

        if (pinLine != null) return;

        Square possibleMoveOption;

        if (position.rankIndex + 2 < 8 && position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 2][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex + 2 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 2];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 2 >= 0 && position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 2][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex - 2 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex - 2];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 2 < 8 && position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 2][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex - 2 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 2];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 2 >= 0 && position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 2][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex + 2 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex + 2];
            processPossibleMoveOption(possibleMoveOption);
        }
    }

    private void processPossibleMoveOption(Square possibleMoveOption) {
        Piece pieceOnSquare = player.game.boardLayout.get(possibleMoveOption);
        if (player.king.attacker == null && (pieceOnSquare == null || pieceOnSquare.player == player.opponent))
                moveOptions.add(possibleMoveOption);
        else {
            if (pieceOnSquare == player.king.attacker) moveOptions.add(possibleMoveOption);
            else if (pieceOnSquare == null && player.king.attacker instanceof PinnerPiece p) {
                if (p.kingAttackLine.equals(p.squareToAttackLine.get(possibleMoveOption)))
                    moveOptions.add(possibleMoveOption);
            }
        }
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();
        Square attackedSquare;
        Piece pieceOnSquare;

        if (position.rankIndex + 2 < 8 && position.fileIndex + 1 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 2][position.fileIndex + 1];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex + 2 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 2];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex - 2 >= 0 && position.fileIndex - 1 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 2][position.fileIndex - 1];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex - 2 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex - 2];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex + 2 < 8 && position.fileIndex - 1 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 2][position.fileIndex - 1];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex - 2 >= 0) {
            attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 2];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex - 2 >= 0 && position.fileIndex + 1 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 2][position.fileIndex + 1];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex + 2 < 8) {
            attackedSquare = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex + 2];
            attackedSquares.add(attackedSquare);
            pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }
    }

}
