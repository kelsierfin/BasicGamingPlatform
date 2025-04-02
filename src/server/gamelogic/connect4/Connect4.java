package server.gamelogic.connect4;
import server.gamelogic.connect4.Player;

import java.util.Scanner;

/* Connect 4 class contains the main game logic for Connect 4. */

public class Connect4 {
    public static void main(String[] args) {

        // Defining game board and player objects.
        Connect4_Board board = new Connect4_Board();
        Scanner scanner  = new Scanner(System.in);
        String player1_input;
        String player2_symbol;

        boolean moveValid = false;

        boolean gameEnd = false;

        // while loop is exited once player one selects a symbol to play as.
        while(true) {
            System.out.println("Pick a symbol to play as (Y) or (R)");
            player1_input = scanner.nextLine();
            // The unselected symbol  is assigned to player two.
            if(player1_input.equals("Y") || player1_input.equals("R")){

                if(player1_input.equals("Y")){
                    player2_symbol = "R";
                }else{
                    player2_symbol = "Y";
                }

                break;
            }
        }


        Player player_one = new Player("Player1", player1_input);
        Player player_two = new Player("Player2", player2_symbol);
        board.printBoard();

        // Main game loop runs until a player wins the game.
        while(!gameEnd){

            // Accepts the column number player one inputs into terminal.
            // while loop repeats if player one does not select a valid column.
            while(!moveValid){
                System.out.println("Player One turn: (select a column to place disc)");
                player1_input = scanner.nextLine();
                moveValid = board.validMove(Integer.parseInt(player1_input));
            }

            // Updates and prints board with player ones attempt.
            board.placeDisc(Integer.parseInt(player1_input), new Disc(player_one.getSelectedSymbol()));
            board.printBoard();

            // Checks if player ones recent attempt resulted in a win.
            gameEnd = board.checkWin(player_one.getSelectedSymbol());
            if(gameEnd){
                System.out.println("Player One Wins!!!!!!!");
                break;
            }

            // Game continues, rest of the code is repeated for player two.
            moveValid = false;



            while(!moveValid){
                System.out.println("Player Two turn: (select a column to place disc)");
                player1_input = scanner.nextLine();
                moveValid = board.validMove(Integer.parseInt(player1_input));
            }

            board.placeDisc(Integer.parseInt(player1_input), new Disc(player_two.getSelectedSymbol()));
            board.printBoard();

            moveValid = false;

            gameEnd = board.checkWin(player_two.getSelectedSymbol());
            if(gameEnd){
                System.out.println("Player Two Wins!!!!!!!");
                break;
            }



        }







    }
}
