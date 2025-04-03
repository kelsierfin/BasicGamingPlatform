package ca.ucalgary.seng.p3.server.gamelogic.tictactoe;

public class Quit implements Options {
    private final GameRunner runner;

    public Quit(GameRunner runner) {
        this.runner = runner;
    }

    @Override
    public void execute() {
        runner.quitApp();
    }

    @Override
    public String title() {return "No";}
}