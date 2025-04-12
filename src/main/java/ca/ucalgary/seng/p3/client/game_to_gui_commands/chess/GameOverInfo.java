package ca.ucalgary.seng.p3.client.game_to_gui_commands.chess;

public class GameOverInfo {
    public boolean checkmate;       // if checkmate then true, if draw then false
    // if checkmate, winner is determined by value of whiteMoved in Move command

    public GameOverInfo(boolean checkmate) {
        this.checkmate = checkmate;
    }
}
