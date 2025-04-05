import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
//This is an AI fixed edition, please do test after merging with the GUI cause I didn't know how to handle exception
//of Jackson system, so I used AI to debug and generate the print borad and data handling method, which may lead to many
//errors, please double check and tell me which part should be fixed.
public class GoGame {
    public static void main(String[] args) {
        Board board = new Board();
        Player blackPlayer;
        Player whitePlayer;

        try {
            List<Player> loadedPlayers = PlayerDataManager.loadPlayers("players.json");
            blackPlayer = loadedPlayers.get(0);
            whitePlayer = loadedPlayers.get(1);
        } catch (Exception e) {
            blackPlayer = new Player(1, 1001, "pw1");
            whitePlayer = new Player(2, 1002, "pw2");
        }

        int step = 0;
        Scanner scanner = new Scanner(System.in);

        while (board.state == 0) {
            Player currentPlayer = (step % 2 == 0) ? blackPlayer : whitePlayer;
            System.out.println("Player:" + (currentPlayer.getTeam() == 1 ? "Black" : "White"));

            List<int[]> available = board.getAvailablePositions();
            System.out.println("Available Position" + available.size());

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
                    board.consecutivePass = 0;
                } catch (Exception e) {
                    System.out.println("Invalid input, please try again.");
                }
            }

            printBoard(board);
        }

        scanner.close();
        System.out.println("Game end, Winner is: " +
                (board.state == 1 ? "Black" :
                        board.state == 2 ? "White" :
                                "Surrender"));

        try {
            PlayerDataManager.savePlayers(List.of(blackPlayer, whitePlayer), "players.json");
        } catch (Exception e) {
            System.out.println("Failed to save player data.");
        }
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