package server.gamelogic.connect4;

import java.util.Scanner;

public class Connect4 {
    public static void main(String[] args) {
        Connect4_Board board = new Connect4_Board();
        Scanner scanner  = new Scanner(System.in);
        String player1_input;
        String player2_symbol;
        boolean moveValid = false;

        boolean gameEnd = false;

        while(true) {
            System.out.println("Pick a symbol to play as (Y) or (R)");
            player1_input = scanner.nextLine();
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


        while(!gameEnd){


            while(!moveValid){
                System.out.println("Player One turn: (select a column to place disc)");
                player1_input = scanner.nextLine();
                moveValid = board.validMove(Integer.parseInt(player1_input));
            }

            board.placeDisc(Integer.parseInt(player1_input), new Disc(player_one.getSelectedSymbol()));
            board.printBoard();

            moveValid = false;



            while(!moveValid){
                System.out.println("Player Two turn: (select a column to place disc)");
                player1_input = scanner.nextLine();
                moveValid = board.validMove(Integer.parseInt(player1_input));
            }

            board.placeDisc(Integer.parseInt(player1_input), new Disc(player_two.getSelectedSymbol()));
            board.printBoard();

            moveValid = false;



        }







    }
}
