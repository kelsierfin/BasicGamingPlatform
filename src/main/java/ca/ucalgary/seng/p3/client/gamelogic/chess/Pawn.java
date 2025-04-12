package ca.ucalgary.seng.p3.client.gamelogic.chess;

public class Pawn extends Piece {
    private final int originRankIndex;

    public Pawn(ChessPlayer player, int file) {
        super(player,'P',1);
        if (isWhite) originRankIndex = 1;
        else originRankIndex = 6;
        position = ChessGame.BOARD[originRankIndex][file-1];
        initializeMoveOptions();
    }

    protected void initializeMoveOptions() {
        Square squareInFront;
        if (isWhite) squareInFront = ChessGame.BOARD[originRankIndex + 1][position.fileIndex];
        else squareInFront = ChessGame.BOARD[originRankIndex - 1][position.fileIndex];
        moveOptions.add(squareInFront);

        Square squareTwoInFront;
        if (isWhite) squareTwoInFront = ChessGame.BOARD[originRankIndex + 2][position.fileIndex];
        else squareTwoInFront = ChessGame.BOARD[originRankIndex - 2][position.fileIndex];
        moveOptions.add(squareTwoInFront);
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();

        if (pinLine != null && pinLine.equals("R")) return;

        if (pinLine == null || pinLine.equals("F")) {
            Square squareInFront;
            if (isWhite) squareInFront = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex];
            else squareInFront = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex];
            if (player.game.boardLayout.get(squareInFront) == null) {
                moveOptions.add(squareInFront);
                if (position.rankIndex == originRankIndex) {
                    Square squareTwoInFront;
                    if (isWhite) squareTwoInFront = ChessGame.BOARD[originRankIndex + 2][position.fileIndex];
                    else squareTwoInFront = ChessGame.BOARD[originRankIndex - 2][position.fileIndex];
                    if (player.game.boardLayout.get(squareTwoInFront) == null)
                        moveOptions.add(squareTwoInFront);
                }
            }
        }

        if ((pinLine == null || pinLine.equals("LH")) && position.rankIndex + 1 < 8 && position.fileIndex + 1 < 8) {
            Square possibleAttack = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 1];
            Piece pieceOnSquare = player.game.boardLayout.get(possibleAttack);
            if (pieceOnSquare.player == player.opponent) moveOptions.add(possibleAttack);
        }

        if ((pinLine == null || pinLine.equals("RF")) && position.rankIndex + 1 < 8 && position.fileIndex - 1 >= 0) {
            Square possibleAttack = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 1];
            Piece pieceOnSquare = player.game.boardLayout.get(possibleAttack);
            if (pieceOnSquare.player == player.opponent) moveOptions.add(possibleAttack);
        }
    }

    @Override
    public void updateAttackedSquares() {
        attackedSquares.clear();

        if (position.rankIndex + 1 < 8 && position.fileIndex + 1 < 8) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 1];
            attackedSquares.add(attackedSquare);
            Piece pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex - 1 >= 0) {
            Square attackedSquare = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 1];
            attackedSquares.add(attackedSquare);
            Piece pieceOnSquare = player.game.boardLayout.get(attackedSquare);
            if (pieceOnSquare == player.opponent.king) player.opponent.king.attacker = this;
        }
    }
}
