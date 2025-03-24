import java.util.List;
import java.util.Scanner;

public class GoGame {
    public static void main(String[] args) {
        Board board = new Board();
        Player blackPlayer = new Player(1, 1001, "pw1");
        Player whitePlayer = new Player(2, 1002, "pw2");
        int step = 0;
        Scanner scanner = new Scanner(System.in);

        while (board.state == 0) {
            Player currentPlayer = (step % 2 == 0) ? blackPlayer : whitePlayer;
            System.out.println("Player:" + (currentPlayer.getTeam() == 1 ? "Black" : "White"));

            // show available positions.This method should be redesigned when
            //designing the UI.
            List<int[]> available = board.getAvailablePositions();
            System.out.println("Available Position" + available.size());

            // Player input.This method should be redesigned when designing the UI.
            
            System.out.print("Input coordinate or pass or surrender: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("pass")) {
                board.passTurn();
                step++;
            } else if (input.equalsIgnoreCase("surrender")) {
                currentPlayer.surrender(board);
                break;
            } else {
                try {
                    String[] parts = input.split(" ");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    currentPlayer.placeStone(x, y, board);
                    step++;
                    board.consecutivePass = 0; // reset consecutive pass.
                } catch (Exception e) {
                    System.out.println("Invalid input, please try again.");
                }
            }

            // print board.Redesign this method when designing the UI.
            printBoard(board);
        }

        scanner.close();
        System.out.println("Game end, Winner is: " +
                (board.state == 1 ? "Black" :
                        board.state == 2 ? "White" :
                                "Surrender"));
    }

    private static void printBoard(Board board) {
        System.out.println("Board:");
        for (int i = 0; i < 5; i++) { 
            for (int j = 0; j < 5; j++) {
                System.out.print(board.matrix[i][j].getColor() + " ");
            }
            System.out.println();
        }
    }
}