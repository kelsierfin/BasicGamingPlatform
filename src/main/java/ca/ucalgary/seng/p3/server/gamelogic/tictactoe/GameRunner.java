package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

import java.util.Arrays;
import java.util.List;

public class GameRunner {
    private Game game;
    private Input input;
    private Print print;
    private boolean gameRunning = true;


    GameRunner(Input input, Print print) {
        this.input = input;
        this.print = print;

    }

    void run() {
        boolean gameRunning = true;
        while (gameRunning) {
            startGame();
            if (game != null) {
                while (gameInProgress()) {
                    makeMove();
                }
                endOfGame();
            }
        }
    }


    private void startGame() {
        print.clearScreen();
        print.welcome();
        Board board = new Board(3);
        createGame(board);
    }

    private void createGame(Board board) {
        game = new PlayerVsPlayer(input).execute(board);
    }



    private boolean gameInProgress() {
        return !game.isGameOver();
    }

    private void makeMove() {
        print.clearScreen();
        print.selectSpace(game);
        print.board(game.board);
        game.takeTurn();
    }

    private int getSelection(List<? extends Menu> options) {
        return input.validateSelection(input.validSelection(options));
    }


    private void endOfGame() {
        print.clearScreen();
        print.outcome(game);
        print.board(game.board);
    }

    private void playAgain() {
        print.playAgain(); // "Play again? (y/n):"
        print.options(playCommands());
        playAgainCommand(getSelection(playCommands()));
    }
    private List<Options> playCommands() {
        return Arrays.asList(new Quit(this));
    }


    private void playAgainCommand(int selection) {
        Options playOrQuit = playCommands().get(selection - 1);
        playOrQuit.execute();
    }

    public void quitApp() {
        print.exiting();
    }
}
