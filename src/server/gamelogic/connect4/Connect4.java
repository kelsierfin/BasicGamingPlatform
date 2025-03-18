package server.gamelogic.connect4;

public class Connect4 {
    public static void main(String[] args) {
        Connect4_Board board = new Connect4_Board();
        board.printBoard();
        board.placeDisc(3, new Disc("0"));
        board.printBoard();
    }
}
