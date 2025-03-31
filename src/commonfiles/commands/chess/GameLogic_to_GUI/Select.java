package commonfiles.commands.chess.GameLogic_to_GUI;

import commonfiles.commands.chess.ChessCommand;

import java.util.ArrayList;

public class Select extends ChessCommand {
    ArrayList<String> moveOptions;

    public Select() {
        super("SELECT");
    }
}
