package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

import java.io.PrintStream;
import java.util.Scanner;

public class TicTacToe {
    private static Input input;
    private static Print print;

    public static void main(String[] args) {
        print = new Print(new PrintStream(System.out));
        input = new Input(new Scanner(System.in), print);
        GameRunner runner = new GameRunner(input, print);
        runner.run();
    }
}
