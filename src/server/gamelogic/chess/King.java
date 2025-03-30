package server.gamelogic.chess;

public class King extends Piece {
    Piece attacker;
    boolean hasMoved;

    public King(Player player) {
        super(player, 0);

        player.king = this;
        hasMoved = false;

        int fileIndex;
        if (isWhite) fileIndex = 0;
        else fileIndex = 7;

        position = ChessGame.BOARD[fileIndex][4];
    }

    @Override
    public void updateMoveOptions() {
        moveOptions.clear();
        Square possibleMoveOption;

        if (position.rankIndex - 1 >= 0 && position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex - 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex + 1 < 8 && position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex + 1][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0 && position.fileIndex + 1 < 8) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex + 1];
            processPossibleMoveOption(possibleMoveOption);
        }

        if (position.rankIndex - 1 >= 0) {
            possibleMoveOption = ChessGame.BOARD[position.rankIndex - 1][position.fileIndex];
            processPossibleMoveOption(possibleMoveOption);
        }

    }

    private void processPossibleMoveOption(Square possibleMoveOption) {
        Piece pieceOnSquare = player.game.boardLayout.get(possibleMoveOption);
        if ((pieceOnSquare == null || pieceOnSquare.player == player.opponent)
                && !player.opponent.attackedSquares.contains(possibleMoveOption))
            moveOptions.add(possibleMoveOption);
    }

    @Override
    public void updateAttackedSquares() {

    }
}
